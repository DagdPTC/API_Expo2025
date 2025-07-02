package OrderlyAPI.Expo2025.Services.unidadMedida;

import OrderlyAPI.Expo2025.Entities.Empleado.EmpleadoEntity;
import OrderlyAPI.Expo2025.Entities.Rol.RolEntity;
import OrderlyAPI.Expo2025.Entities.UnidadMedida.UnidadMedidaEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.EmpleadoDTO;
import OrderlyAPI.Expo2025.Models.DTO.RolDTO;
import OrderlyAPI.Expo2025.Models.DTO.UnidadMedidaDTO;
import OrderlyAPI.Expo2025.Repositories.Empleado.EmpleadoRepository;
import OrderlyAPI.Expo2025.Repositories.UnidadMedida.UnidadMedidaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UnidadMedidaService {
    private UnidadMedidaRepository repo;

    public List<UnidadMedidaDTO> getAllUnidadMedidas(){
        List<UnidadMedidaEntity> roles = repo.findAll();
        return roles.stream()
                .map(this::convertirAUnidadMedidasDTO)
                .collect(Collectors.toList());
    }

    public UnidadMedidaDTO createUnidadMedia(UnidadMedidaDTO unidadMedidaDTO){
        if (unidadMedidaDTO == null || unidadMedidaDTO.getNombre() == null || unidadMedidaDTO.getNombre().isEmpty()){
            throw new IllegalArgumentException("La unidad medida no puede ser nulo");
        }
        try{
            UnidadMedidaEntity unidadMedidaEntity = convertirAUnidadMedidasEntity(unidadMedidaDTO);
            UnidadMedidaEntity unidadMedidaGuardado = repo.save(unidadMedidaEntity);
            return convertirAUnidadMedidasDTO(unidadMedidaGuardado);
        }catch (Exception e){
            log.error("Error al registrar unidad medida: " + e.getMessage());
            throw new ExceptionDatoNoEncontrado("Error al registrar la unidad medida" + e.getMessage());
        }
    }

    public UnidadMedidaDTO updateUnidadMedida(Long id, UnidadMedidaDTO unidadMedida){
        UnidadMedidaEntity unidadMedidaExistente = repo.findById(id).orElseThrow(() -> new ExceptionDatoNoEncontrado("Unidad Medida no encontrada"));

        unidadMedidaExistente.setNombre(unidadMedida.getNombre());
        unidadMedidaExistente.setAbreviatura(unidadMedida.getAbreviatura());

        UnidadMedidaEntity unidadMedidaActualizado = repo.save(unidadMedidaExistente);
        return convertirAUnidadMedidasDTO(unidadMedidaActualizado);
    }

    public boolean deleteUnidadMedida(Long id){
        try{
            UnidadMedidaEntity objUnidadMedida = repo.findById(id).orElse(null);
            if (objUnidadMedida != null){
                repo.deleteById(id);
                return true;
            }else{
                System.out.println("Unidad Medida no encontrado");
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro unidad medida con ID:" + id + " para eliminar.", 1);
        }
    }


    public UnidadMedidaEntity convertirAUnidadMedidasEntity(UnidadMedidaDTO unidadmedida){
        UnidadMedidaEntity dto = new UnidadMedidaEntity();
        dto.setId(unidadmedida.getId());
        dto.setNombre(unidadmedida.getNombre());
        dto.setAbreviatura(unidadmedida.getAbreviatura());
        return dto;
    }

    public UnidadMedidaDTO convertirAUnidadMedidasDTO(UnidadMedidaEntity unidadmedida){
        UnidadMedidaDTO dto = new UnidadMedidaDTO();
        dto.setId(unidadmedida.getId());
        dto.setNombre(unidadmedida.getNombre());
        dto.setAbreviatura(unidadmedida.getAbreviatura());
        return dto;
    }
}
