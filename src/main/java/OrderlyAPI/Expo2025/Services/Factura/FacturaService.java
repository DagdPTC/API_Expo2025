package OrderlyAPI.Expo2025.Services.Factura;

import OrderlyAPI.Expo2025.Entities.Factura.FacturaEntity;
import OrderlyAPI.Expo2025.Entities.Pedido.PedidoEntity;
import OrderlyAPI.Expo2025.Entities.PedidoDetalle.PedidoDetalleEntity;
import OrderlyAPI.Expo2025.Entities.Platillo.PlatilloEntity;
import OrderlyAPI.Expo2025.Entities.EstadoFactura.EstadoFacturaEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.FacturaDTO;
import OrderlyAPI.Expo2025.Repositories.Factura.FacturaRepository;
import OrderlyAPI.Expo2025.Repositories.Pedido.PedidoRepository;
import OrderlyAPI.Expo2025.Repositories.Platillo.PlatilloRepository;
import OrderlyAPI.Expo2025.Repositories.EstadoFactura.EstadoFacturaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@CrossOrigin
public class FacturaService {

    @Autowired private FacturaRepository repo;
    @Autowired private PedidoRepository pedidoRepo;
    @Autowired private PlatilloRepository platilloRepo;
    @Autowired private EstadoFacturaRepository estadoFacturaRepo;
    @Autowired private JdbcTemplate jdbcTemplate;

    private static final double TIP_RATE = 0.10; // propina 10%

    // ===================== READ =====================
    public Page<FacturaDTO> getAllFacturas(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<FacturaEntity> factura = repo.findAll(pageable);
        return factura.map(this::convertirAFacturaDTO);
    }

    // ===================== CREATE - COMPLETAMENTE CORREGIDO =====================
    public FacturaDTO createFacturas(@Valid FacturaDTO dto){
        if (dto == null) throw new IllegalArgumentException("La factura no puede ser nula");

        try{
            System.out.println("🔄 [FACTURA SERVICE] Iniciando creación de factura...");
            System.out.println("📋 Datos recibidos - PedidoID: " + dto.getIdPedido() +
                    ", Total: " + dto.getTotal() +
                    ", EstadoFacturaID: " + dto.getIdEstadoFactura());

            // Verificar si ya existe una factura para este pedido
            if (dto.getIdPedido() != null && existeFacturaParaPedido(dto.getIdPedido())) {
                String errorMsg = "Ya existe una factura para el pedido ID: " + dto.getIdPedido();
                System.err.println("❌ " + errorMsg);
                throw new RuntimeException(errorMsg);
            }

            // Verificar que el pedido existe
            if (dto.getIdPedido() != null) {
                boolean pedidoExiste = pedidoRepo.existsById(dto.getIdPedido());
                if (!pedidoExiste) {
                    String errorMsg = "El pedido con ID: " + dto.getIdPedido() + " no existe";
                    System.err.println("❌ " + errorMsg);
                    throw new RuntimeException(errorMsg);
                }
                System.out.println("✅ Pedido validado: " + dto.getIdPedido());
            }

            // Verificar que el estado de factura existe
            if (dto.getIdEstadoFactura() != null) {
                boolean estadoExiste = estadoFacturaRepo.existsById(dto.getIdEstadoFactura());
                if (!estadoExiste) {
                    String errorMsg = "El estado de factura con ID: " + dto.getIdEstadoFactura() + " no existe";
                    System.err.println("❌ " + errorMsg);
                    throw new RuntimeException(errorMsg);
                }
                System.out.println("✅ Estado factura validado: " + dto.getIdEstadoFactura());
            }

            FacturaEntity e = convertirAFacturaEntity(dto);

            // Asociar Pedido por referencia si viene IdPedido
            if (dto.getIdPedido() != null) {
                try {
                    PedidoEntity pedRef = pedidoRepo.findById(dto.getIdPedido())
                            .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + dto.getIdPedido()));
                    e.setPedido(pedRef);
                    System.out.println("✅ Pedido asociado: " + dto.getIdPedido());
                } catch (Exception ex) {
                    System.err.println("❌ Error al asociar pedido: " + ex.getMessage());
                    throw new RuntimeException("Error al asociar pedido: " + ex.getMessage());
                }
            }

            // Asociar EstadoFactura por referencia
            if (dto.getIdEstadoFactura() != null) {
                try {
                    EstadoFacturaEntity estadoRef = estadoFacturaRepo.findById(dto.getIdEstadoFactura())
                            .orElseThrow(() -> new RuntimeException("Estado factura no encontrado con ID: " + dto.getIdEstadoFactura()));
                    e.setEstadoFactura(estadoRef);
                    System.out.println("✅ Estado factura asociado: " + dto.getIdEstadoFactura());
                } catch (Exception ex) {
                    System.err.println("❌ Error al asociar estado factura: " + ex.getMessage());
                    throw new RuntimeException("Error al asociar estado factura: " + ex.getMessage());
                }
            }

            System.out.println("💾 Guardando factura en base de datos...");
            FacturaEntity guardado = repo.save(e);
            System.out.println("✅ Factura creada exitosamente con ID: " + guardado.getId());

            FacturaDTO resultado = convertirAFacturaDTO(guardado);
            System.out.println("🎉 [FACTURA SERVICE] Creación de factura completada - ID: " + resultado.getId());

            return resultado;

        } catch (Exception ex){
            System.err.println("❌ [FACTURA SERVICE] ERROR CRÍTICO al registrar factura:");
            System.err.println("   - Mensaje: " + ex.getMessage());
            System.err.println("   - Causa: " + (ex.getCause() != null ? ex.getCause().getMessage() : "N/A"));
            ex.printStackTrace();
            throw new RuntimeException("Error al registrar la factura: " + ex.getMessage());
        }
    }

    // ===================== DELETE =====================
    public boolean deleteFactura(Long id){
        FacturaEntity obj = repo.findById(id).orElse(null);
        if (obj == null) return false;
        repo.deleteById(id);
        return true;
    }

    /**
     * Transaccional único (AJUSTADO a PedidoDetalle):
     * - Si vienen idPlatillo/cantidad: upsert de la línea (crear o actualizar cantidad)
     * - Recalcula Subtotal desde todas las líneas del pedido
     * - Propina = 10% del Subtotal; TotalPedido = Subtotal + Propina
     * - Aplica DescuentoPct (0..100) sobre TotalPedido y establece Total de la Factura
     */
    @Transactional
    public Map<String, Object> actualizarCompleto(Long idFactura,
                                                  Long idPedido,
                                                  Long idPlatillo,   // opcional (línea a upsert)
                                                  Long cantidad,     // opcional (>=1)
                                                  Double descuentoPct) {

        FacturaEntity factura = repo.findById(idFactura)
                .orElseThrow(() -> new ExceptionDatoNoEncontrado("Factura no encontrada"));
        PedidoEntity pedido = pedidoRepo.findById(idPedido)
                .orElseThrow(() -> new ExceptionDatoNoEncontrado("Pedido no encontrado"));

        // Asegurar lista de detalles inicializada
        if (pedido.getDetalles() == null) {
            throw new IllegalStateException("El pedido no tiene líneas de detalle inicializadas.");
        }

        // ==== UPSERT de la línea si viene idPlatillo ====
        if (idPlatillo != null) {
            PlatilloEntity plat = platilloRepo.findById(idPlatillo)
                    .orElseThrow(() -> new ExceptionDatoNoEncontrado("Platillo no encontrado"));

            PedidoDetalleEntity existente = findDetalleByPlatillo(pedido.getDetalles(), idPlatillo);

            if (existente != null) {
                // Actualizar cantidad si viene; si no viene, dejamos la existente
                if (cantidad != null && cantidad > 0) {
                    existente.setCantidad(cantidad.intValue());
                } else if (existente.getCantidad() == null || existente.getCantidad() < 1) {
                    existente.setCantidad(1);
                }
                // Asegurar precio unitario (si estuviera nulo)
                if (existente.getPrecioUnitario() == null) {
                    existente.setPrecioUnitario(plat.getPrecio());
                }
            } else {
                // Crear nueva línea
                PedidoDetalleEntity det = new PedidoDetalleEntity();
                det.setPedido(pedido);
                det.setPlatillo(plat);
                det.setCantidad((cantidad != null && cantidad > 0) ? cantidad.intValue() : 1);
                det.setPrecioUnitario(plat.getPrecio());
                pedido.getDetalles().add(det);
            }
        }

        // ==== Recalcular Subtotal desde todas las líneas ====
        double subtotal = recomputeSubtotalesDesdeDetalle(pedido);
        double propina  = round2(subtotal * TIP_RATE);
        double totalPed = round2(subtotal + propina);

        pedido.setSubtotal(subtotal);
        pedido.setPropina(propina);
        pedido.setTotalPedido(totalPed);

        // ==== Descuento de la factura ====
        double pct = (descuentoPct == null ? 0.0 : Math.max(0.0, Math.min(100.0, descuentoPct)));
        double descMonto = round2(totalPed * (pct / 100.0));
        double totalFac  = round2(Math.max(0.0, totalPed - descMonto));

        factura.setDescuento(descMonto);
        factura.setTotal(totalFac);

        // Persistir (cascade en Pedido guardará líneas nuevas/actualizadas)
        pedidoRepo.save(pedido);
        repo.save(factura);

        Map<String, Object> out = new HashMap<>();
        out.put("IdFactura", factura.getId());
        out.put("IdPedido", pedido.getId());
        out.put("Subtotal", subtotal);
        out.put("Propina", propina);
        out.put("TotalPedido", totalPed);
        out.put("DescuentoPct", pct);
        out.put("DescuentoMonto", descMonto);
        out.put("TotalFactura", totalFac);
        return out;
    }

    // ===================== Helpers =====================

    /**
     * Verifica si ya existe una factura para un pedido
     */
    private boolean existeFacturaParaPedido(Long idPedido) {
        try {
            String sql = "SELECT COUNT(*) FROM FACTURA WHERE IDPEDIDO = ?";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, idPedido);
            boolean existe = count != null && count > 0;
            System.out.println("🔍 Verificando factura existente para pedido " + idPedido + ": " + (existe ? "EXISTE" : "NO EXISTE"));
            return existe;
        } catch (Exception e) {
            System.err.println("❌ Error al verificar factura existente: " + e.getMessage());
            return false;
        }
    }

    /** Busca una línea por IdPlatillo en la lista (null si no existe). */
    private PedidoDetalleEntity findDetalleByPlatillo(List<PedidoDetalleEntity> detalles, Long idPlatillo) {
        if (detalles == null) return null;
        for (PedidoDetalleEntity d : detalles) {
            if (d.getPlatillo() != null && d.getPlatillo().getId() != null
                    && d.getPlatillo().getId().equals(idPlatillo)) {
                return d;
            }
        }
        return null;
    }

    /** Suma cantidad × precioUnitario de todas las líneas (asegurando valores válidos). */
    private double recomputeSubtotalesDesdeDetalle(PedidoEntity pedido) {
        double sum = 0.0;
        if (pedido.getDetalles() == null) return 0.0;

        for (PedidoDetalleEntity d : pedido.getDetalles()) {
            if (d == null) continue;
            int cant = (d.getCantidad() != null && d.getCantidad() > 0) ? d.getCantidad() : 0;
            if (cant == 0) continue;

            Double precio = d.getPrecioUnitario();
            if (precio == null) {
                // fallback al precio actual del platillo
                PlatilloEntity pl = d.getPlatillo();
                precio = (pl != null) ? pl.getPrecio() : 0.0;
                d.setPrecioUnitario(precio);
            }
            sum += precio * cant;
        }
        return round2(sum);
    }

    private double round2(double x){ return Math.round(x * 100.0) / 100.0; }

    // ====== Mappers ======
    public FacturaEntity convertirAFacturaEntity(FacturaDTO dto){
        FacturaEntity e = new FacturaEntity();
        e.setId(dto.getId());
        e.setDescuento(dto.getDescuento());
        e.setTotal(dto.getTotal());
        return e;
    }

    public FacturaDTO convertirAFacturaDTO(FacturaEntity e){
        FacturaDTO dto = new FacturaDTO();
        dto.setId(e.getId());
        dto.setIdPedido(e.getPedido() != null ? e.getPedido().getId() : null);
        dto.setDescuento(e.getDescuento());
        dto.setTotal(e.getTotal());
        dto.setIdEstadoFactura(e.getEstadoFactura() != null ? e.getEstadoFactura().getId() : null);
        return dto;
    }
}