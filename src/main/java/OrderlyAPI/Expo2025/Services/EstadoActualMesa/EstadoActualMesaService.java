package OrderlyAPI.Expo2025.Services.EstadoActualMesa;

import OrderlyAPI.Expo2025.Entities.EstadoActualMesa.EstadoActualMesaEntity;
import OrderlyAPI.Expo2025.Entities.Rol.RolEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.EstadoActualMesaDTO;
import OrderlyAPI.Expo2025.Models.DTO.EstadoMesaDTO;
import OrderlyAPI.Expo2025.Models.DTO.RolDTO;
import OrderlyAPI.Expo2025.Repositories.EstadoActualMesa.EstadoActualMesaRepository;
import OrderlyAPI.Expo2025.Repositories.Rol.RolRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EstadoActualMesaService {
    private EstadoActualMesaRepository repo;


    public List<EstadoActualMesaDTO> getAllEstadoActualMesas(){
        List<EstadoActualMesaEntity> estados = repo.findAll();
        return estados.stream()
                .map(this::convertirAEstadoActualMesasDTO)
                .collect(Collectors.toList());
    }

    public EstadoActualMesaDTO createEstadoActualMesa(EstadoActualMesaDTO estadoActualMesaDTO){
        if (estadoActualMesaDTO == null || estadoActualMesaDTO.getIdMesa() == null || estadoActualMesaDTO.getIdMesa().describeConstable().isEmpty()){
            throw new IllegalArgumentException("El estado actual mesa no puede ser nulo");
        }
        try{
            EstadoActualMesaEntity estadoActualMesaEntity = convertirAEstadoActualMesasEntity(estadoActualMesaDTO);
            EstadoActualMesaEntity estadoActualMesaGuardado = repo.save(estadoActualMesaEntity);
            return convertirAEstadoActualMesasDTO(estadoActualMesaGuardado);
        }catch (Exception e){
            log.error("Error al registrar estado actual mesa: " + e.getMessage());
            throw new ExceptionDatoNoEncontrado("Error al registrar el estado actual mesa" + e.getMessage());
        }
    }

    public EstadoActualMesaDTO updateEstadoActualMesa(Long id, EstadoActualMesaDTO estadoActualMesa){
        EstadoActualMesaEntity estadoActualMesaExistente = repo.findById(id).orElseThrow(() -> new ExceptionDatoNoEncontrado("Estado Actual Mesa no encontrado"));

        estadoActualMesaExistente.setIdMesa(estadoActualMesa.getIdMesa());
        estadoActualMesaExistente.setIdEstadoMesa(estadoActualMesa.getIdEstadoMesa());
        estadoActualMesaExistente.setFActualizacion(estadoActualMesa.getFActualizacion());

        EstadoActualMesaEntity estadoActualMesaActualizado = repo.save(estadoActualMesaExistente);
        return convertirAEstadoActualMesasDTO(estadoActualMesaActualizado);
    }

    public boolean deleteEstadoActualMesa(Long id){
        try{
            EstadoActualMesaEntity objEstadoActualMesa = repo.findById(id).orElse(null);
            if (objEstadoActualMesa != null){
                repo.deleteById(id);
                return true;
            }else{
                System.out.println("Estado Actual Mesa no encontrado");
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro estado actual mesa con ID:" + id + " para eliminar.", 1);
        }
    }


    public EstadoActualMesaEntity convertirAEstadoActualMesasEntity(EstadoActualMesaDTO estadoActualMesa){
        EstadoActualMesaEntity dto = new EstadoActualMesaEntity();
        dto.setId(estadoActualMesa.getId());
        dto.setIdMesa(estadoActualMesa.getIdMesa());
        dto.setIdEstadoMesa(estadoActualMesa.getIdEstadoMesa());
        dto.setFActualizacion(estadoActualMesa.getFActualizacion());
        return dto;
    }

    public EstadoActualMesaDTO convertirAEstadoActualMesasDTO(EstadoActualMesaEntity estadoActualMesa){
        EstadoActualMesaDTO dto = new EstadoActualMesaDTO();
        dto.setId(estadoActualMesa.getId());
        dto.setIdMesa(estadoActualMesa.getIdMesa());
        dto.setIdEstadoMesa(estadoActualMesa.getIdEstadoMesa());
        dto.setFActualizacion(estadoActualMesa.getFActualizacion());
        return dto;
    }
}
