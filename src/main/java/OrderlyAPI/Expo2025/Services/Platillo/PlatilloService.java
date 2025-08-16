package OrderlyAPI.Expo2025.Services.Platillo;

import OrderlyAPI.Expo2025.Entities.Platillo.PlatilloEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.PlatilloDTO;
import OrderlyAPI.Expo2025.Repositories.Platillo.PlatilloRepository;
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

@Slf4j
@Service
@CrossOrigin
public class PlatilloService {

    @Autowired
    private PlatilloRepository repo;


    public Page<PlatilloDTO> getAllPlatillos(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<PlatilloEntity> platillos = repo.findAll(pageable);
        return platillos.map(this::convertirAPlatillosDTO);
    }

    public PlatilloDTO createPlatillo(@Valid PlatilloDTO platilloDTO){
        if (platilloDTO == null){
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

    public PlatilloDTO updatePlatillo(Long id, @Valid PlatilloDTO platillo){
        PlatilloEntity platilloExistente = repo.findById(id).orElseThrow(() -> new ExceptionDatoNoEncontrado("Platillo no encontrado"));

        platilloExistente.setNomPlatillo(platillo.getNomPlatillo());
        platilloExistente.setDescripcion(platillo.getDescripcion());
        platilloExistente.setPrecio(platillo.getPrecio());
        platilloExistente.setIdCategoria(platillo.getIdCategoria());

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
        dto.setIdCategoria(platillo.getIdCategoria());
        return dto;
    }

    public PlatilloDTO convertirAPlatillosDTO(PlatilloEntity platillo){
        PlatilloDTO dto = new PlatilloDTO();
        dto.setId(platillo.getId());
        dto.setNomPlatillo(platillo.getNomPlatillo());
        dto.setDescripcion(platillo.getDescripcion());
        dto.setPrecio(platillo.getPrecio());
        dto.setIdCategoria(platillo.getIdCategoria());
        return dto;
    }
}
