package OrderlyAPI.Expo2025.Services.Reserva;

import OrderlyAPI.Expo2025.Entities.Reserva.ReservaEntity;
import OrderlyAPI.Expo2025.Entities.Rol.RolEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.ReservaDTO;
import OrderlyAPI.Expo2025.Models.DTO.RolDTO;
import OrderlyAPI.Expo2025.Repositories.Reserva.ReservaRepository;
import OrderlyAPI.Expo2025.Repositories.Rol.RolRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReservaService {

    @Autowired
    private ReservaRepository repo;


    public List<ReservaDTO> getAllReservas(){
        List<ReservaEntity> reservas = repo.findAll();
        return reservas.stream()
                .map(this::convertirAReservasDTO)
                .collect(Collectors.toList());
    }

    public ReservaDTO createReserva(ReservaDTO reservaDTO){
        if (reservaDTO == null || reservaDTO.getNomCliente() == null || reservaDTO.getNomCliente().isEmpty()){
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

        reservaExistente.setNomCliente(reserva.getNomCliente());
        reservaExistente.setIdMesa(reserva.getIdMesa());
        reservaExistente.setFReserva(reserva.getFReserva());
        reservaExistente.setHora(reserva.getHora());
        reservaExistente.setCantidadPersonas(reserva.getCantidadPersonas());
        reservaExistente.setIdTipoReserva(reserva.getIdTipoReserva());
        reservaExistente.setIdEstadoReserva(reserva.getIdEstadoReserva());

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
        dto.setNomCliente(reserva.getNomCliente());
        dto.setIdMesa(reserva.getIdMesa());
        dto.setFReserva(reserva.getFReserva());
        dto.setHora(reserva.getHora());
        dto.setCantidadPersonas(reserva.getCantidadPersonas());
        dto.setIdTipoReserva(reserva.getIdTipoReserva());
        dto.setIdEstadoReserva(reserva.getIdEstadoReserva());
        return dto;
    }

    public ReservaDTO convertirAReservasDTO(ReservaEntity reserva){
        ReservaDTO dto = new ReservaDTO();
        dto.setId(reserva.getId());
        dto.setNomCliente(reserva.getNomCliente());
        dto.setIdMesa(reserva.getIdMesa());
        dto.setFReserva(reserva.getFReserva());
        dto.setHora(reserva.getHora());
        dto.setCantidadPersonas(reserva.getCantidadPersonas());
        dto.setIdTipoReserva(reserva.getIdTipoReserva());
        dto.setIdEstadoReserva(reserva.getIdEstadoReserva());
        return dto;
    }
}
