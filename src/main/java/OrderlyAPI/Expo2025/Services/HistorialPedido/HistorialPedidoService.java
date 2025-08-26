package OrderlyAPI.Expo2025.Services.HistorialPedido;

import OrderlyAPI.Expo2025.Entities.Factura.FacturaEntity;
import OrderlyAPI.Expo2025.Entities.HistorialPedido.HistorialPedidoEntity;
import OrderlyAPI.Expo2025.Entities.Pedido.PedidoEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.HistorialPedidoDTO;
import OrderlyAPI.Expo2025.Repositories.HistorialPedido.HistorialPedidoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class HistorialPedidoService {

    @Autowired
    private HistorialPedidoRepository repo;

    @PersistenceContext
    EntityManager entityManager;

    public Page<HistorialPedidoDTO> getAllhistorialpedido(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<HistorialPedidoEntity> historialpedido = repo.findAll(pageable);
        return historialpedido.map(this::convertirAHistorialPedidoDTO);
    }

    @Transactional
    public HistorialPedidoDTO createHistorialPediso(@Valid HistorialPedidoDTO dto){
        if (dto == null) throw new IllegalArgumentException("El historial pedido no puede ser nulo");
        if (dto.getIdPedido() == null || dto.getIdFactura() == null)
            throw new IllegalArgumentException("idPedido e idFactura son obligatorios");

        try {
            // Validar existencia
            PedidoEntity pedido = entityManager.find(PedidoEntity.class, dto.getIdPedido());
            if (pedido == null) throw new ExceptionDatoNoEncontrado("Pedido no encontrado (id=" + dto.getIdPedido() + ")");

            FacturaEntity factura = entityManager.find(FacturaEntity.class, dto.getIdFactura());
            if (factura == null) throw new ExceptionDatoNoEncontrado("Factura no encontrada (id=" + dto.getIdFactura() + ")");

            // Evitar duplicar la factura en historial (UNIQUE)
            if (repo.existsByFactura_Id(dto.getIdFactura()))
                throw new IllegalArgumentException("La factura ya tiene historial");

            // Coherencia: factura debe pertenecer a ese pedido (si tu dominio lo exige)
            if (!factura.getPedido().getId().equals(pedido.getId()))
                throw new IllegalArgumentException("La factura no pertenece al pedido indicado");

            HistorialPedidoEntity entity = new HistorialPedidoEntity();
            // NO setees ID en create: lo genera la secuencia
            entity.setPedidos(pedido);
            entity.setFactura(factura);

            HistorialPedidoEntity saved = repo.save(entity);
            return convertirAHistorialPedidoDTO(saved);

        } catch (DataIntegrityViolationException e){
            // por cualquier constraint de BD
            throw e;
        } catch (RuntimeException e){
            log.error("Error al registrar historial pedido: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Transactional
    public HistorialPedidoDTO updateHistorialpedido(Long id, @Valid HistorialPedidoDTO in){
        HistorialPedidoEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionDatoNoEncontrado("Historial pedido no encontrado (id=" + id + ")"));

        // Cambiar pedido si lo envían y es distinto
        if (in.getIdPedido() != null && !in.getIdPedido().equals(existente.getPedidos().getId())) {
            PedidoEntity pedido = entityManager.find(PedidoEntity.class, in.getIdPedido());
            if (pedido == null) throw new ExceptionDatoNoEncontrado("Pedido no encontrado (id=" + in.getIdPedido() + ")");
            existente.setPedidos(pedido);
        }

        // Cambiar factura si la envían y es distinta
        if (in.getIdFactura() != null && !in.getIdFactura().equals(existente.getFactura().getId())) {
            if (repo.existsByFactura_IdAndIdNot(in.getIdFactura(), id))
                throw new IllegalArgumentException("La factura ya tiene historial");

            FacturaEntity factura = entityManager.find(FacturaEntity.class, in.getIdFactura());
            if (factura == null) throw new ExceptionDatoNoEncontrado("Factura no encontrada (id=" + in.getIdFactura() + ")");

            // Asegurar coherencia (opcional pero recomendable):
            if (!factura.getPedido().getId().equals(existente.getPedidos().getId()))
                throw new IllegalArgumentException("La factura no pertenece al pedido actual");

            existente.setFactura(factura);
        }

        HistorialPedidoEntity guardado = repo.save(existente);
        return convertirAHistorialPedidoDTO(guardado);
    }

    public boolean deleteHistorialpedido(Long id){
        return repo.findById(id).map(h -> { repo.delete(h); return true; }).orElse(false);
    }

    // ====== mapeos ======
    private HistorialPedidoDTO convertirAHistorialPedidoDTO(HistorialPedidoEntity h){
        HistorialPedidoDTO dto = new HistorialPedidoDTO();
        dto.setId(h.getId());
        dto.setIdPedido(h.getPedidos().getId());
        dto.setIdFactura(h.getFactura().getId());
        return dto;
    }
}
