package OrderlyAPI.Expo2025.Services.MetodoPago;

import OrderlyAPI.Expo2025.Entities.MetodoPago.MetodoPagoEntity;
import OrderlyAPI.Expo2025.Entities.Rol.RolEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.MetodoPagoDTO;
import OrderlyAPI.Expo2025.Models.DTO.RolDTO;
import OrderlyAPI.Expo2025.Repositories.MetodoPago.MetodoPagoRepository;
import OrderlyAPI.Expo2025.Repositories.Rol.RolRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MetodoPagoService {

    @Autowired
    private MetodoPagoRepository repo;


    public List<MetodoPagoDTO> getAllMetodoPagos(){
        List<MetodoPagoEntity> pagos = repo.findAll();
        return pagos.stream()
                .map(this::convertirAMetodoPagoDTO)
                .collect(Collectors.toList());
    }

    public MetodoPagoDTO createMetodoPago(MetodoPagoDTO metodoPagoDTO){
        if (metodoPagoDTO == null || metodoPagoDTO.getNomMetodo() == null || metodoPagoDTO.getNomMetodo().isEmpty()){
            throw new IllegalArgumentException("El metodo pago no puede ser nulo");
        }
        try{
            MetodoPagoEntity metodoPagoEntity = convertirAMetodoPagoEntity(metodoPagoDTO);
            MetodoPagoEntity metodoPagoGuardado = repo.save(metodoPagoEntity);
            return convertirAMetodoPagoDTO(metodoPagoGuardado);
        }catch (Exception e){
            log.error("Error al registrar metodo pago: " + e.getMessage());
            throw new ExceptionDatoNoEncontrado("Error al registrar el metodo pago" + e.getMessage());
        }
    }

    public MetodoPagoDTO updateMetodoPago(Long id, MetodoPagoDTO metodoPago){
        MetodoPagoEntity metodoPagoExistente = repo.findById(id).orElseThrow(() -> new ExceptionDatoNoEncontrado("Metodo pago no encontrado"));

        metodoPagoExistente.setNomMetodo(metodoPago.getNomMetodo());

        MetodoPagoEntity metodoPagoActualizado = repo.save(metodoPagoExistente);
        return convertirAMetodoPagoDTO(metodoPagoActualizado);
    }

    public boolean deleteMetodoPago(Long id){
        try{
            MetodoPagoEntity objMetodoPago = repo.findById(id).orElse(null);
            if (objMetodoPago != null){
                repo.deleteById(id);
                return true;
            }else{
                System.out.println("Metodo Pago no encontrado");
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro el metodo pago con ID:" + id + " para eliminar.", 1);
        }
    }


    public MetodoPagoEntity convertirAMetodoPagoEntity(MetodoPagoDTO dto){
        MetodoPagoEntity entity = new MetodoPagoEntity();
        entity.setId(dto.getId());
        entity.setNomMetodo(dto.getNomMetodo());
        return entity;
    }

    public MetodoPagoDTO convertirAMetodoPagoDTO(MetodoPagoEntity metodoPago){
        MetodoPagoDTO dto = new MetodoPagoDTO();
        dto.setId(metodoPago.getId());
        dto.setNomMetodo(metodoPago.getNomMetodo());
        return dto;
    }
}
