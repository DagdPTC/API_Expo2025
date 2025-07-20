package OrderlyAPI.Expo2025.Services.Platillo;

import OrderlyAPI.Expo2025.Entities.Platillo.PlatilloEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.PlatilloDTO;
import OrderlyAPI.Expo2025.Repositories.Platillo.PlatilloRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PlatilloService {

    @Autowired
    private PlatilloRepository repo;


    public List<PlatilloDTO> getAllPlatillos(){
        List<PlatilloEntity> platillos = repo.findAll();
        return platillos.stream()
                .map(this::convertirAPlatillosDTO)
                .collect(Collectors.toList());
    }

    public PlatilloDTO createPlatillo(PlatilloDTO platilloDTO){
        if (platilloDTO == null || platilloDTO.getNomPlatillo() == null || platilloDTO.getNomPlatillo().isEmpty()){
            throw new IllegalArgumentException("El nombre del platillo no puede ser nulo");
        }
        try{
            PlatilloEntity platilloEntity = convertirAPlatillosEntity(platilloDTO);
            PlatilloEntity platilloGuardado = repo.save(platilloEntity);
            return convertirAPlatillosDTO(platilloGuardado);
        }catch (Exception e){
            log.error("Error al registrar platillo: " + e.getMessage());
            throw new ExceptionDatoNoEncontrado("Error al registrar el platillo" + e.getMessage());
        }
    }

    public PlatilloDTO updatePlatillo(Long id, PlatilloDTO platillo){
        PlatilloEntity platilloExistente = repo.findById(id).orElseThrow(() -> new ExceptionDatoNoEncontrado("Platillo no encontrado"));

        platilloExistente.setNomPlatillo(platillo.getNomPlatillo());
        platilloExistente.setDescripcion(platillo.getDescripcion());
        platilloExistente.setPrecio(platillo.getPrecio());
        platilloExistente.setTiempoPreparacion(platillo.getTiempoPreparacion());

        PlatilloEntity platilloActualizado = repo.save(platilloExistente);
        return convertirAPlatillosDTO(platilloActualizado);
    }

    public boolean deletePlatillo(Long id){
        try{
            PlatilloEntity objPlatillo = repo.findById(id).orElse(null);
            if (objPlatillo != null){
                repo.deleteById(id);
                return true;
            }else{
                System.out.println("Usuario no encontrado");
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro platillo con ID:" + id + " para eliminar.", 1);
        }
    }


    public PlatilloEntity convertirAPlatillosEntity(PlatilloDTO platillo){
        PlatilloEntity dto = new PlatilloEntity();
        dto.setId(platillo.getId());
        dto.setNomPlatillo(platillo.getNomPlatillo());
        dto.setDescripcion(platillo.getDescripcion());
        dto.setPrecio(platillo.getPrecio());
        dto.setTiempoPreparacion(platillo.getTiempoPreparacion());
        return dto;
    }

    public PlatilloDTO convertirAPlatillosDTO(PlatilloEntity platillo){
        PlatilloDTO dto = new PlatilloDTO();
        dto.setId(platillo.getId());
        dto.setNomPlatillo(platillo.getNomPlatillo());
        dto.setDescripcion(platillo.getDescripcion());
        dto.setPrecio(platillo.getPrecio());
        dto.setTiempoPreparacion(platillo.getTiempoPreparacion());
        return dto;
    }
}
