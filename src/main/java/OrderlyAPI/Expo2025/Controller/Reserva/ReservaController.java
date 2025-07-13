package OrderlyAPI.Expo2025.Controller.Reserva;

import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatosDuplicados;
import OrderlyAPI.Expo2025.Models.DTO.ReservaDTO;
import OrderlyAPI.Expo2025.Models.DTO.RolDTO;
import OrderlyAPI.Expo2025.Services.Reserva.ReservaService;
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

@RestController
@RequestMapping("/apiReserva")
public class ReservaController {

    @Autowired
    private ReservaService service;

    @GetMapping("/getDataReserva")
    public List<ReservaDTO> getData(){
        return service.getAllReservas();
    }

    @PostMapping("/createReserva")
    public ResponseEntity<Map<String, Object>> crear(@Valid @RequestBody ReservaDTO reserva, HttpServletRequest request){
        try{
            ReservaDTO respuesta = service.createReserva(reserva);
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

    @PutMapping("/modificarReserva/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ReservaDTO reserva,
            BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try{
            ReservaDTO reservaActualizado = service.updateReserva(id, reserva);
            return ResponseEntity.ok(reservaActualizado);
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

    @DeleteMapping("/eliminarReserva/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id){
        try{
            if (!service.deleteReserva(id)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("X-Mensaje-Error", "Reserva no encontrada")
                        .body(Map.of(
                                "error", "Not found",
                                "mensaje", "La reserva no ha sido encontrada",
                                "timestamp", Instant.now().toString()
                        ));
            }
            return ResponseEntity.ok().body(Map.of(
                    "status", "Proceso completado",
                    "message", "Reserva eliminada exitosamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",
                    "message", "Error al eliminar la reserva",
                    "detail", e.getMessage()
            ));
        }
    }
}
