package OrderlyAPI.Expo2025.Services.RecetaPlatillo;

import OrderlyAPI.Expo2025.Entities.RecetaPlatillo.RecetaPlatilloEntity;
import OrderlyAPI.Expo2025.Entities.Rol.RolEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.RecetaPlatilloDTO;
import OrderlyAPI.Expo2025.Models.DTO.RolDTO;
import OrderlyAPI.Expo2025.Repositories.RecetaPlatillo.RecetaPlatilloRepository;
import OrderlyAPI.Expo2025.Repositories.Rol.RolRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RecetaPlatilloService {
    private RecetaPlatilloRepository repo;


    public List<RecetaPlatilloDTO> getAllRecetaPlatillos(){
        List<RecetaPlatilloEntity> recetas = repo.findAll();
        return recetas.stream()
                .map(this::convertirARecetaPlatillosDTO)
                .collect(Collectors.toList());
    }

    public RecetaPlatilloDTO createRecetaPlatillo(RecetaPlatilloDTO recetaPlatilloDTO){
        if (recetaPlatilloDTO == null || recetaPlatilloDTO.getIdPlatillo() == null || recetaPlatilloDTO.getIdPlatillo().describeConstable().isEmpty()){
            throw new IllegalArgumentException("La receta platillo no puede ser nulo");
        }
        try{
            RecetaPlatilloEntity recetaPlatilloEntity = convertirARecetaPlatillosEntity(recetaPlatilloDTO);
            RecetaPlatilloEntity recetaPlatilloGuardado = repo.save(recetaPlatilloEntity);
            return convertirARecetaPlatillosDTO(recetaPlatilloGuardado);
        }catch (Exception e){
            log.error("Error al registrar receta platillo: " + e.getMessage());
            throw new ExceptionDatoNoEncontrado("Error al registrar el receta platillo" + e.getMessage());
        }
    }

    public RecetaPlatilloDTO updateRecetaPlatillo(Long id, RecetaPlatilloDTO recetaPlatillo){
        RecetaPlatilloEntity recetaPlatilloExistente = repo.findById(id).orElseThrow(() -> new ExceptionDatoNoEncontrado("Receta Platillo no encontrado"));

        recetaPlatilloExistente.setIdPlatillo(recetaPlatillo.getIdPlatillo());
        recetaPlatilloExistente.setIdIngrediente(recetaPlatillo.getIdIngrediente());
        recetaPlatilloExistente.setCantidad(recetaPlatillo.getCantidad());
        recetaPlatilloExistente.setIdUnidadMedida(recetaPlatillo.getIdUnidadMedida());

        RecetaPlatilloEntity recetaPlatilloActualizado = repo.save(recetaPlatilloExistente);
        return convertirARecetaPlatillosDTO(recetaPlatilloActualizado);
    }

    public boolean deleteRecetaPlatillo(Long id){
        try{
            RecetaPlatilloEntity objRecetaPlatillo = repo.findById(id).orElse(null);
            if (objRecetaPlatillo != null){
                repo.deleteById(id);
                return true;
            }else{
                System.out.println("Receta Platillo no encontrado");
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro receta platillo con ID:" + id + " para eliminar.", 1);
        }
    }


    public RecetaPlatilloEntity convertirARecetaPlatillosEntity(RecetaPlatilloDTO recetaPlatillo){
        RecetaPlatilloEntity dto = new RecetaPlatilloEntity();
        dto.setId(recetaPlatillo.getId());
        dto.setIdPlatillo(recetaPlatillo.getIdPlatillo());
        dto.setIdIngrediente(recetaPlatillo.getIdIngrediente());
        dto.setCantidad(recetaPlatillo.getCantidad());
        dto.setIdUnidadMedida(recetaPlatillo.getIdUnidadMedida());
        return dto;
    }

    public RecetaPlatilloDTO convertirARecetaPlatillosDTO(RecetaPlatilloEntity recetaPlatillo){
        RecetaPlatilloDTO dto = new RecetaPlatilloDTO();
        dto.setId(recetaPlatillo.getId());
        dto.setIdPlatillo(recetaPlatillo.getIdPlatillo());
        dto.setIdIngrediente(recetaPlatillo.getIdIngrediente());
        dto.setCantidad(recetaPlatillo.getCantidad());
        dto.setIdUnidadMedida(recetaPlatillo.getIdUnidadMedida());
        return dto;
    }
}
