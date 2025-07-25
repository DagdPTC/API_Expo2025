package OrderlyAPI.Expo2025.Services.Factura;

import OrderlyAPI.Expo2025.Entities.Factura.FacturaEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.FacturaDTO;
import OrderlyAPI.Expo2025.Repositories.Factura.FacturaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FacturaService {

    @Autowired
    private FacturaRepository repo;


    public java.util.List<FacturaDTO> getAllFacturas(){
        List<FacturaEntity> factura = repo.findAll();
        return factura.stream()
                .map(this::convertirAFacturaDTO)
                .collect(Collectors.toList());
    }

    public FacturaDTO createFacturas(FacturaDTO facturaDTO){
        if (facturaDTO == null || facturaDTO.getIdPedido() == null || facturaDTO.getIdPedido().describeConstable().isEmpty()){
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

    public FacturaDTO updateFactura(Long id, FacturaDTO factura){
        FacturaEntity facturaExistente = repo.findById(id).orElseThrow(() -> new ExceptionDatoNoEncontrado("Historial pedidos no encontrado"));

        facturaExistente.setIdPedido(factura.getIdPedido());
        facturaExistente.setSubTotal(factura.getSubTotal());
        facturaExistente.setDescuento(factura.getDescuento());
        facturaExistente.setPropina(factura.getPropina());
        facturaExistente.setTotal(factura.getTotal());
        facturaExistente.setIdEmpleado(factura.getIdEmpleado());

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
        entity.setIdPedido(factura.getIdPedido());
        entity.setSubTotal(factura.getSubTotal());
        entity.setDescuento(factura.getDescuento());
        entity.setPropina(factura.getPropina());
        entity.setTotal(factura.getTotal());
        entity.setIdEmpleado(factura.getIdEmpleado());
        return entity;
    }

    public FacturaDTO convertirAFacturaDTO(FacturaEntity factura){
        FacturaDTO dto = new FacturaDTO();
        dto.setId(factura.getId());
        dto.setIdPedido(factura.getIdPedido());
        dto.setSubTotal(factura.getSubTotal());
        dto.setDescuento(factura.getDescuento());
        dto.setPropina(factura.getPropina());
        dto.setTotal(factura.getTotal());
        dto.setIdEmpleado(factura.getIdEmpleado());
        return dto;
    }
}
