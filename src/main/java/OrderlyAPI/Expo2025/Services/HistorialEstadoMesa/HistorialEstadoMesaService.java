package OrderlyAPI.Expo2025.Services.HistorialEstadoMesa;

import OrderlyAPI.Expo2025.Entities.HistorialEstadoMesa.HistorialEstadoMesaEntity;
import OrderlyAPI.Expo2025.Entities.Rol.RolEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.HistorialEstadoMesaDTO;
import OrderlyAPI.Expo2025.Models.DTO.RolDTO;
import OrderlyAPI.Expo2025.Repositories.HistorialEstadoMesa.HistorialEstadoMesaRepository;
import OrderlyAPI.Expo2025.Repositories.Rol.RolRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HistorialEstadoMesaService {
    private HistorialEstadoMesaRepository repo;


    public List<HistorialEstadoMesaDTO> getAllHistorialEstadoMesas(){
        List<HistorialEstadoMesaEntity> historial = repo.findAll();
        return historial.stream()
                .map(this::convertirAHistorialEstadoMesasDTO)
                .collect(Collectors.toList());
    }

    public HistorialEstadoMesaDTO createHistorialEstadoMesa(HistorialEstadoMesaDTO historialEstadoMesaDTO){
        if (historialEstadoMesaDTO == null || historialEstadoMesaDTO.getIdEstadoMesa() == null || historialEstadoMesaDTO.getIdEstadoMesa().describeConstable().isEmpty()){
            throw new IllegalArgumentException("El historial estado mesa no puede ser nulo");
        }
        try{
            HistorialEstadoMesaEntity historialEstadoMesaEntity = convertirAHistorialEstadoMesasEntity(historialEstadoMesaDTO);
            HistorialEstadoMesaEntity historialEstadoMesaGuardado = repo.save(historialEstadoMesaEntity);
            return convertirAHistorialEstadoMesasDTO(historialEstadoMesaGuardado);
        }catch (Exception e){
            log.error("Error al registrar historial estado mesa: " + e.getMessage());
            throw new ExceptionDatoNoEncontrado("Error al registrar el historial estado mesa" + e.getMessage());
        }
    }

    public HistorialEstadoMesaDTO updateHistorialEstadoMesa(Long id, HistorialEstadoMesaDTO historialEstadoMesa){
        HistorialEstadoMesaEntity historialEstadoMesaExistente = repo.findById(id).orElseThrow(() -> new ExceptionDatoNoEncontrado("Historial Estado Mesa no encontrado"));

        historialEstadoMesaExistente.setIdMesa(historialEstadoMesa.getIdMesa());
        historialEstadoMesaExistente.setIdEstadoMesa(historialEstadoMesa.getIdEstadoMesa());
        historialEstadoMesaExistente.setFInicio(historialEstadoMesa.getFInicio());
        historialEstadoMesaExistente.setFFin(historialEstadoMesa.getFFin());
        historialEstadoMesaExistente.setIdEmpleado(historialEstadoMesa.getIdEmpleado());

        HistorialEstadoMesaEntity historialEstadoMesaActualizado = repo.save(historialEstadoMesaExistente);
        return convertirAHistorialEstadoMesasDTO(historialEstadoMesaActualizado);
    }

    public boolean deleteHistorialEstadoMesa(Long id){
        try{
            HistorialEstadoMesaEntity objHistorialEstadoMesa = repo.findById(id).orElse(null);
            if (objHistorialEstadoMesa != null){
                repo.deleteById(id);
                return true;
            }else{
                System.out.println("Historial Estado Mesa no encontrado");
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro historial estado mesa con ID:" + id + " para eliminar.", 1);
        }
    }


    public HistorialEstadoMesaEntity convertirAHistorialEstadoMesasEntity(HistorialEstadoMesaDTO historialEstadoMesa){
        HistorialEstadoMesaEntity dto = new HistorialEstadoMesaEntity();
        dto.setId(historialEstadoMesa.getId());
        dto.setIdMesa(historialEstadoMesa.getIdMesa());
        dto.setIdEstadoMesa(historialEstadoMesa.getIdEstadoMesa());
        dto.setFInicio(historialEstadoMesa.getFInicio());
        dto.setFFin(historialEstadoMesa.getFFin());
        dto.setIdEmpleado(historialEstadoMesa.getIdEmpleado());
        return dto;
    }

    public HistorialEstadoMesaDTO convertirAHistorialEstadoMesasDTO(HistorialEstadoMesaEntity historialEstadoMesa){
        HistorialEstadoMesaDTO dto = new HistorialEstadoMesaDTO();
        dto.setId(historialEstadoMesa.getId());
        dto.setIdMesa(historialEstadoMesa.getIdMesa());
        dto.setFInicio(historialEstadoMesa.getFInicio());
        dto.setFFin(historialEstadoMesa.getFFin());
        dto.setIdEmpleado(historialEstadoMesa.getIdEmpleado());
        return dto;
    }
}
