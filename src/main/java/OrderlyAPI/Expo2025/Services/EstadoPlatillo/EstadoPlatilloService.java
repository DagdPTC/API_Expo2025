package OrderlyAPI.Expo2025.Services.EstadoPlatillo;

import OrderlyAPI.Expo2025.Entities.EstadoPlatillo.EstadoPlatilloEntity;
import OrderlyAPI.Expo2025.Entities.Rol.RolEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.EstadoPlatilloDTO;
import OrderlyAPI.Expo2025.Models.DTO.RolDTO;
import OrderlyAPI.Expo2025.Repositories.EstadoPlatillo.EstadoPlatilloRepository;
import OrderlyAPI.Expo2025.Repositories.Rol.RolRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EstadoPlatilloService {

    @Autowired
    private EstadoPlatilloRepository repo;


    public List<EstadoPlatilloDTO> getAllEstadoPlatillos(){
        List<EstadoPlatilloEntity> estados = repo.findAll();
        return estados.stream()
                .map(this::convertirAEstadoPlatillosDTO)
                .collect(Collectors.toList());
    }

    public EstadoPlatilloDTO createEstadoPlatillo(EstadoPlatilloDTO estadoPlatilloDTO){
        if (estadoPlatilloDTO == null || estadoPlatilloDTO.getNomEstado() == null || estadoPlatilloDTO.getNomEstado().isEmpty()){
            throw new IllegalArgumentException("El estado platillo no puede ser nulo");
        }
        try{
            EstadoPlatilloEntity estadoPlatilloEntity = convertirAEstadoPlatillosEntity(estadoPlatilloDTO);
            EstadoPlatilloEntity estadoPlatilloGuardado = repo.save(estadoPlatilloEntity);
            return convertirAEstadoPlatillosDTO(estadoPlatilloGuardado);
        }catch (Exception e){
            log.error("Error al registrar estado platillo: " + e.getMessage());
            throw new ExceptionDatoNoEncontrado("Error al registrar el estado platillo" + e.getMessage());
        }
    }

    public EstadoPlatilloDTO updateEstadoPlatillo(Long id, EstadoPlatilloDTO estadoPlatillo){
        EstadoPlatilloEntity estadoPlatilloExistente = repo.findById(id).orElseThrow(() -> new ExceptionDatoNoEncontrado("Estado Platillo no encontrado"));

        estadoPlatilloExistente.setNomEstado(estadoPlatillo.getNomEstado());

        EstadoPlatilloEntity estadoPlatilloActualizado = repo.save(estadoPlatilloExistente);
        return convertirAEstadoPlatillosDTO(estadoPlatilloActualizado);
    }

    public boolean deleteEstadoPlatillo(Long id){
        try{
            EstadoPlatilloEntity objEstadoPlatillo = repo.findById(id).orElse(null);
            if (objEstadoPlatillo != null){
                repo.deleteById(id);
                return true;
            }else{
                System.out.println("Estado Platillo no encontrado");
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro estado platillo con ID:" + id + " para eliminar.", 1);
        }
    }


    public EstadoPlatilloEntity convertirAEstadoPlatillosEntity(EstadoPlatilloDTO estadoPlatillo){
        EstadoPlatilloEntity dto = new EstadoPlatilloEntity();
        dto.setId(estadoPlatillo.getId());
        dto.setNomEstado(estadoPlatillo.getNomEstado());
        return dto;
    }

    public EstadoPlatilloDTO convertirAEstadoPlatillosDTO(EstadoPlatilloEntity estadoPlatillo){
        EstadoPlatilloDTO dto = new EstadoPlatilloDTO();
        dto.setId(estadoPlatillo.getId());
        dto.setNomEstado(estadoPlatillo.getNomEstado());
        return dto;
    }
}
