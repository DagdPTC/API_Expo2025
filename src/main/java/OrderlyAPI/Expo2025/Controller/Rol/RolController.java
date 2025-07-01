package OrderlyAPI.Expo2025.Controller.Rol;

import OrderlyAPI.Expo2025.Entities.Rol.RolEntity;
import OrderlyAPI.Expo2025.Models.ApiResponse.APIResponse;
import OrderlyAPI.Expo2025.Models.DTO.RolDTO;
import OrderlyAPI.Expo2025.Services.Rol.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apiRol")
public class RolController {

    @Autowired
    private RolService service;

    @GetMapping("/getDataRol")
    public List<RolDTO> getData(){
        return service.getAllRoles();
    }

    @PostMapping("/createRol")
    public APIResponse<RolDTO> crear(@RequestBody RolEntity rol){
        RolDTO dto = service.createRol(rol);
        return new APIResponse<>(true, "Rol creado Correctamente", dto);
    }

    @PutMapping("/{id}")
    public APIResponse<RolDTO> actualizar(@PathVariable Long id, @RequestBody RolEntity rol){
        RolDTO dto = service.updateRol(id, rol);
        return new APIResponse<>(true, "Rol actualizado", dto);
    }

    @DeleteMapping("/{id}")
    public APIResponse<Void> eliminar(@PathVariable Long id){
        service.deleteRol(id);
        return new APIResponse<>(true, "Rol Eliminado", null);
    }
}
