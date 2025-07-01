package OrderlyAPI.Expo2025.Controller.Empleado;

import OrderlyAPI.Expo2025.Models.DTO.EmpleadoDTO;
import OrderlyAPI.Expo2025.Services.Empleado.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/apiEmpleado")
public class EmpleadoController {

    @Autowired
    private EmpleadoService service;

    @GetMapping("/getDataEmpleado")
    public List<EmpleadoDTO> getData(){
        return service.getAllEmpleados();
    }
}
