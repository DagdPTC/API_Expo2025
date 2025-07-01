package OrderlyAPI.Expo2025.Controller.UnidadMedida;

import OrderlyAPI.Expo2025.Models.DTO.RolDTO;
import OrderlyAPI.Expo2025.Models.DTO.UnidadMedidaDTO;
import OrderlyAPI.Expo2025.Services.Rol.RolService;
import OrderlyAPI.Expo2025.Services.unidadMedida.UnidadMedidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/apiRol")
public class UnidadMedidaController {

    @Autowired
    private UnidadMedidaService service;

    @GetMapping("/getDataRol")
    public List<UnidadMedidaDTO> getData(){
        return service.getAllUnidadMedidas();
    }
}
