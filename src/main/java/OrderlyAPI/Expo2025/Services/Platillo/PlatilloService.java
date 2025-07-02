package OrderlyAPI.Expo2025.Services.Platillo;

import OrderlyAPI.Expo2025.Entities.Platillo.PlatilloEntity;
import OrderlyAPI.Expo2025.Entities.Rol.RolEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionRolNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.PlatilloDTO;
import OrderlyAPI.Expo2025.Models.DTO.RolDTO;
import OrderlyAPI.Expo2025.Repositories.Platillo.PlatilloRepository;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
import java.util.stream.Collectors;

public class PlatilloService {
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
            log.error("Error al registrar rol: " + e.getMessage());
            throw new ExceptionRolNoEncontrado("Error al registrar el rol" + e.getMessage());
        }
    }

    public RolDTO updateRol(Long id, RolDTO rol){
        RolEntity rolExistente = repo.findById(id).orElseThrow(() -> new ExceptionRolNoEncontrado("Rol no encontrado"));

        rolExistente.setRol(rol.getRol());

        RolEntity rolActualizado = repo.save(rolExistente);
        return convertirARolesDTO(rolActualizado);
    }

    public boolean deleteRol(Long id){
        try{
            RolEntity objRol = repo.findById(id).orElse(null);
            if (objRol != null){
                repo.deleteById(id);
                return true;
            }else{
                System.out.println("Usuario no encontrado");
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro rol con ID:" + id + " para eliminar.", 1);
        }
    }


    public PlatilloEntity convertirAPlatillosEntity(PlatilloDTO platillo){
        PlatilloEntity dto = new PlatilloEntity();
        dto.setId(platillo.getId());
        dto.setNomPlatillo(platillo.getNomPlatillo());
        dto.setDescripcion(platillo.getDescripcion());
        dto.setTiempoPreparacion(platillo.getTiempoPreparacion());
        return dto;
    }

    public PlatilloDTO convertirAPlatillosDTO(PlatilloEntity platillo){
        PlatilloDTO dto = new PlatilloDTO();
        dto.setId(platillo.getId());
        dto.setNomPlatillo(platillo.getNomPlatillo());
        dto.setDescripcion(platillo.getDescripcion());
        dto.setTiempoPreparacion(platillo.getTiempoPreparacion());
        return dto;
    }
}
