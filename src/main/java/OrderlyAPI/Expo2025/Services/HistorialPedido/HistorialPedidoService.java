package OrderlyAPI.Expo2025.Services.HistorialPedido;

import OrderlyAPI.Expo2025.Entities.Factura.FacturaEntity;
import OrderlyAPI.Expo2025.Entities.HistorialPedido.HistorialPedidoEntity;
import OrderlyAPI.Expo2025.Entities.Pedido.PedidoEntity;
import OrderlyAPI.Expo2025.Entities.Rol.RolEntity;
import OrderlyAPI.Expo2025.Entities.TipoDocumento.TipoDocumentoEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.HistorialPedidoDTO;
import OrderlyAPI.Expo2025.Models.DTO.RolDTO;
import OrderlyAPI.Expo2025.Repositories.HistorialPedido.HistorialPedidoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@CrossOrigin
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

    public HistorialPedidoDTO createHistorialPediso(@Valid HistorialPedidoDTO historialpedidoDTO){
        if (historialpedidoDTO == null){
            throw new IllegalArgumentException("El historila pedido no puede ser nulo");
        }
        try{
            HistorialPedidoEntity historialpedidoEntity = convertirAHistorialPedidoEntity(historialpedidoDTO);
            HistorialPedidoEntity historialpedidoGuardado = repo.save(historialpedidoEntity);
            return convertirAHistorialPedidoDTO(historialpedidoGuardado);
        }catch (Exception e){
            log.error("Error al registrar historial pedido: " + e.getMessage());
            throw new ExceptionDatoNoEncontrado("Error al registrar el historial pedido" + e.getMessage());
        }
    }

    public HistorialPedidoDTO updateHistorialpedido(Long id, @Valid HistorialPedidoDTO historialpedido){
        HistorialPedidoEntity historialpedidoExistente = repo.findById(id).orElseThrow(() -> new ExceptionDatoNoEncontrado("Historial pedido no encontrado"));

        historialpedidoExistente.setPedidos(historialpedidoExistente.getPedidos());
        historialpedidoExistente.setFactura(historialpedidoExistente.getFactura());

        HistorialPedidoEntity historialpedidoActualizado = repo.save(historialpedidoExistente);
        return convertirAHistorialPedidoDTO(historialpedidoActualizado);
    }

    public boolean deleteHistorialpedido(Long id){
        try{
            HistorialPedidoEntity objHistorialpedido = repo.findById(id).orElse(null);
            if (objHistorialpedido != null){
                repo.deleteById(id);
                return true;
            }else{
                System.out.println("Historial pedido no encontrado");
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro historial pedido con ID:" + id + " para eliminar.", 1);
        }
    }


    public HistorialPedidoEntity convertirAHistorialPedidoEntity(HistorialPedidoDTO dto){
        HistorialPedidoEntity entity = new HistorialPedidoEntity();
        entity.setId(dto.getId());
        entity.setPedidos(entityManager.getReference(PedidoEntity.class, dto.getIdPedido()));
        entity.setFactura(entityManager.getReference(FacturaEntity.class, dto.getIdFactura()));
        return entity;
    }

    public HistorialPedidoDTO convertirAHistorialPedidoDTO(HistorialPedidoEntity historialpedido){
        HistorialPedidoDTO dto = new HistorialPedidoDTO();
        dto.setId(historialpedido.getId());
        dto.setIdPedido(historialpedido.getPedidos().getId());
        dto.setIdFactura(historialpedido.getFactura().getId());
        return dto;
    }
}
