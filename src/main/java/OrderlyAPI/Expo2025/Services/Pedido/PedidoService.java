package OrderlyAPI.Expo2025.Services.Pedido;

import OrderlyAPI.Expo2025.Models.DTO.PedidoDTO;
import OrderlyAPI.Expo2025.Models.DTO.PedidoItemDTO;
import OrderlyAPI.Expo2025.Services.Factura.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

@Service
public class PedidoService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private FacturaService facturaService;

    /* =========================================================
       TUS MÉTODOS ORIGINALES (SIN CAMBIOS)
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

    @Transactional
    public PedidoDTO createPedido(PedidoDTO dto) {
        validarDto(dto, true);

        Long idNuevo = jdbcTemplate.queryForObject("SELECT PEDIDO_SEQ.NEXTVAL FROM DUAL", Long.class);

        String insert =
                "INSERT INTO PEDIDO " +
                        "(IDPEDIDO, NOMBRECLIENTE, IDMESA, IDEMPLEADO, FECHAPEDIDO, HORAINICIO, HORAFIN, IDESTADOPEDIDO, " +
                        " OBSERVACIONES, SUBTOTAL, PROPINA, TOTALPEDIDO) " +
                        "VALUES (?, ?, ?, ?, SYSDATE, SYSDATE, NULL, ?, ?, ?, ?, ?)";

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

        jdbcTemplate.update("DELETE FROM PEDIDODETALLE WHERE IDPEDIDO = ?", id);
        List<PedidoItemDTO> compact = compactarItems(dto.getItems());
        insertarDetalle(id, compact);

        return getById(id);
    }

    @Transactional
    public boolean eliminarPedido(Long id) {
        jdbcTemplate.update("DELETE FROM PEDIDODETALLE WHERE IDPEDIDO = ?", id);
        int r = jdbcTemplate.update("DELETE FROM PEDIDO WHERE IDPEDIDO = ?", id);
        return r > 0;
    }

    /* =========================================================
       MÉTODOS NUEVOS PARA FACTURA AUTOMÁTICA
       ========================================================= */

    @Transactional
    public PedidoDTO finalizarPedido(Long idPedido) {
        try {
            System.out.println("=== 🚀 INICIANDO FINALIZAR PEDIDO ID: " + idPedido + " ===");

            // 1. Verificar que el pedido existe
            PedidoDTO pedido = getById(idPedido);
            System.out.println("✅ Pedido encontrado: " + pedido.getId());

            // 2. Verificar que no esté ya finalizado
            if (pedido.getHoraFin() != null) {
                throw new RuntimeException("El pedido ya está finalizado");
            }
            System.out.println("✅ Pedido no está finalizado");

            // 3. Verificar si ya tiene factura
            if (tieneFactura(idPedido)) {
                throw new RuntimeException("El pedido ya tiene una factura generada");
            }
            System.out.println("✅ Pedido no tiene factura previa");

            // 4. Actualizar el pedido: establecer horaFin y cambiar estado a "Entregado" (ID=3)
            String updatePedido = "UPDATE PEDIDO SET HORAFIN = SYSDATE, IDESTADOPEDIDO = 3 WHERE IDPEDIDO = ?";
            int rowsUpdated = jdbcTemplate.update(updatePedido, idPedido);

            if (rowsUpdated == 0) {
                throw new RuntimeException("No se pudo actualizar el pedido");
            }
            System.out.println("✅ Pedido actualizado correctamente (HoraFin + Estado=3)");

            // 5. Generar factura automáticamente con estado "Sin pagar" (ID=1)
            System.out.println("📋 Generando factura automática...");
            generarFacturaAutomatica(pedido);
            System.out.println("✅ Factura generada correctamente");

            // 6. Obtener el pedido actualizado
            PedidoDTO pedidoActualizado = getById(idPedido);
            System.out.println("=== 🎉 FINALIZAR PEDIDO COMPLETADO ===");

            return pedidoActualizado;

        } catch (Exception e) {
            System.err.println("❌ ERROR al finalizar pedido: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al finalizar pedido: " + e.getMessage(), e);
        }
    }

    /**
     * Genera una factura automáticamente para un pedido finalizado
     */
    private void generarFacturaAutomatica(PedidoDTO pedido) {
        try {
            System.out.println("🔄 Generando factura automática para pedido: " + pedido.getId());

            // Usar FacturaService para crear la factura
            facturaService.generarFacturaAutomaticaDesdePedido(pedido);
            System.out.println("✅ Factura generada exitosamente");

        } catch (Exception e) {
            System.err.println("❌ ERROR al generar factura automática: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al generar factura automática: " + e.getMessage(), e);
        }
    }

    /**
     * Verifica si un pedido tiene factura
     */
    public boolean tieneFactura(Long idPedido) {
        String sql = "SELECT COUNT(*) FROM FACTURA WHERE IDPEDIDO = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, idPedido);
        return count != null && count > 0;
    }

    /* =========================================================
       TUS HELPERS ORIGINALES (SIN CAMBIOS)
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

                // Mapear HoraFin
                java.sql.Timestamp horaFin = rs.getTimestamp("HORAFIN");
                if (horaFin != null) {
                    dto.setHoraFin(horaFin.toLocalDateTime());
                }

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
}