package OrderlyAPI.Expo2025.Services.Reserva;

import OrderlyAPI.Expo2025.Entities.Reserva.ReservaEntity;
import OrderlyAPI.Expo2025.Entities.Rol.RolEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.ReservaDTO;
import OrderlyAPI.Expo2025.Models.DTO.RolDTO;
import OrderlyAPI.Expo2025.Repositories.Reserva.ReservaRepository;
import OrderlyAPI.Expo2025.Repositories.Rol.RolRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@CrossOrigin
public class ReservaService {

    @Autowired
    private ReservaRepository repo;

    public Page<ReservaDTO> getAllReservas(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<ReservaEntity> reservas = repo.findAll(pageable);
        return reservas.map(this::convertirAReservasDTO);
    }

    public ReservaDTO createReserva(@Valid ReservaDTO reservaDTO){
        if (reservaDTO == null){
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

    public ReservaDTO updateReserva(Long id, @Valid ReservaDTO reserva){
        ReservaEntity reservaExistente = repo.findById(id).orElseThrow(() -> new ExceptionDatoNoEncontrado("Reserva no encontrado"));

        reservaExistente.setNomCliente(reserva.getNomCliente());
        reservaExistente.setTelefono(reserva.getTelefono());
        reservaExistente.setIdMesa(reserva.getIdMesa());
        reservaExistente.setFReserva(reserva.getFReserva());
        reservaExistente.setHoraI(reserva.getHoraI());
        reservaExistente.setHoraF(reserva.getHoraF());
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
        dto.setTelefono(reserva.getTelefono());
        dto.setIdMesa(reserva.getIdMesa());
        dto.setFReserva(reserva.getFReserva());
        dto.setHoraI(reserva.getHoraI());
        dto.setHoraF(reserva.getHoraF());
        dto.setCantidadPersonas(reserva.getCantidadPersonas());
        dto.setIdTipoReserva(reserva.getIdTipoReserva());
        dto.setIdEstadoReserva(reserva.getIdEstadoReserva());
        return dto;
    }

    public ReservaDTO convertirAReservasDTO(ReservaEntity reserva){
        ReservaDTO dto = new ReservaDTO();
        dto.setId(reserva.getId());
        dto.setNomCliente(reserva.getNomCliente());
        dto.setTelefono(reserva.getTelefono());
        dto.setIdMesa(reserva.getIdMesa());
        dto.setFReserva(reserva.getFReserva());
        dto.setHoraI(reserva.getHoraI());
        dto.setHoraF(reserva.getHoraF());
        dto.setCantidadPersonas(reserva.getCantidadPersonas());
        dto.setIdTipoReserva(reserva.getIdTipoReserva());
        dto.setIdEstadoReserva(reserva.getIdEstadoReserva());
        return dto;
    }
}
