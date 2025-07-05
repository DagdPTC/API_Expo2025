package OrderlyAPI.Expo2025.Services.Reserva;

import OrderlyAPI.Expo2025.Entities.Reserva.ReservaEntity;
import OrderlyAPI.Expo2025.Entities.Rol.RolEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.ReservaDTO;
import OrderlyAPI.Expo2025.Models.DTO.RolDTO;
import OrderlyAPI.Expo2025.Repositories.Reserva.ReservaRepository;
import OrderlyAPI.Expo2025.Repositories.Rol.RolRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReservaService {
    private ReservaRepository repo;


    public List<ReservaDTO> getAllReservas(){
        List<ReservaEntity> reservas = repo.findAll();
        return reservas.stream()
                .map(this::convertirAReservasDTO)
                .collect(Collectors.toList());
    }

    public ReservaDTO createReserva(ReservaDTO reservaDTO){
        if (reservaDTO == null || reservaDTO.getIdCliente() == null || reservaDTO.getIdCliente().describeConstable().isEmpty()){
            throw new IllegalArgumentException("La reserva no puede ser nulo");
        }
        try{
            ReservaEntity reservaEntity = convertirAReservasEntity(reservaDTO);
            ReservaEntity reservaGuardado = repo.save(reservaEntity);
            return convertirAReservasDTO(reservaGuardado);
        }catch (Exception e){
            log.error("Error al registrar reserva: " + e.getMessage());
            throw new ExceptionDatoNoEncontrado("Error al registrar la reserva" + e.getMessage());
        }
    }

    public ReservaDTO updateReserva(Long id, ReservaDTO reserva){
        ReservaEntity reservaExistente = repo.findById(id).orElseThrow(() -> new ExceptionDatoNoEncontrado("Reserva no encontrado"));

        reservaExistente.setIdCliente(reserva.getIdCliente());
        reservaExistente.setIdMesa(reserva.getIdMesa());
        reservaExistente.setFReserva(reserva.getFReserva());
        reservaExistente.setHInicio(reserva.getHFin());
        reservaExistente.setCantidadPersonas(reserva.getCantidadPersonas());
        reservaExistente.setIdTipoReserva(reserva.getIdTipoReserva());
        reservaExistente.setIdEstadoReserva(reserva.getIdEstadoReserva());
        reservaExistente.setObservaciones(reserva.getObservaciones());

        ReservaEntity reservaActualizado = repo.save(reservaExistente);
        return convertirAReservasDTO(reservaActualizado);
    }

    public boolean deleteReserva(Long id){
        try{
            ReservaEntity objReserva = repo.findById(id).orElse(null);
            if (objReserva != null){
                repo.deleteById(id);
                return true;
            }else{
                System.out.println("Reserva no encontrado");
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro reserva con ID:" + id + " para eliminar.", 1);
        }
    }


    public ReservaEntity convertirAReservasEntity(ReservaDTO reserva){
        ReservaEntity dto = new ReservaEntity();
        dto.setId(reserva.getId());
        dto.setIdCliente(reserva.getIdCliente());
        dto.setIdMesa(reserva.getIdMesa());
        dto.setFReserva(reserva.getFReserva());
        dto.setHInicio(reserva.getHInicio());
        dto.setHFin(reserva.getHFin());
        dto.setCantidadPersonas(reserva.getCantidadPersonas());
        dto.setIdTipoReserva(reserva.getIdTipoReserva());
        dto.setIdEstadoReserva(reserva.getIdEstadoReserva());
        dto.setObservaciones(reserva.getObservaciones());
        return dto;
    }

    public ReservaDTO convertirAReservasDTO(ReservaEntity reserva){
        ReservaDTO dto = new ReservaDTO();
        dto.setId(reserva.getId());
        dto.setIdCliente(reserva.getIdCliente());
        dto.setIdMesa(reserva.getIdMesa());
        dto.setFReserva(reserva.getFReserva());
        dto.setHInicio(reserva.getHInicio());
        dto.setHFin(reserva.getHFin());
        dto.setCantidadPersonas(reserva.getCantidadPersonas());
        dto.setIdTipoReserva(reserva.getIdTipoReserva());
        dto.setIdEstadoReserva(reserva.getIdEstadoReserva());
        dto.setObservaciones(reserva.getObservaciones());
        return dto;
    }
}
