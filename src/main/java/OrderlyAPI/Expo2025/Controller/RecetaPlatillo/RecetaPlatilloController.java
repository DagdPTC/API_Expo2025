package OrderlyAPI.Expo2025.Controller.RecetaPlatillo;

import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatosDuplicados;
import OrderlyAPI.Expo2025.Models.DTO.PlatilloDTO;
import OrderlyAPI.Expo2025.Models.DTO.RecetaPlatilloDTO;
import OrderlyAPI.Expo2025.Services.RecetaPlatillo.RecetaPlatilloService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping
@RestController("/apiRecetaPlatillo")
public class RecetaPlatilloController {

    @Autowired
    private RecetaPlatilloService service;

    @GetMapping("/getDataRecetaPlatillo")
    public List<RecetaPlatilloDTO> getData(){
        return service.getAllRecetaPlatillos();
    }

    @PostMapping("/createRecetaPlatillo")
    public ResponseEntity<Map<String, Object>> crear(@Valid @RequestBody RecetaPlatilloDTO recetaplatillo, HttpServletRequest request){
        try{
            RecetaPlatilloDTO respuesta = service.createRecetaPlatillo(recetaplatillo);
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

    @PutMapping("/modificar/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody RecetaPlatilloDTO recetaplatillo,
            BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try{
            RecetaPlatilloDTO recetaplatilloActualizado = service.updateRecetaPlatillo(id, recetaplatillo);
            return ResponseEntity.ok(recetaplatilloActualizado);
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

    @DeleteMapping("/eliminarRecetaPlatillo/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id){
        try{
            if (!service.deleteRecetaPlatillo(id)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("X-Mensaje-Error", "Receta Platillo no encontrado")
                        .body(Map.of(
                                "error", "Not found",
                                "mensaje", "La Receta Platillo no ha sido encontrado",
                                "timestamp", Instant.now().toString()
                        ));
            }
            return ResponseEntity.ok().body(Map.of(
                    "status", "Proceso completado",
                    "message", "Receta Platillo eliminado exitosamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",
                    "message", "Error al eliminar la Receta Platillo",
                    "detail", e.getMessage()
            ));
        }
    }
}
