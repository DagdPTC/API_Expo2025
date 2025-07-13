package OrderlyAPI.Expo2025.Controller.Mesa;

import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatosDuplicados;
import OrderlyAPI.Expo2025.Models.DTO.MesaDTO;
import OrderlyAPI.Expo2025.Models.DTO.RolDTO;
import OrderlyAPI.Expo2025.Services.Mesa.MesaService;
import OrderlyAPI.Expo2025.Services.Rol.RolService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MesaController {

    @Autowired
    private MesaService service;

    @GetMapping("/getDataMesa")
    public List<MesaDTO> getData(){
        return service.getAllMesas();
    }

    @PostMapping("/createMesa")
    public ResponseEntity<Map<String, Object>> crear(@Valid @RequestBody MesaDTO mesa, HttpServletRequest request){
        try{
            MesaDTO respuesta = service.createMesa(mesa);
            if (respuesta == null){
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "Inserción incorrecta",
                        "errorType", "VALIDATION_ERROR",
                        "message", "Datos del usuario inválidos"
                ));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status","sucess",
                    "data",respuesta));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "error",
                            "message", "Error al registrar",
                            "detail", e.getMessage()
                    ));
        }
    }

    @PutMapping("/modificarMesa/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody MesaDTO mesa,
            BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try{
            MesaDTO mesaActualizado = service.updateMesa(id, mesa);
            return ResponseEntity.ok(mesaActualizado);
        }

        catch (ExceptionDatoNoEncontrado e){
            return ResponseEntity.notFound().build();
        }

        catch (ExceptionDatosDuplicados e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("error", "Datos duplicados", "campo", e.getCampoDuplicado())
            );
        }
    }

    @DeleteMapping("/eliminarMesa/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id){
        try{
            if (!service.deleteMesa(id)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("X-Mensaje-Error", "Mesa no encontrada")
                        .body(Map.of(
                                "error", "Not found",
                                "mensaje", "La Mesa no ha sido encontrada",
                                "timestamp", Instant.now().toString()
                        ));
            }
            return ResponseEntity.ok().body(Map.of(
                    "status", "Proceso completado",
                    "message", "Mesa eliminada exitosamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",
                    "message", "Error al eliminar la Mesa",
                    "detail", e.getMessage()
            ));
        }
    }
}
