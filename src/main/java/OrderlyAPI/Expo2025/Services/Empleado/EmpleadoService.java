package OrderlyAPI.Expo2025.Services.Empleado;

import OrderlyAPI.Expo2025.Entities.DocumentoIdentidad.DocumentoIdentidadEntity;
import OrderlyAPI.Expo2025.Entities.Empleado.EmpleadoEntity;
import OrderlyAPI.Expo2025.Entities.Persona.PersonaEntity;
import OrderlyAPI.Expo2025.Entities.Rol.RolEntity;
import OrderlyAPI.Expo2025.Entities.TipoDocumento.TipoDocumentoEntity;
import OrderlyAPI.Expo2025.Entities.Usuario.UsuarioEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.EmpleadoDTO;
import OrderlyAPI.Expo2025.Models.DTO.RolDTO;
import OrderlyAPI.Expo2025.Repositories.DocumentoIdentidad.DocumentoIdentidadRepository;
import OrderlyAPI.Expo2025.Repositories.Empleado.EmpleadoRepository;
import OrderlyAPI.Expo2025.Repositories.Pedido.PedidoRepository;
import OrderlyAPI.Expo2025.Repositories.Persona.PersonaRepository;
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
import jakarta.transaction.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@CrossOrigin
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository repo;

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private PersonaRepository personaRepository;
    @Autowired private DocumentoIdentidadRepository documentoIdentidadRepository;
    @Autowired private PedidoRepository pedidoRepository;

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

        // ---- Reasociaciones por ID (si vienen) ----
        if (dto.getIdPersona() != null) {
            e.setPersona(entityManager.getReference(PersonaEntity.class, dto.getIdPersona()));
        }
        if (dto.getIdUsuario() != null) {
            e.setUsuario(entityManager.getReference(UsuarioEntity.class, dto.getIdUsuario()));
        }

        // ---- USUARIO (username / email / rol) ----
        if (dto.getUsername() != null || dto.getEmail() != null || dto.getRolId() != null) {
            UsuarioEntity u = e.getUsuario();
            if (u == null && dto.getIdUsuario() != null) {
                u = entityManager.getReference(UsuarioEntity.class, dto.getIdUsuario());
                e.setUsuario(u);
            }
            if (u != null) {
                if (dto.getUsername() != null && !dto.getUsername().trim().isEmpty()) {
                    u.setNombreusuario(dto.getUsername());
                }
                if (dto.getEmail() != null && !dto.getEmail().trim().isEmpty()) {
                    u.setCorreo(dto.getEmail());
                }
                if (dto.getRolId() != null) {
                    RolEntity rol = entityManager.getReference(RolEntity.class, dto.getRolId());
                    u.setRol(rol); // ← AQUÍ se actualiza la FK ROLID en USUARIO
                }
            }
        }

        // ---- PERSONA (solo campos enviados) ----
        {
            PersonaEntity p = e.getPersona();
            if (p == null && dto.getIdPersona() != null) {
                p = entityManager.getReference(PersonaEntity.class, dto.getIdPersona());
                e.setPersona(p);
            }
            if (p != null) {
                if (dto.getFirstName()  != null) p.setPnombre(dto.getFirstName());
                if (dto.getSecondName() != null) p.setSnombre(dto.getSecondName());
                if (dto.getLastNameP()  != null) p.setApellidoP(dto.getLastNameP());
                if (dto.getLastNameM()  != null) p.setApellidoM(dto.getLastNameM());
                if (dto.getAddress()    != null) p.setDireccion(dto.getAddress());
                if (dto.getBirthDate()  != null) p.setFechaN(dto.getBirthDate()); // LocalDate

                // ---- DOCUMENTO IDENTIDAD (opcional) ----
                if (dto.getDocNumber() != null || dto.getDocType() != null) {
                    DocumentoIdentidadEntity d = p.getDocumento();
                    if (d == null) {
                        d = new DocumentoIdentidadEntity();
                        p.setDocumento(d);
                    }
                    if (dto.getDocNumber() != null) {
                        d.setNumDoc(dto.getDocNumber());
                    }
                    if (dto.getDocType() != null && !dto.getDocType().trim().isEmpty()) {
                        // Resolver TipoDocumento por nombre (columna real: TipoDoc)
                        TipoDocumentoEntity tipo = entityManager.createQuery(
                                        "SELECT t FROM TipoDocumentoEntity t WHERE LOWER(t.TipoDoc) = :n",
                                        TipoDocumentoEntity.class
                                )
                                .setParameter("n", dto.getDocType().toLowerCase())
                                .setMaxResults(1)
                                .getResultList()
                                .stream()
                                .findFirst()
                                .orElse(null);
                        if (tipo != null) {
                            d.setTipodocumento(tipo);
                        }
                    }
                }
            }
        }

        // ---- EMPLEADO (Fecha de contratación) ----
        if (dto.getHireDate() != null) {
            e.setFContratacion(dto.getHireDate()); // LocalDateTime
        }

        // El entity está administrado; se sincroniza al finalizar la transacción
        return convertirAEmpleadosDTO(e);
    }




    @Transactional
    public void deleteEmpleadoHard(Long id) {
        EmpleadoEntity emp = repo.findById(id)
                .orElseThrow(() -> new ExceptionDatoNoEncontrado("Empleado no encontrado"));

        // 1) Verificar referencias en Pedido (FK a Empleado)
        long pedidos = pedidoRepository.countByEmpleadoId(emp.getId()); // usa getId() del EmpleadoEntity
        if (pedidos > 0) {
            throw new IllegalStateException("No se puede eliminar: el empleado tiene pedidos relacionados.");
        }

        // 2) Guardar IDs relacionados (usa los getters reales de tus entidades)
        Long usuarioId = (emp.getUsuario() != null) ? emp.getUsuario().getId() : null;
        Long personaId = (emp.getPersona() != null) ? emp.getPersona().getId() : null;
        Long docId = null;
        if (emp.getPersona() != null && emp.getPersona().getDocumento() != null) {
            docId = emp.getPersona().getDocumento().getId();
        }

        // 3) Eliminar EMPLEADO (libera FKs hacia Persona/Usuario)
        repo.deleteById(emp.getId());

        // 4) Eliminar USUARIO
        if (usuarioId != null) {
            usuarioRepository.deleteById(usuarioId);
        }

        // 5) Eliminar PERSONA
        if (personaId != null) {
            personaRepository.deleteById(personaId);
        }

        // 6) Eliminar DOCUMENTO DE IDENTIDAD
        if (docId != null) {
            documentoIdentidadRepository.deleteById(docId);
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
        dto.setFirstName(p.getPnombre());
        dto.setSecondName(p.getSnombre());
        dto.setLastNameP(p.getApellidoP());
        dto.setLastNameM(p.getApellidoM());
        dto.setBirthDate(p.getFechaN());
        dto.setAddress(p.getDireccion());

        if (p.getDocumento() != null) {
            dto.setDocNumber(p.getDocumento().getNumDoc());
            if (p.getDocumento().getTipodocumento() != null) {
                dto.setDocType(p.getDocumento().getTipodocumento().getTipoDoc());
            }
        }

        // Usuario / Rol
        var u = e.getUsuario();
        dto.setUsername(u.getNombreusuario());
        dto.setEmail(u.getCorreo());
        if (u.getRol() != null) {
            dto.setRole(u.getRol().getRol());
            dto.setRolId(u.getRol().getId()); // <-- NUEVO
        }

        // Empleado
        dto.setHireDate(e.getFContratacion());

        return dto;
    }


}
