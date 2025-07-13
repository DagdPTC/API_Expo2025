package OrderlyAPI.Expo2025.Controller.EstadoReserva;

import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatosDuplicados;
import OrderlyAPI.Expo2025.Models.DTO.EstadoReservaDTO;
import OrderlyAPI.Expo2025.Models.DTO.RolDTO;
import OrderlyAPI.Expo2025.Services.EstadoReserva.EstadoReservaService;
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
@RequestMapping("/apiEstadoReserva")
public class EstadoReservaController {

    @Autowired
    private EstadoReservaService service;

    @GetMapping("/getDataEstadoReserva")
    public List<EstadoReservaDTO> getData(){
        return service.getAllEstadoReservas();
    }

    @PostMapping("/createEstadoReserva")
    public ResponseEntity<Map<String, Object>> crear(@Valid @RequestBody EstadoReservaDTO estadoReserva, HttpServletRequest request){
        try{
            EstadoReservaDTO respuesta = service.createEstadoReserva(estadoReserva);
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

    @PutMapping("/modificarEstadoReserva/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody EstadoReservaDTO estadoReserva,
            BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try{
            EstadoReservaDTO estadoReservaActualizado = service.updateEstadoReserva(id, estadoReserva);
            return ResponseEntity.ok(estadoReservaActualizado);
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

    @DeleteMapping("/eliminarEstadoReserva/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id){
        try{
            if (!service.deleteEstadoReserva(id)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("X-Mensaje-Error", "Estado Reserva no encontrado")
                        .body(Map.of(
                                "error", "Not found",
                                "mensaje", "El Estado Reserva no ha sido encontrado",
                                "timestamp", Instant.now().toString()
                        ));
            }
            return ResponseEntity.ok().body(Map.of(
                    "status", "Proceso completado",
                    "message", "Estado Reserva eliminado exitosamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",
                    "message", "Error al eliminar el Estado Reserva",
                    "detail", e.getMessage()
            ));
        }
    }
}
