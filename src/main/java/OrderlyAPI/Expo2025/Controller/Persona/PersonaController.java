package OrderlyAPI.Expo2025.Controller.Persona;

import OrderlyAPI.Expo2025.Models.DTO.PersonaDTO;
import OrderlyAPI.Expo2025.Services.Persona.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/apiPersona")
public class PersonaController {

    @Autowired
    private PersonaService service;

    @GetMapping("/getDataPersona")
    public List<PersonaDTO> getData(){
        return service.getAllPersonas();
    }
}
