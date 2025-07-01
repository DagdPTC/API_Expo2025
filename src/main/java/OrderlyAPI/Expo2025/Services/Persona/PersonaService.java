package OrderlyAPI.Expo2025.Services.Persona;

import OrderlyAPI.Expo2025.Entities.Persona.PersonaEntity;
import OrderlyAPI.Expo2025.Entities.Usuario.UsuarioEntity;
import OrderlyAPI.Expo2025.Models.DTO.PersonaDTO;
import OrderlyAPI.Expo2025.Repositories.Persona.PersonaRepository;
import OrderlyAPI.Expo2025.Repositories.Usuario.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonaService {
    private PersonaRepository repo;

    public List<PersonaDTO> getAllPersonas(){
        List<PersonaEntity> personas = repo.findAll();
        return personas.stream()
                .map(this::convertirAPersonasDTO)
                .collect(Collectors.toList());
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
