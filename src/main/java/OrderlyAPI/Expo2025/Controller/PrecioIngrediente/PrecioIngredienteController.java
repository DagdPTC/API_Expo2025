package OrderlyAPI.Expo2025.Controller.PrecioIngrediente;

import OrderlyAPI.Expo2025.Models.DTO.PrecioIngredienteDTO;
import OrderlyAPI.Expo2025.Services.PrecioIngrediente.PrecioIngredienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/apiPrecioIngrediente")
public class PrecioIngredienteController {

    @Autowired
    private PrecioIngredienteService service;

    @GetMapping("/getDataRol")
    public List<PrecioIngredienteDTO> getData(){
        return service.getAllPrecioIngredientes();
    }
}
