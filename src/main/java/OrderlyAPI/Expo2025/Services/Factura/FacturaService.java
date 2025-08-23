package OrderlyAPI.Expo2025.Services.Factura;

import OrderlyAPI.Expo2025.Entities.Factura.FacturaEntity;
import OrderlyAPI.Expo2025.Entities.Pedido.PedidoEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.FacturaDTO;
import OrderlyAPI.Expo2025.Repositories.Factura.FacturaRepository;
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
public class FacturaService {

    @Autowired
    private FacturaRepository repo;

    @PersistenceContext
    EntityManager entityManager;

    public Page<FacturaDTO> getAllFacturas(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<FacturaEntity> factura = repo.findAll(pageable);
        return factura.map(this::convertirAFacturaDTO);
    }

    public FacturaDTO createFacturas(@Valid FacturaDTO facturaDTO){
        if (facturaDTO == null){
            throw new IllegalArgumentException("La factura no puede ser nulo");
        }
        try{
            FacturaEntity facturaEntity = convertirAFacturaEntity(facturaDTO);
            FacturaEntity facturaGuardado = repo.save(facturaEntity);
            return convertirAFacturaDTO(facturaGuardado);
        }catch (Exception e){
            log.error("Error al registrar factura: " + e.getMessage());
            throw new ExceptionDatoNoEncontrado("Error al registrar la factura" + e.getMessage());
        }
    }

    public FacturaDTO updateFactura(Long id, @Valid FacturaDTO factura){
        FacturaEntity facturaExistente = repo.findById(id).orElseThrow(() -> new ExceptionDatoNoEncontrado("Historial pedidos no encontrado"));

        facturaExistente.setPedido(facturaExistente.getPedido());
        facturaExistente.setDescuento(factura.getDescuento());
        facturaExistente.setTotal(factura.getTotal());

        FacturaEntity facturaActualizado = repo.save(facturaExistente);
        return convertirAFacturaDTO(facturaActualizado);
    }

    public boolean deleteFactura(Long id){
        try{
            FacturaEntity objFactura = repo.findById(id).orElse(null);
            if (objFactura != null){
                repo.deleteById(id);
                return true;
            }else{
                System.out.println("Factura no encontrada");
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro la factura con ID:" + id + " para eliminar.", 1);
        }
    }


    public FacturaEntity convertirAFacturaEntity(FacturaDTO factura){
        FacturaEntity entity = new FacturaEntity();
        entity.setId(factura.getId());
        entity.setPedido(entityManager.getReference(PedidoEntity.class, factura.getIdPedido()));
        entity.setDescuento(factura.getDescuento());
        entity.setTotal(factura.getTotal());
        return entity;
    }

    public FacturaDTO convertirAFacturaDTO(FacturaEntity factura){
        FacturaDTO dto = new FacturaDTO();
        dto.setId(factura.getId());
        dto.setIdPedido(factura.getPedido().getId());
        dto.setDescuento(factura.getDescuento());
        dto.setTotal(factura.getTotal());
        return dto;
    }
}
