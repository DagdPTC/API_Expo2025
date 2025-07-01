package OrderlyAPI.Expo2025.Controller.Usuario;

import OrderlyAPI.Expo2025.Models.DTO.UsuarioDTO;
import OrderlyAPI.Expo2025.Services.Usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/apiUsuario")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping("/getDataUsuario")
    private List<UsuarioDTO> getData(){
        return service.getAllUsuarios();
    }
}
