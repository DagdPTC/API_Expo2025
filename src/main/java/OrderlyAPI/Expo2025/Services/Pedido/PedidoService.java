package OrderlyAPI.Expo2025.Services.Pedido;

import OrderlyAPI.Expo2025.Models.DTO.PedidoDTO;
import OrderlyAPI.Expo2025.Models.DTO.PedidoItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PedidoService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /* =========================================================
       LISTAR (paginado)  — desde tabla PEDIDO
       ========================================================= */
    public Page<PedidoDTO> getDataPedido(int page, int size) {
        if (size <= 0) size = 10;
        if (page < 0) page = 0;

        long total = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM PEDIDO", Long.class);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        String sql =
                "SELECT IDPEDIDO, NOMBRECLIENTE, IDMESA, IDEMPLEADO, FECHAPEDIDO, HORAINICIO, HORAFIN, IDESTADOPEDIDO, " +
                        "       OBSERVACIONES, SUBTOTAL, PROPINA, TOTALPEDIDO " +
                        "FROM PEDIDO " +
                        "ORDER BY IDPEDIDO DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        List<PedidoDTO> content = jdbcTemplate.query(
                sql,
                new Object[]{ page * size, size },
                pedidoRowMapper()
        );

        for (PedidoDTO dto : content) {
            dto.setItems(listarItems(dto.getId()));
        }

        return new PageImpl<>(content, pageable, total);
    }

    /* =========================================================
       OBTENER POR ID
       ========================================================= */
    public PedidoDTO getById(Long id) {
        String sql =
                "SELECT IDPEDIDO, NOMBRECLIENTE, IDMESA, IDEMPLEADO, FECHAPEDIDO, HORAINICIO, HORAFIN, IDESTADOPEDIDO, " +
                        "       OBSERVACIONES, SUBTOTAL, PROPINA, TOTALPEDIDO " +
                        "FROM PEDIDO WHERE IDPEDIDO = ?";
        List<PedidoDTO> list = jdbcTemplate.query(sql, new Object[]{ id }, pedidoRowMapper());
        if (list.isEmpty()) throw new RuntimeException("Pedido no encontrado");
        PedidoDTO dto = list.get(0);
        dto.setItems(listarItems(id));
        return dto;
    }

    /* =========================================================
       CREAR
       ========================================================= */
    @Transactional
    public PedidoDTO createPedido(PedidoDTO dto) {
        validarDto(dto, true);

        // Tomamos ID de la secuencia (aunque hay trigger, esto nos da el ID para insertar detalle)
        Long idNuevo = jdbcTemplate.queryForObject("SELECT PEDIDO_SEQ.NEXTVAL FROM DUAL", Long.class);

        String insert =
                "INSERT INTO PEDIDO (IDPEDIDO, NOMBRECLIENTE, IDMESA, IDEMPLEADO, FECHAPEDIDO," +
                        "                    HORAINICIO, IDESTADOPEDIDO, OBSERVACIONES, SUBTOTAL, PROPINA, TOTALPEDIDO)\n" +
                        "VALUES (?, ?, ?, ?, SYSDATE, SYSDATE, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(
                insert,
                idNuevo,
                nvl(dto.getNombreCliente(), ""),
                req(dto.getIdMesa()),
                req(dto.getIdEmpleado()),
                req(dto.getIdEstadoPedido()),
                nvl(dto.getObservaciones(), ""),
                toDouble(dto.getSubtotal()),
                toDouble(dto.getPropina()),
                toDouble(dto.getTotalPedido())
        );

        List<PedidoItemDTO> compact = compactarItems(dto.getItems());
        insertarDetalle(idNuevo, compact);

        return getById(idNuevo);
    }

    /* =========================================================
       MODIFICAR
       ========================================================= */
    @Transactional
    public PedidoDTO modificarPedido(Long id, PedidoDTO dto) {
        if (id == null) throw new IllegalArgumentException("ID de pedido requerido");
        validarDto(dto, false);

        String update =
                "UPDATE PEDIDO SET " +
                        "NOMBRECLIENTE = ?, " +
                        "IDMESA = ?, " +
                        "IDEMPLEADO = ?, " +
                        "IDESTADOPEDIDO = ?, " +
                        "OBSERVACIONES = ?, " +
                        "SUBTOTAL = ?, " +
                        "PROPINA = ?, " +
                        "TOTALPEDIDO = ? " +
                        "WHERE IDPEDIDO = ?";

        int rows = jdbcTemplate.update(
                update,
                nvl(dto.getNombreCliente(), ""),
                req(dto.getIdMesa()),
                req(dto.getIdEmpleado()),
                req(dto.getIdEstadoPedido()),
                nvl(dto.getObservaciones(), ""),
                toDouble(dto.getSubtotal()),
                toDouble(dto.getPropina()),
                toDouble(dto.getTotalPedido()),
                id
        );
        if (rows == 0) throw new RuntimeException("Pedido no encontrado");

        // Regenerar detalle para evitar ORA-00001 (UQ_PedidoDet_Linea)
        jdbcTemplate.update("DELETE FROM PEDIDODETALLE WHERE IDPEDIDO = ?", id);
        List<PedidoItemDTO> compact = compactarItems(dto.getItems());
        insertarDetalle(id, compact);

        return getById(id);
    }

    /* =========================================================
       ELIMINAR
       ========================================================= */
    @Transactional
    public boolean eliminarPedido(Long id) {
        jdbcTemplate.update("DELETE FROM PEDIDODETALLE WHERE IDPEDIDO = ?", id);
        int r = jdbcTemplate.update("DELETE FROM PEDIDO WHERE IDPEDIDO = ?", id);
        return r > 0;
    }

    /* =========================================================
       HELPERS
       ========================================================= */

    private RowMapper<PedidoDTO> pedidoRowMapper() {
        return new RowMapper<PedidoDTO>() {
            @Override
            public PedidoDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                PedidoDTO dto = new PedidoDTO();
                dto.setId(rs.getLong("IDPEDIDO"));
                dto.setNombreCliente(rs.getString("NOMBRECLIENTE"));

                long v;
                v = rs.getLong("IDMESA");         if (!rs.wasNull()) dto.setIdMesa(v);
                v = rs.getLong("IDEMPLEADO");     if (!rs.wasNull()) dto.setIdEmpleado(v);
                v = rs.getLong("IDESTADOPEDIDO"); if (!rs.wasNull()) dto.setIdEstadoPedido(v);

                java.sql.Timestamp tIni = rs.getTimestamp("HORAINICIO");
                java.sql.Timestamp tFp  = rs.getTimestamp("FECHAPEDIDO");
                if (tIni != null) {
                    dto.setFPedido(tIni.toLocalDateTime());
                } else if (tFp != null) {
                           dto.setFPedido(tFp.toLocalDateTime());
                }

                java.sql.Timestamp tFin = rs.getTimestamp("HORAFIN");
                if (tFin != null) dto.setHoraFin(tFin.toLocalDateTime());

                dto.setObservaciones(rs.getString("OBSERVACIONES"));
                dto.setSubtotal(rs.getDouble("SUBTOTAL"));
                dto.setPropina(rs.getDouble("PROPINA"));
                dto.setTotalPedido(rs.getDouble("TOTALPEDIDO"));
                return dto;
            }
        };
    }

    private List<PedidoItemDTO> listarItems(Long idPedido) {
        String sql =
                "SELECT IDPLATILLO, CANTIDAD, PRECIOUNITARIO " +
                        "FROM PEDIDODETALLE WHERE IDPEDIDO = ? ORDER BY IDDETALLE";

        return jdbcTemplate.query(sql, new Object[]{ idPedido }, new RowMapper<PedidoItemDTO>() {
            @Override
            public PedidoItemDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                PedidoItemDTO it = new PedidoItemDTO();
                it.setIdPlatillo(rs.getLong("IDPLATILLO"));
                it.setCantidad(rs.getInt("CANTIDAD"));
                it.setPrecioUnitario(rs.getDouble("PRECIOUNITARIO"));
                return it;
            }
        });
    }

    private void insertarDetalle(Long idPedido, List<PedidoItemDTO> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Items: Debe incluir al menos un platillo");
        }
        final String sql =
                "INSERT INTO PEDIDODETALLE (IDPEDIDO, IDPLATILLO, CANTIDAD, PRECIOUNITARIO) " +
                        "VALUES (?, ?, ?, ?)";

        for (PedidoItemDTO it : items) {
            if (it == null) continue;
            Long idPlat = it.getIdPlatillo();
            Integer cant = (it.getCantidad() != null && it.getCantidad() > 0) ? it.getCantidad() : 1;
            Double precio = toDouble(it.getPrecioUnitario());
            if (idPlat == null) throw new IllegalArgumentException("Items: idPlatillo es requerido");
            jdbcTemplate.update(sql, idPedido, idPlat, cant, precio);
        }
    }

    /** Reglas mínimas: nombre, ids y al menos un item. Totales los dejamos tal como vengan (el frontend ya los calcula). */
    private void validarDto(PedidoDTO dto, boolean crear) {
        if (dto == null) throw new IllegalArgumentException("Payload requerido");
        if (dto.getNombreCliente() == null || dto.getNombreCliente().trim().isEmpty()) {
            throw new IllegalArgumentException("NombreCliente es requerido");
        }
        if (dto.getIdMesa() == null)         throw new IllegalArgumentException("IdMesa es requerido");
        if (dto.getIdEmpleado() == null)     throw new IllegalArgumentException("IdEmpleado es requerido");
        if (dto.getIdEstadoPedido() == null) throw new IllegalArgumentException("IdEstadoPedido es requerido");
        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new IllegalArgumentException("Items: Debe incluir al menos un platillo");
        }
        // Nota: NO comparamos dto.getSubtotal()/getPropina()/getTotalPedido() con null
        // porque en tu DTO podrían ser primitivos (double) y eso da error de compilación.
        // Se mandan tal cual desde el frontend.
    }

    private String nvl(String s, String def) {
        return (s == null) ? def : s;
    }

    private Double toDouble(Number n) {
        return (n == null) ? 0.0 : n.doubleValue();
    }

    /** Valida requerido (no nulo) para IDs. */
    private Long req(Long v) {
        if (v == null) throw new IllegalArgumentException("Valor requerido");
        return v;
    }

    /**
     * Agrupa items por IdPlatillo sumando cantidades, conservando el último precio unitario no nulo.
     * Evita violar UQ_PedidoDet_Linea (IdPedido, IdPlatillo).
     */
    private List<PedidoItemDTO> compactarItems(List<PedidoItemDTO> items) {
        if (items == null) return Collections.emptyList();
        Map<Long, PedidoItemDTO> map = new LinkedHashMap<>();
        for (PedidoItemDTO it : items) {
            if (it == null || it.getIdPlatillo() == null) continue;
            long idPlat = it.getIdPlatillo();
            int cant = (it.getCantidad() != null && it.getCantidad() > 0) ? it.getCantidad() : 1;
            double precio = toDouble(it.getPrecioUnitario());
            PedidoItemDTO acc = map.get(idPlat);
            if (acc == null) {
                PedidoItemDTO n = new PedidoItemDTO();
                n.setIdPlatillo(idPlat);
                n.setCantidad(cant);
                n.setPrecioUnitario(precio);
                map.put(idPlat, n);
            } else {
                acc.setCantidad(acc.getCantidad() + cant);
                if (precio > 0) acc.setPrecioUnitario(precio);
            }
        }
        return new ArrayList<>(map.values());
    }
}
