package OrderlyAPI.Expo2025.Services.Usuario;

import OrderlyAPI.Expo2025.Entities.Rol.RolEntity;
import OrderlyAPI.Expo2025.Entities.TipoDocumento.TipoDocumentoEntity;
import OrderlyAPI.Expo2025.Entities.Usuario.UsuarioEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.RolDTO;
import OrderlyAPI.Expo2025.Models.DTO.UsuarioDTO;
import OrderlyAPI.Expo2025.Repositories.Usuario.UsuarioRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
public class UsuarioService {

    @Autowired
    private UsuarioRepository repo;

    @PersistenceContext
    EntityManager entityManager;

    public Page<UsuarioDTO> getAllUsuarios(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<UsuarioEntity> usuarios = repo.findAll(pageable);
        return usuarios.map(this::convertirAUsuariosDTO);
    }

    public UsuarioDTO createUsuario( @Valid UsuarioDTO usuarioDTO){
        if (usuarioDTO == null) {
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

    public UsuarioDTO updateUsuario( @Valid Long id, UsuarioDTO usuario){
        UsuarioEntity usuarioExistente = repo.findById(id).orElseThrow(() -> new ExceptionDatoNoEncontrado("Usuario no encontrado"));

        usuarioExistente.setContrasenia(usuario.getContrasenia());
        usuarioExistente.setUrol(usuarioExistente.getUrol());
        usuarioExistente.setCorreo(usuario.getCorreo());

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
        dto.setContrasenia(usuario.getContrasenia());
        dto.setRolId(usuario.getUrol().getId());
        dto.setCorreo(usuario.getCorreo());
        return dto;
    }

    public UsuarioEntity convertirAUsuariosEntity(UsuarioDTO usuario){
        UsuarioEntity dto = new UsuarioEntity();
        dto.setId(usuario.getId());
        dto.setContrasenia(usuario.getContrasenia());
        dto.setUrol(entityManager.getReference(RolEntity.class, usuario.getRolId()));
        dto.setCorreo(usuario.getCorreo());
        return dto;
    }
}
