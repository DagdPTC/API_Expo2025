package OrderlyAPI.Expo2025.Services.ContactoPersona;

import OrderlyAPI.Expo2025.Entities.ContactoPersona.ContactoPersonaEntity;
import OrderlyAPI.Expo2025.Entities.Rol.RolEntity;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.ContactoPersonaDTO;
import OrderlyAPI.Expo2025.Models.DTO.RolDTO;
import OrderlyAPI.Expo2025.Repositories.ContactoPersona.ContactoPersonaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ContactoPersonaService {
    private ContactoPersonaRepository repo;

    public List<ContactoPersonaDTO> getAllContactosPersona(){
        List<ContactoPersonaEntity> contactos = repo.findAll();
        return contactos.stream()
                .map(this::convertirAContactoPersonaDTO)
                .collect(Collectors.toList());
    }

    public ContactoPersonaDTO createContactoPersona(ContactoPersonaDTO contactoPersonaDTO){
        if (contactoPersonaDTO == null || contactoPersonaDTO.getValorContacto() == null || contactoPersonaDTO.getValorContacto().isEmpty()){
            throw new IllegalArgumentException("El contacto persona no puede ser nulo");
        }
        try{
            ContactoPersonaEntity contactoPersonaEntity = convertirAContactoPersonaEntity(contactoPersonaDTO);
            ContactoPersonaEntity contactoPersonaGuardado = repo.save(contactoPersonaEntity);
            return convertirAContactoPersonaDTO(contactoPersonaGuardado);
        }catch (Exception e){
            log.error("Error al registrar cpntacto persona: " + e.getMessage());
            throw new ExceptionDatoNoEncontrado("Error al registrar contacto persona" + e.getMessage());
        }
    }

    public ContactoPersonaDTO updateContactoPersona(Long id, ContactoPersonaDTO contactoPersona){
        ContactoPersonaEntity contactoPersonaExistente = repo.findById(id).orElseThrow(() -> new ExceptionDatoNoEncontrado("Contacto Persona no encontrado"));

        contactoPersonaExistente.setIdPersona(contactoPersona.getIdPersona());
        contactoPersonaExistente.setTipoContacto(contactoPersona.getTipoContacto());
        contactoPersonaExistente.setValorContacto(contactoPersona.getValorContacto());
        contactoPersonaExistente.setEsPrincipal(contactoPersona.isEsPrincipal());

        ContactoPersonaEntity contactoPersonaActualizado = repo.save(contactoPersonaExistente);
        return convertirAContactoPersonaDTO(contactoPersonaActualizado);
    }

    public boolean deleteContactoPersona(Long id){
        try{
            ContactoPersonaEntity objContactoPersona = repo.findById(id).orElse(null);
            if (objContactoPersona != null){
                repo.deleteById(id);
                return true;
            }else{
                System.out.println("Contacto Persona no encontrado");
                return false;
            }
        }catch (EmptyResultDataAccessException e){
            throw new EmptyResultDataAccessException("No se encontro contacto persona con ID:" + id + " para eliminar.", 1);
        }
    }


    public ContactoPersonaEntity convertirAContactoPersonaEntity(ContactoPersonaDTO contactoPersona){
        ContactoPersonaEntity dto = new ContactoPersonaEntity();
        dto.setId(contactoPersona.getId());
        dto.setIdPersona(contactoPersona.getIdPersona());
        dto.setValorContacto(contactoPersona.getValorContacto());
        dto.setEsPrincipal(contactoPersona.isEsPrincipal());
        return dto;
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
