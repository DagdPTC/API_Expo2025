package OrderlyAPI.Expo2025.Services.Factura;

import OrderlyAPI.Expo2025.Entities.Factura.FacturaEntity;
import OrderlyAPI.Expo2025.Entities.Pedido.PedidoEntity;
import OrderlyAPI.Expo2025.Entities.Platillo.PlatilloEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.FacturaDTO;
import OrderlyAPI.Expo2025.Repositories.Factura.FacturaRepository;
import OrderlyAPI.Expo2025.Repositories.Pedido.PedidoRepository;
import OrderlyAPI.Expo2025.Repositories.Platillo.PlatilloRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@CrossOrigin
public class FacturaService {

    @Autowired private FacturaRepository repo;
    @Autowired private PedidoRepository pedidoRepo;
    @Autowired private PlatilloRepository platilloRepo;

    private static final double TIP_RATE = 0.10; // propina 10%

    public Page<FacturaDTO> getAllFacturas(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<FacturaEntity> factura = repo.findAll(pageable);
        return factura.map(this::convertirAFacturaDTO);
    }

    public FacturaDTO createFacturas(@Valid FacturaDTO dto){
        if (dto == null) throw new IllegalArgumentException("La factura no puede ser nula");

        try{
            FacturaEntity e = convertirAFacturaEntity(dto);
            // Si viene IdPedido en el DTO, asociamos referencia (sin EntityManager)
            if (dto.getIdPedido() != null) {
                PedidoEntity pedRef = pedidoRepo.getReferenceById(dto.getIdPedido());
                e.setPedido(pedRef);
            }
            FacturaEntity guardado = repo.save(e);
            return convertirAFacturaDTO(guardado);
        }catch (Exception ex){
            log.error("Error al registrar factura: {}", ex.getMessage());
            throw new RuntimeException("Error al registrar la factura: " + ex.getMessage());
        }
    }

    public boolean deleteFactura(Long id){
        FacturaEntity obj = repo.findById(id).orElse(null);
        if (obj == null) return false;
        repo.deleteById(id);
        return true;
    }

    /**
     * Transaccional único:
     * - Cambia platillo/cantidad (si vienen)
     * - Recalcula Subtotal, Propina(10%), TotalPedido
     * - Aplica DescuentoPct (0..100) y calcula TotalFactura
     * - Persiste Pedido y Factura
     * - Devuelve Map con totales
     */
    @Transactional
    public Map<String, Object> actualizarCompleto(Long idFactura,
                                                  Long idPedido,
                                                  Long idPlatillo,   // opcional
                                                  Long cantidad,     // opcional (>=1)
                                                  Double descuentoPct) {
        FacturaEntity factura = repo.findById(idFactura)
                .orElseThrow(() -> new ExceptionDatoNoEncontrado("Factura no encontrada"));
        PedidoEntity pedido = pedidoRepo.findById(idPedido)
                .orElseThrow(() -> new ExceptionDatoNoEncontrado("Pedido no encontrado"));

        // Cambiar platillo si viene
        if (idPlatillo != null && (pedido.getPlatillo() == null ||
                !idPlatillo.equals(pedido.getPlatillo().getId()))) {
            PlatilloEntity plat = platilloRepo.findById(idPlatillo)
                    .orElseThrow(() -> new ExceptionDatoNoEncontrado("Platillo no encontrado"));
            pedido.setPlatillo(plat);
        }

        // Cambiar cantidad si viene
        if (cantidad != null && cantidad > 0) {
            pedido.setCantidad(cantidad);
        } else if (pedido.getCantidad() == null || pedido.getCantidad() < 1) {
            pedido.setCantidad(1L);
        }

        // Recalcular siempre
        double precio   = pedido.getPlatillo() != null ? pedido.getPlatillo().getPrecio() : 0.0;
        long cant       = pedido.getCantidad() != null ? pedido.getCantidad() : 1L;
        double subtotal = round2(precio * cant);
        double propina  = round2(subtotal * TIP_RATE); // 10%
        double totalPed = round2(subtotal + propina);

        pedido.setSubtotal(subtotal);
        pedido.setPropina(propina);
        pedido.setTotalPedido(totalPed);

        // DescuentoPct 0..100 (0 permitido)
        double pct = (descuentoPct == null ? 0.0 : Math.max(0.0, Math.min(100.0, descuentoPct)));
        double descMonto = round2(totalPed * (pct / 100.0));
        double totalFac  = round2(Math.max(0.0, totalPed - descMonto));

        factura.setDescuento(descMonto);
        factura.setTotal(totalFac);

        // Persistir
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
        return dto;
    }
}
