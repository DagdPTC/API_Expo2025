package OrderlyAPI.Expo2025.Services.Persona;

import OrderlyAPI.Expo2025.Entities.DocumentoIdentidad.DocumentoIdentidadEntity;
import OrderlyAPI.Expo2025.Entities.Persona.PersonaEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.PersonaDTO;
import OrderlyAPI.Expo2025.Repositories.Persona.PersonaRepository;
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
public class PersonaService {

    @Autowired
    private PersonaRepository repo;

    @PersistenceContext
    EntityManager entityManager;

    public Page<PersonaDTO> getAllPersonas(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<PersonaEntity> personas = repo.findAll(pageable);
        return personas.map(this::convertirAPersonasDTO);
    }

    public PersonaDTO createPersona(@Valid PersonaDTO personaDTO){
        if (personaDTO == null){
            throw new IllegalArgumentException("La persona no puede ser nula");
        }
        try{
            PersonaEntity personaEntity = convertirAPersonasEntity(personaDTO);
            PersonaEntity personaGuardado = repo.save(personaEntity);
            return convertirAPersonasDTO(personaGuardado);
        }catch (Exception e){
            log.error("Error al registrar persona: " + e.getMessage(), e);
            throw new ExceptionDatoNoEncontrado("Error al registrar la persona: " + e.getMessage());
        }
    }

    public PersonaDTO updatePersona(Long id, @Valid PersonaDTO persona){
        PersonaEntity personaExistente = repo.findById(id)
                .orElseThrow(() -> new ExceptionDatoNoEncontrado("Persona no encontrada"));

        personaExistente.setPnombre(persona.getPnombre());
        personaExistente.setSnombre(persona.getSnombre());
        personaExistente.setApellidoP(persona.getApellidoP());
        personaExistente.setApellidoM(persona.getApellidoM());
        personaExistente.setFechaN(persona.getFechaN());
        personaExistente.setDireccion(persona.getDireccion());
        personaExistente.setDocumento(personaExistente.getDocumento());

        PersonaEntity personaActualizado = repo.save(personaExistente);
        return convertirAPersonasDTO(personaActualizado);
    }

    public boolean deletePersona(Long id){
        try{
            PersonaEntity objPersona = repo.findById(id).orElse(null);
            if (objPersona != null){
                repo.deleteById(id);
                return true;
            }else{
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontró persona con ID:" + id + " para eliminar.", 1);
        }
    }

    public PersonaEntity convertirAPersonasEntity(PersonaDTO persona){
        if (persona.getIdDoc() == null){
            throw new IllegalArgumentException("IdDoc es obligatorio para crear la Persona");
        }

        PersonaEntity e = new PersonaEntity();
        e.setId(persona.getId());
        e.setPnombre(persona.getPnombre());
        e.setSnombre(persona.getSnombre());
        e.setApellidoM(persona.getApellidoM());
        e.setApellidoP(persona.getApellidoP());
        e.setFechaN(persona.getFechaN());
        e.setDireccion(persona.getDireccion());
        // referencia al DocumentoIdentidad (debe existir)
        e.setDocumento(entityManager.getReference(DocumentoIdentidadEntity.class, persona.getIdDoc()));
        return e;
    }

    public PersonaDTO convertirAPersonasDTO(PersonaEntity persona){
        PersonaDTO dto = new PersonaDTO();
        dto.setId(persona.getId());
        dto.setPnombre(persona.getPnombre());
        dto.setSnombre(persona.getSnombre());
        dto.setApellidoM(persona.getApellidoM());
        dto.setApellidoP(persona.getApellidoP());
        dto.setFechaN(persona.getFechaN());
        dto.setDireccion(persona.getDireccion());
        dto.setIdDoc(persona.getDocumento().getId());
        return dto;
    }
}
