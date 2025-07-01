package OrderlyAPI.Expo2025.Services.Rol;

import OrderlyAPI.Expo2025.Entities.Rol.RolEntity;
import OrderlyAPI.Expo2025.Entities.Usuario.UsuarioEntity;
import OrderlyAPI.Expo2025.Models.DTO.RolDTO;
import OrderlyAPI.Expo2025.Repositories.Rol.RolRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RolService {
    private RolRepository repo;


    public List<RolDTO> getAllRoles(){
        List<RolEntity> roles = repo.findAll();
        return roles.stream()
                .map(this::convertirARolesDTO)
                .collect(Collectors.toList());
    }

    public RolDTO createUser(RolDTO rolDTO){
        if (rolDTO == null || rolDTO.getRol() == null || rolDTO.getRol().isEmpty()){
            throw new IllegalArgumentException("El rol no puede ser nulo");
        }
        try{
            RolEntity rolEntity = convertirARolesEntity(rolDTO);
            RolEntity rolGuardado = repo.save(rolEntity);
            return convertirARolesDTO(rolGuardado);
        }catch (Exception e){
            log.error("Error al registrar rol: " + e.getMessage());
            throw new
        }
    }




    public RolEntity convertirARolesEntity(RolDTO rol){
        RolEntity dto = new RolEntity();
        dto.setId(rol.getId());
        dto.setRol(rol.getRol());
        return dto;
    }

    public RolDTO convertirARolesDTO(RolEntity rol){
        RolDTO dto = new RolDTO();
        dto.setId(rol.getId());
        dto.setRol(rol.getRol());
        return dto;
    }



}
