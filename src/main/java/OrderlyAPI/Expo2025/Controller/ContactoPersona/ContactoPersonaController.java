package OrderlyAPI.Expo2025.Controller.ContactoPersona;

import OrderlyAPI.Expo2025.Models.DTO.ContactoPersonaDTO;
import OrderlyAPI.Expo2025.Services.ContactoPersona.ContactoPersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/apiPersonaContacto")
public class ContactoPersonaController {

    @Autowired
    private ContactoPersonaService service;

    @GetMapping("/getDataContactoPersona")
    public List<ContactoPersonaDTO> getData(){
        return service.getAllContactosPersona();
    }
}
