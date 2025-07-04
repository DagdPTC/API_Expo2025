package OrderlyAPI.Expo2025.Services.PrecioPlatillo;

import OrderlyAPI.Expo2025.Entities.PrecioPlatillo.PrecioPlatilloEntity;
import OrderlyAPI.Expo2025.Entities.Rol.RolEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.PrecioPlatilloDTO;
import OrderlyAPI.Expo2025.Models.DTO.RolDTO;
import OrderlyAPI.Expo2025.Repositories.PrecioPlatillo.PrecioPlatilloRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PrecioPlatilloService {
    private PrecioPlatilloRepository repo;


    public List<PrecioPlatilloDTO> getAllPrecioPlatillos(){
        List<PrecioPlatilloEntity> precios = repo.findAll();
        return precios.stream()
                .map(this::convertirAPrecioPlatillosDTO)
                .collect(Collectors.toList());
    }

    public PrecioPlatilloDTO createPrecioPlatillo(PrecioPlatilloDTO precioPlatilloDTO){
        if (precioPlatilloDTO == null || precioPlatilloDTO.getIdPlatillo() == null || precioPlatilloDTO.getIdPlatillo().describeConstable().isEmpty()){
            throw new IllegalArgumentException("El precio platillo no puede ser nulo");
        }
        try{
            PrecioPlatilloEntity precioPlatilloEntity = convertirAPrecioPlatillosEntity(precioPlatilloDTO);
            PrecioPlatilloEntity precioPlatilloGuardado = repo.save(precioPlatilloEntity);
            return convertirAPrecioPlatillosDTO(precioPlatilloGuardado);
        }catch (Exception e){
            log.error("Error al registrar precio platillo: " + e.getMessage());
            throw new ExceptionDatoNoEncontrado("Error al registrar el precio platillo" + e.getMessage());
        }
    }

    public PrecioPlatilloDTO updatePrecioPlatillo(Long id, PrecioPlatilloDTO precioPlatillo){
        PrecioPlatilloEntity precioPlatilloExistente = repo.findById(id).orElseThrow(() -> new ExceptionDatoNoEncontrado("Precio Platillo no encontrado"));

        precioPlatilloExistente.setIdPlatillo(precioPlatillo.getIdPlatillo());
        precioPlatilloExistente.setPrecioUnitario(precioPlatillo.getPrecioUnitario());
        precioPlatilloExistente.setFInicio(precioPlatillo.getFInicio());
        precioPlatilloExistente.setFFin(precioPlatillo.getFFin());

        PrecioPlatilloEntity precioPlatilloActualizado = repo.save(precioPlatilloExistente);
        return convertirAPrecioPlatillosDTO(precioPlatilloActualizado);
    }

    public boolean deletePrecioPlatillo(Long id){
        try{
            PrecioPlatilloEntity objPrecioPlatillo = repo.findById(id).orElse(null);
            if (objPrecioPlatillo != null){
                repo.deleteById(id);
                return true;
            }else{
                System.out.println("Precio Platillo no encontrado");
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro precio platillo con ID:" + id + " para eliminar.", 1);
        }
    }


    public PrecioPlatilloEntity convertirAPrecioPlatillosEntity(PrecioPlatilloDTO precioPlatillo){
        PrecioPlatilloEntity dto = new PrecioPlatilloEntity();
        dto.setId(precioPlatillo.getId());
        dto.setIdPlatillo(precioPlatillo.getIdPlatillo());
        dto.setPrecioUnitario(precioPlatillo.getPrecioUnitario());
        dto.setFInicio(precioPlatillo.getFInicio());
        dto.setFFin(precioPlatillo.getFFin());
        return dto;
    }

    public PrecioPlatilloDTO convertirAPrecioPlatillosDTO(PrecioPlatilloEntity precioPlatillo){
        PrecioPlatilloDTO dto = new PrecioPlatilloDTO();
        dto.setId(precioPlatillo.getId());
        dto.setIdPlatillo(precioPlatillo.getIdPlatillo());
        dto.setPrecioUnitario(precioPlatillo.getPrecioUnitario());
        dto.setFInicio(precioPlatillo.getFInicio());
        dto.setFFin(precioPlatillo.getFFin());
        return dto;
    }
}

