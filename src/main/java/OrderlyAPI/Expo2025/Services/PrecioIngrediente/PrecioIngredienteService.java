package OrderlyAPI.Expo2025.Services.PrecioIngrediente;

import OrderlyAPI.Expo2025.Entities.Empleado.EmpleadoEntity;
import OrderlyAPI.Expo2025.Entities.PrecioIngrediente.PrecioIngredienteEntity;
import OrderlyAPI.Expo2025.Entities.Rol.RolEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.EmpleadoDTO;
import OrderlyAPI.Expo2025.Models.DTO.PrecioIngredienteDTO;
import OrderlyAPI.Expo2025.Models.DTO.RolDTO;
import OrderlyAPI.Expo2025.Repositories.PrecioIngrediente.PrecioIngredienteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PrecioIngredienteService {
    private PrecioIngredienteRepository repo;

    public List<PrecioIngredienteDTO> getAllPrecioIngredientes(){
        List<PrecioIngredienteEntity> roles = repo.findAll();
        return roles.stream()
                .map(this::convertirAPrecioIngredientesDTO)
                .collect(Collectors.toList());
    }

    public PrecioIngredienteDTO createPrecioIngrediente(PrecioIngredienteDTO precioIngredienteDTO){
        if (precioIngredienteDTO == null || precioIngredienteDTO.getIdIngrediente() == null || precioIngredienteDTO.getIdIngrediente().describeConstable().isEmpty()){
            throw new IllegalArgumentException("El precio ingrediente no puede ser nulo");
        }
        try{
            PrecioIngredienteEntity precioIngredienteEntity = convertirAPrecioIngredientesEntity(precioIngredienteDTO);
            PrecioIngredienteEntity precioIngredienteGuardado = repo.save(precioIngredienteEntity);
            return convertirAPrecioIngredientesDTO(precioIngredienteGuardado);
        }catch (Exception e){
            log.error("Error al registrar precio ingrediente: " + e.getMessage());
            throw new ExceptionDatoNoEncontrado("Error al registrar el precio ingrediente" + e.getMessage());
        }
    }

    public PrecioIngredienteDTO updatePrecioIngrediente(Long id, PrecioIngredienteDTO precioIngrediente){
        PrecioIngredienteEntity precioIngredienteExistente = repo.findById(id).orElseThrow(() -> new ExceptionDatoNoEncontrado("Precio ingrediente no encontrado"));

        precioIngredienteExistente.setIdIngrediente(precioIngrediente.getIdIngrediente());
        precioIngredienteExistente.setPrecio(precioIngrediente.getPrecio());
        precioIngredienteExistente.setFInicio(precioIngrediente.getFInicio());
        precioIngredienteExistente.setFFin(precioIngrediente.getFFin());

        PrecioIngredienteEntity precioIngredienteActualizado = repo.save(precioIngredienteExistente);
        return convertirAPrecioIngredientesDTO(precioIngredienteActualizado);
    }

    public boolean deletePrecioIngrediente(Long id){
        try{
            PrecioIngredienteEntity objPrecioIngrediente = repo.findById(id).orElse(null);
            if (objPrecioIngrediente != null){
                repo.deleteById(id);
                return true;
            }else{
                System.out.println("Precio Ingrediente no encontrado");
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro precio ingrediente con ID:" + id + " para eliminar.", 1);
        }
    }


    public PrecioIngredienteEntity convertirAPrecioIngredientesEntity(PrecioIngredienteDTO precioIngrediente){
        PrecioIngredienteEntity dto = new PrecioIngredienteEntity();
        dto.setId(precioIngrediente.getId());
        dto.setIdIngrediente(precioIngrediente.getIdIngrediente());
        dto.setPrecio(precioIngrediente.getPrecio());
        dto.setFInicio(precioIngrediente.getFInicio());
        dto.setFFin(precioIngrediente.getFFin());
        return dto;
    }

    public PrecioIngredienteDTO convertirAPrecioIngredientesDTO(PrecioIngredienteEntity precioIngrediente){
        PrecioIngredienteDTO dto = new PrecioIngredienteDTO();
        dto.setId(precioIngrediente.getId());
        dto.setIdIngrediente(precioIngrediente.getIdIngrediente());
        dto.setPrecio(precioIngrediente.getPrecio());
        dto.setFInicio(precioIngrediente.getFInicio());
        dto.setFFin(precioIngrediente.getFFin());
        return dto;
    }
}
