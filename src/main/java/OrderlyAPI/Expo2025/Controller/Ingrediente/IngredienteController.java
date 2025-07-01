package OrderlyAPI.Expo2025.Controller.Ingrediente;

import OrderlyAPI.Expo2025.Models.DTO.IngredienteDTO;
import OrderlyAPI.Expo2025.Services.Ingrediente.IngredienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/apiIngrediente")
public class IngredienteController {

    @Autowired
    private IngredienteService service;

    @GetMapping("/getDataRol")
    public List<IngredienteDTO> getData(){
        return service.getAllIngredientes();
    }
}
