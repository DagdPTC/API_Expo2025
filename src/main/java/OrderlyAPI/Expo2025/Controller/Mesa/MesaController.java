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
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/apiMesa")
@CrossOrigin
public class MesaController {

    @Autowired
    private MesaService service;

    @GetMapping("/getDataMesa")
    public ResponseEntity<Page<MesaDTO>> getData(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        if (size <= 0 || size > 50){
            ResponseEntity.badRequest().body(Map.of(
                    "status", "El tamaño de la pagina debe estar entre 1 y 50"
            ));
            return ResponseEntity.ok(null);
        }
        Page<MesaDTO> datos = service.getAllMesas(page, size);
        if (datos == null){
            ResponseEntity.badRequest().body(Map.of(
                    "status", "No hay mesas registrados"
            ));
        }
        return ResponseEntity.ok(datos);
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

    @PatchMapping("/estado/{id}/{estadoId}")
    public ResponseEntity<?> actualizarSoloEstado(@PathVariable Long id, @PathVariable Long estadoId) {
        try {
            MesaDTO actualizado = service.actualizarEstado(id, estadoId);
            return ResponseEntity.ok(actualizado); // 200 con el DTO actualizado
        } catch (ExceptionDatoNoEncontrado e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",
                    "message", "Error al actualizar el estado de la mesa",
                    "detail", e.getMessage()
            ));
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
