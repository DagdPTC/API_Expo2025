package OrderlyAPI.Expo2025.Services.Factura;

import OrderlyAPI.Expo2025.Entities.EstadoFactura.EstadoFacturaEntity;
import OrderlyAPI.Expo2025.Entities.Factura.FacturaEntity;
import OrderlyAPI.Expo2025.Entities.Pedido.PedidoEntity;
import OrderlyAPI.Expo2025.Entities.PedidoDetalle.PedidoDetalleEntity;
import OrderlyAPI.Expo2025.Entities.Platillo.PlatilloEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.FacturaDTO;
import OrderlyAPI.Expo2025.Repositories.EstadoFactura.EstadoFacturaRepository;
import OrderlyAPI.Expo2025.Repositories.Factura.FacturaRepository;
import OrderlyAPI.Expo2025.Repositories.Pedido.PedidoRepository;
import OrderlyAPI.Expo2025.Repositories.Platillo.PlatilloRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;

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
    @Autowired private EstadoFacturaRepository estadoFacturaRepo; // <-- NUEVO

    private static final double TIP_RATE = 0.10; // propina 10%

    // ===================== READ =====================
    public Page<FacturaDTO> getAllFacturas(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<FacturaEntity> factura = repo.findAll(pageable);
        return factura.map(this::convertirAFacturaDTO);
    }

    // ===================== CREATE =====================
    public FacturaDTO createFacturas(@Valid FacturaDTO dto){
        if (dto == null) throw new IllegalArgumentException("La factura no puede ser nula");

        try{
            FacturaEntity e = convertirAFacturaEntity(dto);

            // Asociar Pedido por referencia si viene IdPedido
            if (dto.getIdPedido() != null) {
                PedidoEntity pedRef = pedidoRepo.getReferenceById(dto.getIdPedido());
                e.setPedido(pedRef);
            }

            // Asociar Estado de Factura si viene IdEstadoFactura
            if (dto.getIdEstadoFactura() != null) {
                EstadoFacturaEntity est = estadoFacturaRepo.findById(dto.getIdEstadoFactura())
                        .orElseThrow(() -> new ExceptionDatoNoEncontrado("Estado de factura no encontrado"));
                e.setEstadoFactura(est);
            }

            FacturaEntity guardado = repo.save(e);
            return convertirAFacturaDTO(guardado);
        }catch (Exception ex){
            log.error("Error al registrar factura: {}", ex.getMessage());
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
     * - Si viene idEstadoFactura, actualiza el estado de la factura
     */
    @Transactional
    public Map<String, Object> actualizarCompleto(Long idFactura,
                                                  Long idPedido,
                                                  Long idPlatillo,   // opcional (línea a upsert)
                                                  Long cantidad,     // opcional (>=1)
                                                  Double descuentoPct,
                                                  Long idEstadoFactura) { // <-- NUEVO parámetro

        FacturaEntity factura = repo.findById(idFactura)
                .orElseThrow(() -> new ExceptionDatoNoEncontrado("Factura no encontrada"));
        PedidoEntity pedido = pedidoRepo.findById(idPedido)
                .orElseThrow(() -> new ExceptionDatoNoEncontrado("Pedido no encontrado"));

        // Actualizar estado de factura si viene
        if (idEstadoFactura != null) {
            EstadoFacturaEntity est = estadoFacturaRepo.findById(idEstadoFactura)
                    .orElseThrow(() -> new ExceptionDatoNoEncontrado("Estado de factura no encontrado"));
            factura.setEstadoFactura(est);
        }

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
        out.put("IdEstadoFactura", factura.getEstadoFactura() != null ? factura.getEstadoFactura().getId() : null);
        out.put("EstadoFactura",   factura.getEstadoFactura() != null ? factura.getEstadoFactura().getEstadoFactura() : null);
        return out;
    }

    // ===================== Helpers =====================

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
        // Estado se setea en create/actualizarCompleto cuando tenemos el id
        return e;
    }

    public FacturaDTO convertirAFacturaDTO(FacturaEntity e){
        FacturaDTO dto = new FacturaDTO();
        dto.setId(e.getId());
        dto.setIdPedido(e.getPedido() != null ? e.getPedido().getId() : null);
        dto.setDescuento(e.getDescuento());
        dto.setTotal(e.getTotal());
        if (e.getEstadoFactura() != null) {
            dto.setIdEstadoFactura(e.getEstadoFactura().getId());
            dto.setEstadoFactura(e.getEstadoFactura().getEstadoFactura());
        }
        return dto;
    }

    // ===================== UTIL PÚBLICO =====================

    /**
     * Crea una factura en estado "Sin pagar" para un pedido si todavía no existe.
     * Se usa desde PedidoService cuando un pedido queda FINALIZADO.
     */
    @Transactional
    public FacturaEntity crearFacturaSinPagarSiNoExiste(Long idPedido) {
        if (idPedido == null) throw new IllegalArgumentException("IdPedido requerido");

        // ¿Ya existe factura para este pedido?
        FacturaEntity existente = repo.findFirstByPedidoId(idPedido).orElse(null);
        if (existente != null) return existente;

        PedidoEntity pedido = pedidoRepo.findById(idPedido)
                .orElseThrow(() -> new ExceptionDatoNoEncontrado("Pedido no encontrado"));

        // Buscar estado "Sin pagar"
        EstadoFacturaEntity est = estadoFacturaRepo.findByEstadoFacturaIgnoreCase("Sin pagar")
                .orElseGet(() -> {
                    // fallback por id si así lo tienes (p. ej. 1 = Sin pagar)
                    return estadoFacturaRepo.findById(1L)
                            .orElseThrow(() -> new ExceptionDatoNoEncontrado("No existe estado 'Sin pagar'"));
                });

        // Calcular totales desde detalle
        double subtotal = recomputeSubtotalesDesdeDetalle(pedido);
        double propina  = round2(subtotal * TIP_RATE);
        double totalPed = round2(subtotal + propina);

        // Crear factura base
        FacturaEntity f = new FacturaEntity();
        f.setPedido(pedido);
        f.setEstadoFactura(est);
        f.setDescuento(0.0);
        f.setTotal(totalPed); // total inicial igual a total del pedido sin descuento

        return repo.save(f);
    }
}
