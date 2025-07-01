package OrderlyAPI.Expo2025.Controller.Platillo;

import OrderlyAPI.Expo2025.Models.DTO.PlatilloDTO;
import OrderlyAPI.Expo2025.Models.DTO.RolDTO;
import OrderlyAPI.Expo2025.Services.Platillo.PlatilloService;
import OrderlyAPI.Expo2025.Services.Rol.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/apiPlatillo")
public class PlatilloController {

    @Autowired
    private PlatilloService service;

    @GetMapping("/getDataPlatillo")
    public List<PlatilloDTO> getData(){
        return service.getAllPlatillos();
    }
}
