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

    public EmpleadoDTO updateEmpleado(Long id, @Valid EmpleadoDTO empleado){
        EmpleadoEntity empleadoExistente = repo.findById(id).orElseThrow(() -> new ExceptionDatoNoEncontrado("Empleado no encontrado"));

        empleadoExistente.setPersona(empleadoExistente.getPersona());
        empleadoExistente.setUsuario(empleadoExistente.getUsuario());
        empleadoExistente.setFContratacion(empleado.getFContratacion());

        EmpleadoEntity empleadoActualizado = repo.save(empleadoExistente);
        return convertirAEmpleadosDTO(empleadoActualizado);
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


    public EmpleadoEntity convertirAEmpleadosEntity(EmpleadoDTO empleado){
        EmpleadoEntity dto = new EmpleadoEntity();
        dto.setId(empleado.getId());
        dto.setPersona(entityManager.getReference(PersonaEntity.class, empleado.getIdPersona()));
        dto.setUsuario(entityManager.getReference(UsuarioEntity.class, empleado.getIdUsuario()));
        dto.setFContratacion(empleado.getFContratacion());
        return dto;
    }

    public EmpleadoDTO convertirAEmpleadosDTO(EmpleadoEntity empleado){
        EmpleadoDTO dto = new EmpleadoDTO();
        dto.setId(empleado.getId());
        dto.setIdPersona(empleado.getPersona().getId());
        dto.setIdUsuario(empleado.getUsuario().getId());
        dto.setFContratacion(empleado.getFContratacion());
        return dto;
    }
}
