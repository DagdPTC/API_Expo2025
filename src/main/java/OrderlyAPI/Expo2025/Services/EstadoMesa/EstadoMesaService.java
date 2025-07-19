package OrderlyAPI.Expo2025.Services.EstadoMesa;

import OrderlyAPI.Expo2025.Entities.EstadoMesa.EstadoMesaEntity;
import OrderlyAPI.Expo2025.Entities.Rol.RolEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.EstadoMesaDTO;
import OrderlyAPI.Expo2025.Models.DTO.RolDTO;
import OrderlyAPI.Expo2025.Repositories.EstadoMesa.EstadoMesaRepository;
import OrderlyAPI.Expo2025.Repositories.Rol.RolRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EstadoMesaService {

    @Autowired
    private EstadoMesaRepository repo;


    public List<EstadoMesaDTO> getAllEstadoMesas(){
        List<EstadoMesaEntity> estados = repo.findAll();
        return estados.stream()
                .map(this::convertirAEstadosMesasDTO)
                .collect(Collectors.toList());
    }

    public EstadoMesaDTO createEstadoMesa(EstadoMesaDTO estadoMesaDTO){
        if (estadoMesaDTO == null || estadoMesaDTO.getEstadoMesa() == null || estadoMesaDTO.getEstadoMesa().isEmpty()){
            throw new IllegalArgumentException("El estado mesa no puede ser nulo");
        }
        try{
            EstadoMesaEntity estadoMesaEntity = convertirAEstadosMesasEntity(estadoMesaDTO);
            EstadoMesaEntity estadoMesaGuardado = repo.save(estadoMesaEntity);
            return convertirAEstadosMesasDTO(estadoMesaGuardado);
        }catch (Exception e){
            log.error("Error al registrar estado mesa: " + e.getMessage());
            throw new ExceptionDatoNoEncontrado("Error al registrar el estado mesa" + e.getMessage());
        }
    }

    public EstadoMesaDTO updateEstadoMesa(Long id, EstadoMesaDTO estadoMesa){
        EstadoMesaEntity estadoMesaExistente = repo.findById(id).orElseThrow(() -> new ExceptionDatoNoEncontrado("Estado Mesa no encontrado"));

        estadoMesaExistente.setEstadoMesa(estadoMesa.getEstadoMesa());
        estadoMesaExistente.setColorEstadoMesa(estadoMesa.getColorEstadoMesa());

        EstadoMesaEntity estadoMesaActualizado = repo.save(estadoMesaExistente);
        return convertirAEstadosMesasDTO(estadoMesaActualizado);
    }

    public boolean deleteEstadoMesa(Long id){
        try{
            EstadoMesaEntity objEstadoMesa = repo.findById(id).orElse(null);
            if (objEstadoMesa != null){
                repo.deleteById(id);
                return true;
            }else{
                System.out.println("Estado Mesa no encontrado");
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro estado mesa con ID:" + id + " para eliminar.", 1);
        }
    }


    public EstadoMesaEntity convertirAEstadosMesasEntity(EstadoMesaDTO estadoMesa){
        EstadoMesaEntity dto = new EstadoMesaEntity();
        dto.setId(estadoMesa.getId());
        dto.setEstadoMesa(estadoMesa.getEstadoMesa());
        dto.setColorEstadoMesa(estadoMesa.getColorEstadoMesa());
        return dto;
    }

    public EstadoMesaDTO convertirAEstadosMesasDTO(EstadoMesaEntity estadoMesa){
        EstadoMesaDTO dto = new EstadoMesaDTO();
        dto.setId(estadoMesa.getId());
        dto.setEstadoMesa(estadoMesa.getEstadoMesa());
        dto.setColorEstadoMesa(estadoMesa.getColorEstadoMesa());
        return dto;
    }
}
