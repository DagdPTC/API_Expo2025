package OrderlyAPI.Expo2025.Services.Persona;

import OrderlyAPI.Expo2025.Entities.Persona.PersonaEntity;
import OrderlyAPI.Expo2025.Entities.Usuario.UsuarioEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.PersonaDTO;
import OrderlyAPI.Expo2025.Models.DTO.UsuarioDTO;
import OrderlyAPI.Expo2025.Repositories.Persona.PersonaRepository;
import OrderlyAPI.Expo2025.Repositories.Usuario.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PersonaService {

    @Autowired
    private PersonaRepository repo;

    public List<PersonaDTO> getAllPersonas(){
        List<PersonaEntity> personas = repo.findAll();
        return personas.stream()
                .map(this::convertirAPersonasDTO)
                .collect(Collectors.toList());
    }

    public PersonaDTO createPersona(PersonaDTO personaDTO){
        if (personaDTO == null || personaDTO.getPnombre() == null || personaDTO.getPnombre().isEmpty()){
            throw new IllegalArgumentException("La persona no puede ser nulo");
        }
        try{
            PersonaEntity personaEntity = convertirAPersonasEntity(personaDTO);
            PersonaEntity personaGuardado = repo.save(personaEntity);
            return convertirAPersonasDTO(personaGuardado);
        }catch (Exception e){
            log.error("Error al registrar persona: " + e.getMessage());
            throw new ExceptionDatoNoEncontrado("Error al registrar la persona" + e.getMessage());
        }
    }

    public PersonaDTO updatePersona(Long id, PersonaDTO persona){
        PersonaEntity personaExistente = repo.findById(id).orElseThrow(() -> new ExceptionDatoNoEncontrado("Persona no encontrada"));

        personaExistente.setPnombre(persona.getPnombre());
        personaExistente.setSnombre(persona.getSnombre());
        personaExistente.setApellidoP(persona.getApellidoP());
        personaExistente.setApellidoM(persona.getApellidoM());
        personaExistente.setFechaN(persona.getFechaN());

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
                System.out.println("Persona no encontrada");
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro persona con ID:" + id + " para eliminar.", 1);
        }
    }

    public PersonaEntity convertirAPersonasEntity(PersonaDTO persona){
        PersonaEntity dto = new PersonaEntity();
        dto.setId(persona.getId());
        dto.setPnombre(persona.getPnombre());
        dto.setSnombre(persona.getSnombre());
        dto.setApellidoM(persona.getApellidoM());
        dto.setApellidoP(persona.getApellidoP());
        dto.setFechaN(persona.getFechaN());
        return dto;
    }

    public PersonaDTO convertirAPersonasDTO(PersonaEntity persona){
        PersonaDTO dto = new PersonaDTO();
        dto.setId(persona.getId());
        dto.setPnombre(persona.getPnombre());
        dto.setSnombre(persona.getSnombre());
        dto.setApellidoM(persona.getApellidoM());
        dto.setApellidoP(persona.getApellidoP());
        dto.setFechaN(persona.getFechaN());
        return dto;
    }
}
