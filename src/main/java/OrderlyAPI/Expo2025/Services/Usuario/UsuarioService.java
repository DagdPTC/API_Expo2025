package OrderlyAPI.Expo2025.Services.Usuario;

import OrderlyAPI.Expo2025.Entities.Rol.RolEntity;
import OrderlyAPI.Expo2025.Entities.Usuario.UsuarioEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.RolDTO;
import OrderlyAPI.Expo2025.Models.DTO.UsuarioDTO;
import OrderlyAPI.Expo2025.Repositories.Usuario.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UsuarioService {
    private UsuarioRepository repo;

    public List<UsuarioDTO> getAllUsuarios(){
        List<UsuarioEntity> usuarios = repo.findAll();
        return usuarios.stream()
                .map(this::convertirAUsuariosDTO)
                .collect(Collectors.toList());
    }

    public UsuarioDTO createUsuario(UsuarioDTO usuarioDTO){
        if (usuarioDTO == null || usuarioDTO.getNombre() == null || usuarioDTO.getNombre().isEmpty()){
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }
        try{
            UsuarioEntity usuarioEntity = convertirAUsuariosEntity(usuarioDTO);
            UsuarioEntity usuarioGuardado = repo.save(usuarioEntity);
            return convertirAUsuariosDTO(usuarioGuardado);
        }catch (Exception e){
            log.error("Error al registrar rol: " + e.getMessage());
            throw new ExceptionDatoNoEncontrado("Error al registrar el usuario" + e.getMessage());
        }
    }

    public UsuarioDTO updateUsuario(Long id, UsuarioDTO usuario){
        UsuarioEntity usuarioExistente = repo.findById(id).orElseThrow(() -> new ExceptionDatoNoEncontrado("Usuario no encontrado"));

        usuarioExistente.setNombre(usuario.getNombre());

        UsuarioEntity usuarioActualizado = repo.save(usuarioExistente);
        return convertirAUsuariosDTO(usuarioActualizado);
    }

    public boolean deleteUsuario(Long id){
        try{
            UsuarioEntity objUsuario = repo.findById(id).orElse(null);
            if (objUsuario != null){
                repo.deleteById(id);
                return true;
            }else{
                System.out.println("Usuario no encontrado");
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro usuario con ID:" + id + " para eliminar.", 1);
        }
    }

    public UsuarioDTO convertirAUsuariosDTO(UsuarioEntity usuario){
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setContrasenia(usuario.getContrasenia());
        dto.setRolId(usuario.getRolId());
        dto.setCorreo(usuario.getCorreo());
        return dto;
    }

    public UsuarioEntity convertirAUsuariosEntity(UsuarioDTO usuario){
        UsuarioEntity dto = new UsuarioEntity();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setContrasenia(usuario.getContrasenia());
        dto.setRolId(usuario.getRolId());
        dto.setCorreo(usuario.getCorreo());
        return dto;
    }
}
