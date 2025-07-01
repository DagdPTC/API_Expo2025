package OrderlyAPI.Expo2025.Services.ContactoPersona;

import OrderlyAPI.Expo2025.Entities.ContactoPersona.ContactoPersonaEntity;
import OrderlyAPI.Expo2025.Models.DTO.ContactoPersonaDTO;
import OrderlyAPI.Expo2025.Repositories.ContactoPersona.ContactoPersonaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactoPersonaService {
    private ContactoPersonaRepository repo;

    public List<ContactoPersonaDTO> getAllContactosPersona(){
        List<ContactoPersonaEntity> contactos = repo.findAll();
        return contactos.stream()
                .map(this::convertirAContactoPersonaDTO)
                .collect(Collectors.toList());
    }

    public ContactoPersonaDTO convertirAContactoPersonaDTO(ContactoPersonaEntity contactoPersona){
        ContactoPersonaDTO dto = new ContactoPersonaDTO();
        dto.setId(contactoPersona.getId());
        dto.setIdPersona(contactoPersona.getIdPersona());
        dto.setValorContacto(contactoPersona.getValorContacto());
        dto.setEsPrincipal(contactoPersona.isEsPrincipal());
        return dto;
    }
}
