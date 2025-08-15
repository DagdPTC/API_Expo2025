package OrderlyAPI.Expo2025.Services.Rol;

import OrderlyAPI.Expo2025.Entities.Rol.RolEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.RolDTO;
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

@Slf4j
@Service
@CrossOrigin
public class RolService {

    @Autowired
    private RolRepository repo;


    public Page<RolDTO> getAllRoles(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<RolEntity> roles = repo.findAll(pageable);
        return roles.map(this::convertirARolesDTO);
    }

    public RolDTO createRol(@Valid RolDTO rolDTO){
        if (rolDTO == null){
            throw new IllegalArgumentException("El rol no puede ser nulo");
        }
        try{
            RolEntity rolEntity = convertirARolesEntity(rolDTO);
            RolEntity rolGuardado = repo.save(rolEntity);
            return convertirARolesDTO(rolGuardado);
        }catch (Exception e){
            log.error("Error al registrar rol: " + e.getMessage());
            throw new ExceptionDatoNoEncontrado("Error al registrar el rol" + e.getMessage());
        }
    }

    public RolDTO updateRol(Long id, @Valid RolDTO rol){
        RolEntity rolExistente = repo.findById(id).orElseThrow(() -> new ExceptionDatoNoEncontrado("Rol no encontrado"));

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
                System.out.println("Rol no encontrado");
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro rol con ID:" + id + " para eliminar.", 1);
        }
    }


    public RolEntity convertirARolesEntity(RolDTO dto){
        RolEntity entity = new RolEntity();
        entity.setId(dto.getId());
        entity.setRol(dto.getRol());
        return entity;
    }

    public RolDTO convertirARolesDTO(RolEntity rol){
        RolDTO dto = new RolDTO();
        dto.setId(rol.getId());
        dto.setRol(rol.getRol());
        return dto;
    }
}
