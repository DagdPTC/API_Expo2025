package OrderlyAPI.Expo2025.Services.UbicacionRestaurante;

import OrderlyAPI.Expo2025.Entities.Rol.RolEntity;
import OrderlyAPI.Expo2025.Entities.UbicacionRestaurante.UbicacionRestauranteEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.RolDTO;
import OrderlyAPI.Expo2025.Models.DTO.UbicacionRestauranteDTO;
import OrderlyAPI.Expo2025.Repositories.Rol.RolRepository;
import OrderlyAPI.Expo2025.Repositories.UbicacionRestaurante.UbicacionRestauranteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UbicacionRestauranteService {
    private UbicacionRestauranteRepository repo;


    public List<UbicacionRestauranteDTO> getAllUbicacion(){
        List<UbicacionRestauranteEntity> ubicacion = repo.findAll();
        return ubicacion.stream()
                .map(this::convertirAUbicacionRestauranteDTO)
                .collect(Collectors.toList());
    }

    public UbicacionRestauranteDTO createUbicacionRestaurante(UbicacionRestauranteDTO ubicacionRestauranteDTO){
        if (ubicacionRestauranteDTO == null || ubicacionRestauranteDTO.getNomUbicacion() == null || ubicacionRestauranteDTO.getNomUbicacion().isEmpty()){
            throw new IllegalArgumentException("La Ubicacion Restaurante no puede ser nulo");
        }
        try{
            UbicacionRestauranteEntity ubicacionRestauranteEntity = convertirAUbicacionRestauranteEntity(ubicacionRestauranteDTO);
            UbicacionRestauranteEntity ubicacionRestauranteGuardado = repo.save(ubicacionRestauranteEntity);
            return convertirAUbicacionRestauranteDTO(ubicacionRestauranteGuardado);
        }catch (Exception e){
            log.error("Error al registrar ubicacion restaurante: " + e.getMessage());
            throw new ExceptionDatoNoEncontrado("Error al registrar la ubicacion restaurante" + e.getMessage());
        }
    }

    public UbicacionRestauranteDTO updateUbicacionRestaurante(Long id, UbicacionRestauranteDTO ubicacionRestaurante){
        UbicacionRestauranteEntity ubicacionRestauranteExistente = repo.findById(id).orElseThrow(() -> new ExceptionDatoNoEncontrado("Ubicacion Restaurante no encontrado"));

        ubicacionRestauranteExistente.setNomUbicacion(ubicacionRestaurante.getNomUbicacion());
        ubicacionRestauranteExistente.setDescripcion(ubicacionRestaurante.getDescripcion());

        UbicacionRestauranteEntity ubicacionRestauranteActualizado = repo.save(ubicacionRestauranteExistente);
        return convertirAUbicacionRestauranteDTO(ubicacionRestauranteActualizado);
    }

    public boolean deleteUbicacionRestaurante(Long id){
        try{
            UbicacionRestauranteEntity objUbicacionRestaurante = repo.findById(id).orElse(null);
            if (objUbicacionRestaurante != null){
                repo.deleteById(id);
                return true;
            }else{
                System.out.println("Ubicacion Restaurante no encontrado");
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro ubicacion restaurante con ID:" + id + " para eliminar.", 1);
        }
    }


    public UbicacionRestauranteEntity convertirAUbicacionRestauranteEntity(UbicacionRestauranteDTO ubicacionRestaurante){
        UbicacionRestauranteEntity dto = new UbicacionRestauranteEntity();
        dto.setId(ubicacionRestaurante.getId());
        dto.setNomUbicacion(ubicacionRestaurante.getNomUbicacion());
        dto.setDescripcion(ubicacionRestaurante.getDescripcion());
        return dto;
    }

    public UbicacionRestauranteDTO convertirAUbicacionRestauranteDTO(UbicacionRestauranteEntity ubicacionRestaurante){
        UbicacionRestauranteDTO dto = new UbicacionRestauranteDTO();
        dto.setId(ubicacionRestaurante.getId());
        dto.setNomUbicacion(ubicacionRestaurante.getNomUbicacion());
        dto.setDescripcion(ubicacionRestaurante.getDescripcion());
        return dto;
    }
}
