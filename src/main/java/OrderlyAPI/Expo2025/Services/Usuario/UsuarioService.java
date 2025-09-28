package OrderlyAPI.Expo2025.Services.Usuario;

import OrderlyAPI.Expo2025.Config.Argon2.Argon2Password;
import OrderlyAPI.Expo2025.Entities.Rol.RolEntity;
import OrderlyAPI.Expo2025.Entities.Usuario.UsuarioEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.UsuarioDTO;
import OrderlyAPI.Expo2025.Repositories.Rol.RolRepository;
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

@Service
@Slf4j
@CrossOrigin
public class UsuarioService {

    @Autowired
    private UsuarioRepository repo;

    @Autowired
    private RolRepository rolRepository;

    // Usamos tu servicio Argon2 propio
    @Autowired
    private Argon2Password argon2;

    @PersistenceContext
    EntityManager entityManager;

    public Page<UsuarioDTO> getAllUsuarios(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<UsuarioEntity> usuarios = repo.findAll(pageable);
        return usuarios.map(this::convertirAUsuariosDTO);
    }

    public UsuarioDTO createUsuario(@Valid UsuarioDTO usuarioDTO) {
        if (usuarioDTO == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }
        try {
            UsuarioEntity usuarioEntity = new UsuarioEntity();

            // ID opcional
            if (usuarioDTO.getId() != null) {
                usuarioEntity.setId(usuarioDTO.getId());
            }

            // Campos simples
            usuarioEntity.setNombreusuario(usuarioDTO.getNombreusuario());
            usuarioEntity.setCorreo(usuarioDTO.getCorreo());

            // Hash Argon2 (usando tu servicio)
            String hashed = argon2.EncryptPassword(usuarioDTO.getContrasenia());
            usuarioEntity.setContrasenia(hashed);

            // Rol (si viene)
            if (usuarioDTO.getRolId() != null) {
                RolEntity rol = rolRepository.findById(usuarioDTO.getRolId())
                        .orElseThrow(() -> new ExceptionDatoNoEncontrado("Rol no encontrado"));
                usuarioEntity.setRol(rol);
            }

            UsuarioEntity guardado = repo.save(usuarioEntity);
            return convertirAUsuariosDTO(guardado);

        } catch (Exception e) {
            log.error("Error al registrar usuario: {}", e.getMessage(), e);
            throw new ExceptionDatoNoEncontrado("Error al registrar el usuario " + e.getMessage());
        }
    }

    public UsuarioDTO updateUsuario(Long id, @Valid UsuarioDTO usuario) {
        UsuarioEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionDatoNoEncontrado("Usuario no encontrado"));

        // Actualiza si vienen
        if (usuario.getNombreusuario() != null && !usuario.getNombreusuario().isBlank()) {
            existente.setNombreusuario(usuario.getNombreusuario());
        }
        if (usuario.getCorreo() != null && !usuario.getCorreo().isBlank()) {
            existente.setCorreo(usuario.getCorreo());
        }
        if (usuario.getContrasenia() != null && !usuario.getContrasenia().isBlank()) {
            String hashed = argon2.EncryptPassword(usuario.getContrasenia());
            existente.setContrasenia(hashed);
        }
        if (usuario.getRolId() != null) {
            RolEntity rol = rolRepository.findById(usuario.getRolId())
                    .orElseThrow(() -> new ExceptionDatoNoEncontrado("Rol no encontrado"));
            existente.setRol(rol);
        }

        UsuarioEntity actualizado = repo.save(existente);
        return convertirAUsuariosDTO(actualizado);
    }

    public boolean deleteUsuario(Long id){
        try{
            UsuarioEntity obj = repo.findById(id).orElse(null);
            if (obj != null){
                repo.deleteById(id);
                return true;
            } else {
                log.warn("Usuario no encontrado para eliminar. ID: {}", id);
                return false;
            }
        } catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException(
                    "No se encontró usuario con ID:" + id + " para eliminar.", 1);
        }
    }

    // === Mappers ===

    private UsuarioDTO convertirAUsuariosDTO(UsuarioEntity usuario){
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNombreusuario(usuario.getNombreusuario());
        dto.setCorreo(usuario.getCorreo());
        // Por seguridad NO devolvemos la contraseña. (Si tu front lo exige, quítalo del DTO)
        dto.setContrasenia(null);
        if (usuario.getRol() != null) {
            dto.setRolId(usuario.getRol().getId());
        }
        return dto;
    }

    // Si lo necesitas en otros servicios
    public UsuarioEntity convertirAUsuariosEntity(UsuarioDTO usuario){
        UsuarioEntity e = new UsuarioEntity();
        e.setId(usuario.getId());
        e.setNombreusuario(usuario.getNombreusuario());
        e.setCorreo(usuario.getCorreo());
        // ¡OJO! Aquí NO encriptamos; deja que lo haga create/update para un solo punto de verdad.
        e.setContrasenia(usuario.getContrasenia());
        if (usuario.getRolId() != null) {
            e.setRol(entityManager.getReference(RolEntity.class, usuario.getRolId()));
        }
        return e;
    }
}
