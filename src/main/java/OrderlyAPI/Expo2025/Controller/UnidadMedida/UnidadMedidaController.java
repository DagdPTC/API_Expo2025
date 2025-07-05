package OrderlyAPI.Expo2025.Controller.UnidadMedida;

import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatosDuplicados;
import OrderlyAPI.Expo2025.Models.DTO.RolDTO;
import OrderlyAPI.Expo2025.Models.DTO.UnidadMedidaDTO;
import OrderlyAPI.Expo2025.Services.Rol.RolService;
import OrderlyAPI.Expo2025.Services.unidadMedida.UnidadMedidaService;
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

@RestController
@RequestMapping("/apiUnidadMedida")
public class UnidadMedidaController {

    @Autowired
    private UnidadMedidaService service;

    @GetMapping("/getDataUnidadMedida")
    public List<UnidadMedidaDTO> getData(){
        return service.getAllUnidadMedidas();
    }


    @PostMapping("/createUnidadMedida")
    public ResponseEntity<Map<String, Object>> crear(@Valid @RequestBody UnidadMedidaDTO unidadMedida, HttpServletRequest request){
        try{
            UnidadMedidaDTO respuesta = service.createUnidadMedia(unidadMedida);
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

    @PutMapping("/modificarUnidadMedida/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UnidadMedidaDTO unidadMedida,
            BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try{
            UnidadMedidaDTO unidadMedidaActualizado = service.updateUnidadMedida(id, unidadMedida);
            return ResponseEntity.ok(unidadMedidaActualizado);
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

    @DeleteMapping("/eliminarUnidadMedida/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id){
        try{
            if (!service.deleteUnidadMedida(id)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("X-Mensaje-Error", "Unidad Medida no encontrada")
                        .body(Map.of(
                                "error", "Not found",
                                "mensaje", "La Unidad Medida no ha sido encontrado",
                                "timestamp", Instant.now().toString()
                        ));
            }
            return ResponseEntity.ok().body(Map.of(
                    "status", "Proceso completado",
                    "message", "Unidad Medida eliminado exitosamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",
                    "message", "Error al eliminar la Unidad Medida",
                    "detail", e.getMessage()
            ));
        }
    }
}
