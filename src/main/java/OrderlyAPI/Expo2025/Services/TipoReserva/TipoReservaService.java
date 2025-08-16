package OrderlyAPI.Expo2025.Services.TipoReserva;

import OrderlyAPI.Expo2025.Entities.Rol.RolEntity;
import OrderlyAPI.Expo2025.Entities.TipoReserva.TipoReservaEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.RolDTO;
import OrderlyAPI.Expo2025.Models.DTO.TipoReservaDTO;
import OrderlyAPI.Expo2025.Repositories.Rol.RolRepository;
import OrderlyAPI.Expo2025.Repositories.TipoReserva.TipoReservaRepository;
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
public class TipoReservaService {

    @Autowired
    private TipoReservaRepository repo;

    public Page<TipoReservaDTO> getAllTipoReservas(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<TipoReservaEntity> reservas = repo.findAll(pageable);
        return reservas.map(this::convertirATipoReservasDTO);
    }

    public TipoReservaDTO createTipoReserva(@Valid TipoReservaDTO tipoReservaDTO){
        if (tipoReservaDTO == null){
            throw new IllegalArgumentException("El tipo reserva no puede ser nulo");
        }
        try{
            TipoReservaEntity tipoReservaEntity = convertirATipoReservasEntity(tipoReservaDTO);
            TipoReservaEntity tipoReservaGuardado = repo.save(tipoReservaEntity);
            return convertirATipoReservasDTO(tipoReservaGuardado);
        }catch (Exception e){
            log.error("Error al registrar tipo reserva: " + e.getMessage());
            throw new ExceptionDatoNoEncontrado("Error al registrar el tipo reserva" + e.getMessage());
        }
    }

    public TipoReservaDTO updateTipoReserva(Long id, @Valid TipoReservaDTO tipoReserva){
        TipoReservaEntity tipoReservaExistente = repo.findById(id).orElseThrow(() -> new ExceptionDatoNoEncontrado("Tipo Reserva no encontrado"));

        tipoReservaExistente.setNomTipo(tipoReserva.getNomTipo());

        TipoReservaEntity tipoReservaActualizado = repo.save(tipoReservaExistente);
        return convertirATipoReservasDTO(tipoReservaActualizado);
    }

    public boolean deleteTipoReserva(Long id){
        try{
            TipoReservaEntity objTipoReserva = repo.findById(id).orElse(null);
            if (objTipoReserva != null){
                repo.deleteById(id);
                return true;
            }else{
                System.out.println("Tipo Reserva no encontrado");
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro tipo reserva con ID:" + id + " para eliminar.", 1);
        }
    }


    public TipoReservaEntity convertirATipoReservasEntity(TipoReservaDTO tipoReserva){
        TipoReservaEntity dto = new TipoReservaEntity();
        dto.setId(tipoReserva.getId());
        dto.setNomTipo(tipoReserva.getNomTipo());
        return dto;
    }

    public TipoReservaDTO convertirATipoReservasDTO(TipoReservaEntity tipoReserva){
        TipoReservaDTO dto = new TipoReservaDTO();
        dto.setId(tipoReserva.getId());
        dto.setNomTipo(tipoReserva.getNomTipo());
        return dto;
    }
}
