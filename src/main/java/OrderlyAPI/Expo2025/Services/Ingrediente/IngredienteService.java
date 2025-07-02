package OrderlyAPI.Expo2025.Services.Ingrediente;

import OrderlyAPI.Expo2025.Entities.Empleado.EmpleadoEntity;
import OrderlyAPI.Expo2025.Entities.Ingrediente.IngredienteEntity;
import OrderlyAPI.Expo2025.Entities.Rol.RolEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.EmpleadoDTO;
import OrderlyAPI.Expo2025.Models.DTO.IngredienteDTO;
import OrderlyAPI.Expo2025.Models.DTO.RolDTO;
import OrderlyAPI.Expo2025.Repositories.Empleado.EmpleadoRepository;
import OrderlyAPI.Expo2025.Repositories.Ingrediente.IngredienteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class IngredienteService {
    private IngredienteRepository repo;

    public List<IngredienteDTO> getAllIngredientes(){
        List<IngredienteEntity> roles = repo.findAll();
        return roles.stream()
                .map(this::convertirAIngredientesDTO)
                .collect(Collectors.toList());
    }

    public IngredienteDTO createIngrediente(IngredienteDTO ingredienteDTO){
        if (ingredienteDTO == null || ingredienteDTO.getNomIngrediente() == null || ingredienteDTO.getNomIngrediente().isEmpty()){
            throw new IllegalArgumentException("El ingrediente no puede ser nulo");
        }
        try{
            IngredienteEntity ingredienteEntity = convertirAIngredientesEntity(ingredienteDTO);
            IngredienteEntity ingredienteGuardado = repo.save(ingredienteEntity);
            return convertirAIngredientesDTO(ingredienteGuardado);
        }catch (Exception e){
            log.error("Error al registrar ingrediente: " + e.getMessage());
            throw new ExceptionDatoNoEncontrado("Error al registrar el ingrediente" + e.getMessage());
        }
    }

    public IngredienteDTO updateIngrediente(Long id, IngredienteDTO ingrediente){
        IngredienteEntity ingredienteExistente = repo.findById(id).orElseThrow(() -> new ExceptionDatoNoEncontrado("Ingrediente no encontrado"));

        ingredienteExistente.setNomIngrediente(ingrediente.getNomIngrediente());
        ingredienteExistente.setCantidad(ingrediente.getCantidad());
        ingredienteExistente.setIdCategoria(ingrediente.getIdCategoria());
        ingredienteExistente.setIdUnidadMedida(ingrediente.getIdUnidadMedida());
        ingredienteExistente.setCosteUnidad(ingrediente.getCosteUnidad());

        IngredienteEntity ingredienteActualizado = repo.save(ingredienteExistente);
        return convertirAIngredientesDTO(ingredienteActualizado);
    }

    public boolean deleteIngrediente(Long id){
        try{
            IngredienteEntity objIngrediente = repo.findById(id).orElse(null);
            if (objIngrediente != null){
                repo.deleteById(id);
                return true;
            }else{
                System.out.println("Ingrediente no encontrado");
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro ingrediente con ID:" + id + " para eliminar.", 1);
        }
    }


    public IngredienteEntity convertirAIngredientesEntity(IngredienteDTO ingrediente){
        IngredienteEntity dto = new IngredienteEntity();
        dto.setId(ingrediente.getId());
        dto.setNomIngrediente(ingrediente.getNomIngrediente());
        dto.setCantidad(ingrediente.getCantidad());
        dto.setIdCategoria(ingrediente.getIdCategoria());
        dto.setIdUnidadMedida(ingrediente.getIdUnidadMedida());
        dto.setCosteUnidad(ingrediente.getCosteUnidad());
        return dto;
    }

    public IngredienteDTO convertirAIngredientesDTO(IngredienteEntity ingrediente){
        IngredienteDTO dto = new IngredienteDTO();
        dto.setId(ingrediente.getId());
        dto.setNomIngrediente(ingrediente.getNomIngrediente());
        dto.setCantidad(ingrediente.getCantidad());
        dto.setIdCategoria(ingrediente.getIdCategoria());
        dto.setIdUnidadMedida(ingrediente.getIdUnidadMedida());
        dto.setCosteUnidad(ingrediente.getCosteUnidad());
        return dto;
    }
}
