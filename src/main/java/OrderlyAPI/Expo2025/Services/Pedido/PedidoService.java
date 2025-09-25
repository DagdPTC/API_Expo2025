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
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PedidoService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /* =========================================================
       LISTAR (paginado)  — desde tabla PEDIDO
       ========================================================= */
    // LISTAR (usa HORA_INICIO / HORA_FIN)
    public Page<PedidoDTO> getDataPedido(int page, int size) {
        if (size <= 0) size = 10;
        if (page < 0) page = 0;

        long total = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM PEDIDO", Long.class);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        String sql =
                "SELECT IDPEDIDO, NOMBRECLIENTE, IDMESA, IDEMPLEADO, " +
                        "       HORAINICIO, HORAFIN, IDESTADOPEDIDO, " +
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
    // OBTENER POR ID (usa HORA_INICIO / HORA_FIN)
    public PedidoDTO getById(Long id) {
        String sql =
                "SELECT IDPEDIDO, NOMBRECLIENTE, IDMESA, IDEMPLEADO, " +
                        "       HORAINICIO, HORAFIN, IDESTADOPEDIDO, " +
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
    // CREAR (inserta HORA_INICIO con la hora del servidor; HORA_FIN = NULL)
    @Transactional
    public PedidoDTO createPedido(PedidoDTO dto) {
        validarDto(dto, true);

        Long idNuevo = jdbcTemplate.queryForObject("SELECT PEDIDO_SEQ.NEXTVAL FROM DUAL", Long.class);

        // Construimos INSERT respetando los nombres reales de columnas en Oracle
        List<String> cols = new ArrayList<>(Arrays.asList(
                "IDPEDIDO", "NOMBRECLIENTE", "IDMESA", "IDEMPLEADO", "IDESTADOPEDIDO",
                "OBSERVACIONES", "SUBTOTAL", "PROPINA", "TOTALPEDIDO"
        ));
        List<String> vals = new ArrayList<>(Arrays.asList(
                "?", "?", "?", "?", "?", "?", "?", "?", "?"
        ));
        List<Object> args = new ArrayList<>(Arrays.asList(
                idNuevo,
                nvl(dto.getNombreCliente(), ""),
                req(dto.getIdMesa()),
                req(dto.getIdEmpleado()),
                req(dto.getIdEstadoPedido()),
                nvl(dto.getObservaciones(), ""),
                toDouble(dto.getSubtotal()),
                toDouble(dto.getPropina()),
                toDouble(dto.getTotalPedido())
        ));

        // HORA INICIO = hora del usuario (FPedido) o SYSTIMESTAMP si no vino
        if (dto.getFPedido() != null) {
            cols.add("HORAINICIO");
            vals.add("?");
            args.add(java.sql.Timestamp.valueOf(dto.getFPedido()));
        } else {
            cols.add("HORAINICIO");
            vals.add("SYSTIMESTAMP");
        }

        // HORA FIN empieza en NULL
        cols.add("HORAFIN");
        vals.add("NULL");

        String insert = "INSERT INTO PEDIDO (" + String.join(", ", cols) + ") VALUES (" + String.join(", ", vals) + ")";
        jdbcTemplate.update(insert, args.toArray());

        List<PedidoItemDTO> compact = compactarItems(dto.getItems());
        insertarDetalle(idNuevo, compact);

        return getById(idNuevo);
    }





    /* =========================================================
       MODIFICAR
       ========================================================= */
    // MODIFICAR (si cambias a estado 'finalizado' y HORA_FIN es NULL, se pone SYSTIMESTAMP)
    @Transactional
    public PedidoDTO modificarPedido(Long id, PedidoDTO dto) {
        if (id == null) throw new IllegalArgumentException("ID de pedido requerido");
        validarDto(dto, false);

        // Estado actual y hora fin actual
        Long estadoActual = jdbcTemplate.queryForObject(
                "SELECT IDESTADOPEDIDO FROM PEDIDO WHERE IDPEDIDO = ?",
                Long.class, id
        );
        java.sql.Timestamp finActual = jdbcTemplate.query(
                "SELECT HORAFIN FROM PEDIDO WHERE IDPEDIDO = ?",
                ps -> ps.setLong(1, id),
                rs -> rs.next() ? rs.getTimestamp(1) : null
        );

        boolean pasaAFinalizado =
                !Objects.equals(estadoActual, dto.getIdEstadoPedido()) &&
                        isEstadoFinalizado(dto.getIdEstadoPedido());

        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE PEDIDO SET ");
        sb.append("NOMBRECLIENTE = ?, ");
        sb.append("IDMESA = ?, ");
        sb.append("IDEMPLEADO = ?, ");
        sb.append("IDESTADOPEDIDO = ?, ");
        sb.append("OBSERVACIONES = ?, ");
        sb.append("SUBTOTAL = ?, ");
        sb.append("PROPINA = ?, ");
        sb.append("TOTALPEDIDO = ?");

        List<Object> args = new ArrayList<>(Arrays.asList(
                nvl(dto.getNombreCliente(), ""),
                req(dto.getIdMesa()),
                req(dto.getIdEmpleado()),
                req(dto.getIdEstadoPedido()),
                nvl(dto.getObservaciones(), ""),
                toDouble(dto.getSubtotal()),
                toDouble(dto.getPropina()),
                toDouble(dto.getTotalPedido())
        ));

        // Si pasó a finalizado y aún no tiene hora fin → la marcamos
        if (pasaAFinalizado && finActual == null) {
            sb.append(", HORAFIN = SYSTIMESTAMP");
        }

        sb.append(" WHERE IDPEDIDO = ?");
        args.add(id);

        int rows = jdbcTemplate.update(sb.toString(), args.toArray());
        if (rows == 0) throw new RuntimeException("Pedido no encontrado");

        // Regeneramos el detalle
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

    // ROW MAPPER (lee HORA_INICIO como FPedido y mapea HORA_FIN si añadiste el campo en el DTO)
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

                // ← NUEVO: HORA DE INICIO para que el JSON tenga fecha/hora
                try {
                    java.sql.Timestamp ts = rs.getTimestamp("HORAINICIO");
                    if (ts != null) dto.setFPedido(ts.toLocalDateTime());
                } catch (Exception ignore) { }

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
        // Hora de inicio (FPedido) es @NotNull en tu DTO; si el front por algún motivo no la manda,
        // createPedido arma una hora por defecto (LocalDateTime.now()) para no chocar con NOT NULL.
    }

    private String nvl(String s, String def) {
        return (s == null) ? def : s;
    }

    private Double toDouble(Number n) {
        return (n == null) ? 0.0 : n.doubleValue();
    }

    private Long req(Long v) {
        if (v == null) throw new IllegalArgumentException("Valor requerido");
        return v;
    }

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

    // ===== Helpers de estado =====

    private String nombreEstadoById(Long idEstado) {
        if (idEstado == null) return null;
        return jdbcTemplate.queryForObject(
                "SELECT NOMBREESTADO FROM ESTADOPEDIDO WHERE IDESTADOPEDIDO = ?",
                new Object[]{ idEstado },
                String.class
        );
    }
    /** Determina si un id de estado corresponde a “finalizado”.
     *  Busca el nombre del estado y chequea si contiene "final". */
    private boolean isEstadoFinalizado(Long idEstado) {
        if (idEstado == null) return false;
        try {
            // Detecta el nombre de la columna de texto del estado
            String col =
                    hasColumn("ESTADOPEDIDO", "NOMESTADO") ? "NOMESTADO" :
                            (hasColumn("ESTADOPEDIDO", "NOMESTADOPEDIDO") ? "NOMESTADOPEDIDO" :
                                    (hasColumn("ESTADOPEDIDO", "NOMBRE") ? "NOMBRE" : null));
            if (col == null) return false;

            String nombre = jdbcTemplate.queryForObject(
                    "SELECT " + col + " FROM ESTADOPEDIDO WHERE IDESTADOPEDIDO = ?",
                    String.class, idEstado
            );
            if (nombre == null) return false;
            String n = nombre.toUpperCase();
            return n.contains("FINAL") || n.contains("PAG") || n.contains("ENTREG")
                    || n.contains("CERR") || n.contains("COMP");
        } catch (Exception e) {
            return false;
        }
    }


    /** Devuelve el nombre del estado por id, probando nombres de columna comunes. */
    private String getNombreEstadoById(Long idEstado) {
        if (idEstado == null) return null;
        String[] cols = { "NOMESTADO", "NOMESTADOPEDIDO", "NOMBRE", "NOMBRE_ESTADO" };
        for (String c : cols) {
            try {
                return jdbcTemplate.queryForObject(
                        "SELECT " + c + " FROM ESTADOPEDIDO WHERE IDESTADOPEDIDO = ?",
                        String.class, idEstado
                );
            } catch (Exception ignore) { }
        }
        return null;
    }

    private boolean hasColumn(String table, String column) {
        String sql = "SELECT COUNT(*) FROM ALL_TAB_COLUMNS " +
                "WHERE UPPER(TABLE_NAME)=? AND UPPER(COLUMN_NAME)=?";
        Integer c = jdbcTemplate.queryForObject(sql, Integer.class,
                table.toUpperCase(), column.toUpperCase());
        return c != null && c > 0;
    }



}
