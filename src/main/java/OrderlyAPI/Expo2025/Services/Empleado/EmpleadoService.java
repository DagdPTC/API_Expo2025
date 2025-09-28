package OrderlyAPI.Expo2025.Services.Empleado;

import OrderlyAPI.Expo2025.Entities.Empleado.EmpleadoEntity;
import OrderlyAPI.Expo2025.Entities.Persona.PersonaEntity;
import OrderlyAPI.Expo2025.Entities.Rol.RolEntity;
import OrderlyAPI.Expo2025.Entities.TipoDocumento.TipoDocumentoEntity;
import OrderlyAPI.Expo2025.Entities.Usuario.UsuarioEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.EmpleadoDTO;
import OrderlyAPI.Expo2025.Models.DTO.RolDTO;
import OrderlyAPI.Expo2025.Repositories.Empleado.EmpleadoRepository;
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
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@CrossOrigin
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository repo;

    @PersistenceContext
    EntityManager entityManager;

    public Page<EmpleadoDTO> getAllEmpleados(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<EmpleadoEntity> roles = repo.findAll(pageable);
        return roles.map(this::convertirAEmpleadosDTO);
    }

    public EmpleadoDTO createEmpleado(@Valid EmpleadoDTO empleadoDTO){
        if (empleadoDTO == null){
            throw new IllegalArgumentException("El Empleado no puede ser nulo");
        }
        try{
            EmpleadoEntity empleadoEntity = convertirAEmpleadosEntity(empleadoDTO);
            EmpleadoEntity empleadoGuardado = repo.save(empleadoEntity);
            return convertirAEmpleadosDTO(empleadoGuardado);
        }catch (Exception e){
            log.error("Error al registrar empleado: " + e.getMessage());
            throw new ExceptionDatoNoEncontrado("Error al registrar el empleado" + e.getMessage());
        }
    }

    @Transactional
    public EmpleadoDTO updateEmpleado(Long id, @Valid EmpleadoDTO dto) {
        EmpleadoEntity e = repo.findById(id)
                .orElseThrow(() -> new ExceptionDatoNoEncontrado("Empleado no encontrado"));

        // Actualiza relaciones solo si te las mandan
        if (dto.getIdPersona() != null) {
            e.setPersona(entityManager.getReference(PersonaEntity.class, dto.getIdPersona()));
        }
        if (dto.getIdUsuario() != null) {
            e.setUsuario(entityManager.getReference(UsuarioEntity.class, dto.getIdUsuario()));
        }

        // Fecha de contratación
        if (dto.getHireDate() != null) {
            // Si EmpleadoEntity usa LocalDateTime:
            e.setFContratacion(dto.getHireDate());

        /*  Si usa LocalDate:
            e.setFContratacion(dto.getHireDate().toLocalDate());
        */
        }

        EmpleadoEntity actualizado = repo.save(e);
        return convertirAEmpleadosDTO(actualizado);
    }


    public boolean deleteEmpleado(Long id){
        try{
            EmpleadoEntity objEmpleado = repo.findById(id).orElse(null);
            if (objEmpleado != null){
                repo.deleteById(id);
                return true;
            }else{
                System.out.println("Empleado no encontrado");
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro empleado con ID:" + id + " para eliminar.", 1);
        }
    }


    public EmpleadoEntity convertirAEmpleadosEntity(EmpleadoDTO dto) {
        EmpleadoEntity e = new EmpleadoEntity();

        if (dto.getId() != null) {
            e.setId(dto.getId());
        }

        // Persona (solo si viene el id)
        if (dto.getIdPersona() != null) {
            e.setPersona(entityManager.getReference(PersonaEntity.class, dto.getIdPersona()));
        }

        // Usuario (solo si viene el id)
        if (dto.getIdUsuario() != null) {
            e.setUsuario(entityManager.getReference(UsuarioEntity.class, dto.getIdUsuario()));
        }

        // Fecha de contratación
        // DTO -> hireDate
        if (dto.getHireDate() != null) {
            // Si tu entidad usa LocalDateTime:
            e.setFContratacion(dto.getHireDate());

        /*  Si tu entidad usa LocalDate en lugar de LocalDateTime,
            cambia la línea anterior por:
            e.setFContratacion(dto.getHireDate().toLocalDate());
        */
        }

        return e;
    }


    public EmpleadoDTO convertirAEmpleadosDTO(EmpleadoEntity e) {
        EmpleadoDTO dto = new EmpleadoDTO();

        dto.setId(e.getId());
        dto.setIdPersona(e.getPersona().getId());
        dto.setIdUsuario(e.getUsuario().getId());

        // Persona
        var p = e.getPersona();
        dto.setFirstName(p.getPnombre());         // map a PrimerNombre
        dto.setSecondName(p.getSnombre());        // SegundoNombre
        dto.setLastNameP(p.getApellidoP());       // ApellidoPaterno
        dto.setLastNameM(p.getApellidoM());       // ApellidoMaterno
        dto.setBirthDate(p.getFechaN());          // DATE → LocalDate
        dto.setAddress(p.getDireccion());

        if (p.getDocumento() != null) {
            dto.setDocNumber(p.getDocumento().getNumDoc()); // NumeroDocumento
            if (p.getDocumento().getTipodocumento() != null) {
                dto.setDocType(p.getDocumento().getTipodocumento().getTipoDoc()); // TipoDocumento
            }
        }

        // Usuario / Rol
        var u = e.getUsuario();
        dto.setUsername(u.getNombreusuario());
        dto.setEmail(u.getCorreo());
        if (u.getRol() != null) {
            dto.setRole(u.getRol().getRol());
        }

        // Empleado
        dto.setHireDate(e.getFContratacion()); // DATE → LocalDateTime

        return dto;
    }

}
