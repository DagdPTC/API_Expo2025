package OrderlyAPI.Expo2025.Services.EstadoReserva;

import OrderlyAPI.Expo2025.Entities.EstadoMesa.EstadoMesaEntity;
import OrderlyAPI.Expo2025.Entities.EstadoReserva.EstadoReservaEntity;
import OrderlyAPI.Expo2025.Entities.Rol.RolEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.EstadoReservaDTO;
import OrderlyAPI.Expo2025.Models.DTO.RolDTO;
import OrderlyAPI.Expo2025.Repositories.EstadoReserva.EstadoReservaRepository;
import OrderlyAPI.Expo2025.Repositories.Rol.RolRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EstadoReservaService {
    private EstadoReservaRepository repo;


    public List<EstadoReservaDTO> getAllEstadoReservas(){
        List<EstadoReservaEntity> estados = repo.findAll();
        return estados.stream()
                .map(this::convertirAEstadoReservasDTO)
                .collect(Collectors.toList());
    }

    public EstadoReservaDTO createEstadoReserva(EstadoReservaDTO estadoReservaDTO){
        if (estadoReservaDTO == null || estadoReservaDTO.getNomEstado() == null || estadoReservaDTO.getNomEstado().isEmpty()){
            throw new IllegalArgumentException("El estado reserva no puede ser nulo");
        }
        try{
            EstadoReservaEntity estadoReservaEntity = convertirAEstadoReservasEntity(estadoReservaDTO);
            EstadoReservaEntity estadoReservaGuardado = repo.save(estadoReservaEntity);
            return convertirAEstadoReservasDTO(estadoReservaGuardado);
        }catch (Exception e){
            log.error("Error al registrar estado reserva: " + e.getMessage());
            throw new ExceptionDatoNoEncontrado("Error al registrar el estado reserva" + e.getMessage());
        }
    }

    public EstadoReservaDTO updateEstadoReserva(Long id, EstadoReservaDTO estadoReserva){
        EstadoReservaEntity estadoReservaExistente = repo.findById(id).orElseThrow(() -> new ExceptionDatoNoEncontrado("Estado Reserva no encontrado"));

        estadoReservaExistente.setNomEstado(estadoReserva.getNomEstado());

        EstadoReservaEntity estadoReservaActualizado = repo.save(estadoReservaExistente);
        return convertirAEstadoReservasDTO(estadoReservaActualizado);
    }

    public boolean deleteEstadoReserva(Long id){
        try{
            EstadoReservaEntity objEstadoReserva = repo.findById(id).orElse(null);
            if (objEstadoReserva != null){
                repo.deleteById(id);
                return true;
            }else{
                System.out.println("Estado Reserva no encontrado");
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro estado reserva con ID:" + id + " para eliminar.", 1);
        }
    }


    public EstadoReservaEntity convertirAEstadoReservasEntity(EstadoReservaDTO estadoReserva){
        EstadoReservaEntity dto = new EstadoReservaEntity();
        dto.setId(estadoReserva.getId());
        dto.setNomEstado(estadoReserva.getNomEstado());
        return dto;
    }

    public EstadoReservaDTO convertirAEstadoReservasDTO(EstadoReservaEntity estadoReserva){
        EstadoReservaDTO dto = new EstadoReservaDTO();
        dto.setId(estadoReserva.getId());
        dto.setNomEstado(estadoReserva.getNomEstado());
        return dto;
    }
}
