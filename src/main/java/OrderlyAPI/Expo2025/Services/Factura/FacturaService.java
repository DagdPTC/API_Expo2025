package OrderlyAPI.Expo2025.Services.Factura;

import OrderlyAPI.Expo2025.Entities.Factura.FacturaEntity;
import OrderlyAPI.Expo2025.Entities.Pedido.PedidoEntity;
import OrderlyAPI.Expo2025.Entities.EstadoFactura.EstadoFacturaEntity;
import OrderlyAPI.Expo2025.Models.DTO.FacturaDTO;
import OrderlyAPI.Expo2025.Models.DTO.PedidoDTO;
import OrderlyAPI.Expo2025.Repositories.Factura.FacturaRepository;
import OrderlyAPI.Expo2025.Repositories.Pedido.PedidoRepository;
import OrderlyAPI.Expo2025.Repositories.EstadoFactura.EstadoFacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.validation.Valid;
import java.util.Optional;

@Service
public class FacturaService {

    @Autowired
    private FacturaRepository repo;

    @Autowired
    private PedidoRepository pedidoRepo;

    @Autowired
    private EstadoFacturaRepository estadoFacturaRepo;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /* =========================================================
       TUS MÉTODOS ORIGINALES (SIN CAMBIOS)
       ========================================================= */

    public Page<FacturaDTO> getAllFacturas(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<FacturaEntity> factura = repo.findAll(pageable);
        return factura.map(this::convertirAFacturaDTO);
    }

    public FacturaDTO createFacturas(@Valid FacturaDTO dto){
        if (dto == null) throw new IllegalArgumentException("La factura no puede ser nula");

        try{
            FacturaEntity e = convertirAFacturaEntity(dto);
            if (dto.getIdPedido() != null) {
                PedidoEntity pedRef = pedidoRepo.getReferenceById(dto.getIdPedido());
                e.setPedido(pedRef);
            }
            FacturaEntity guardado = repo.save(e);
            return convertirAFacturaDTO(guardado);
        }catch (Exception ex){
            throw new RuntimeException("Error al registrar la factura: " + ex.getMessage());
        }
    }

    public boolean deleteFactura(Long id){
        FacturaEntity obj = repo.findById(id).orElse(null);
        if (obj == null) return false;
        repo.deleteById(id);
        return true;
    }

    /* =========================================================
       MÉTODO NUEVO PARA GENERAR FACTURA AUTOMÁTICA
       ========================================================= */

    @Transactional
    public void generarFacturaAutomaticaDesdePedido(PedidoDTO pedidoDTO) {
        try {
            System.out.println("🔄 [FACTURA SERVICE] Generando factura automática desde pedido: " + pedidoDTO.getId());

            // 1. Verificar si ya existe factura para este pedido
            if (existeFacturaParaPedido(pedidoDTO.getId())) {
                throw new RuntimeException("Ya existe una factura para el pedido ID: " + pedidoDTO.getId());
            }

            // 2. Buscar el pedido en la base de datos
            PedidoEntity pedido = pedidoRepo.findById(pedidoDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + pedidoDTO.getId()));
            System.out.println("✅ Pedido encontrado: " + pedido.getId());

            // 3. Buscar el estado de factura "Sin pagar" (ID=1)
            EstadoFacturaEntity estadoFactura = estadoFacturaRepo.findById(1L)
                    .orElseThrow(() -> new RuntimeException("Estado de factura 'Sin pagar' no encontrado"));
            System.out.println("✅ Estado factura encontrado: " + estadoFactura.getEstadoFactura());

            // 4. Crear la entidad Factura
            FacturaEntity factura = new FacturaEntity();
            factura.setDescuento(0.0);
            factura.setTotal(pedidoDTO.getTotalPedido());
            factura.setPedido(pedido);
            factura.setEstadoFactura(estadoFactura);

            // 5. Guardar la factura
            System.out.println("💾 Guardando factura en base de datos...");
            FacturaEntity facturaGuardada = repo.save(factura);
            System.out.println("✅ Factura guardada con ID: " + facturaGuardada.getId());
            System.out.println("🎉 Factura automática generada exitosamente");

        } catch (Exception ex) {
            System.err.println("❌ ERROR al generar factura automática:");
            System.err.println("   - Mensaje: " + ex.getMessage());
            ex.printStackTrace();
            throw new RuntimeException("Error al generar factura automática: " + ex.getMessage());
        }
    }

    /**
     * Verifica si ya existe una factura para un pedido
     */
    private boolean existeFacturaParaPedido(Long idPedido) {
        try {
            String sql = "SELECT COUNT(*) FROM FACTURA WHERE IDPEDIDO = ?";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, idPedido);
            boolean existe = count != null && count > 0;
            System.out.println("🔍 Verificando factura para pedido " + idPedido + ": " + (existe ? "EXISTE" : "NO EXISTE"));
            return existe;
        } catch (Exception e) {
            System.err.println("❌ Error al verificar factura existente: " + e.getMessage());
            return false;
        }
    }

    /* =========================================================
       TUS MÉTODOS DE CONVERSIÓN ORIGINALES (SIN CAMBIOS)
       ========================================================= */

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