package OrderlyAPI.Expo2025.Controller.HistorialPedidos;

import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatosDuplicados;
import OrderlyAPI.Expo2025.Models.DTO.HistorialPedidosDTO;
import OrderlyAPI.Expo2025.Services.HistorialPedidos.HistorialPedidosService;
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
@RequestMapping("/apiHistorialPedidos")

public class HistorialPedidosController {

    @Autowired
    private HistorialPedidosService service;

    @GetMapping("/getDataHistorialPedidos ")
    public List<HistorialPedidosDTO> getData(){
        return service.getAllhistorialpedido ();
    }

    @PostMapping("/createHistorialPedidos")
    public ResponseEntity<Map<String, Object>> crear(@Valid @RequestBody HistorialPedidosDTO historialpedidos, HttpServletRequest request){
        try{
            HistorialPedidosDTO respuesta = service.createHistorialPedidos(historialpedidos);
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
                            "message", "Error al registrar usuario",
                            "detail", e.getMessage()
                    ));
        }
    }

    @PutMapping("/modificarHistorialPedidos /{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody HistorialPedidosDTO historialpedidos,
            BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try{
            HistorialPedidosDTO historialpedidosActualizado = service.updateHistorialpedidos(id, historialpedidos);
            return ResponseEntity.ok(historialpedidosActualizado);
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

    @DeleteMapping("/eliminarHistorialPedidos/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id){
        try{
            if (!service.deleteHistorialpedidos(id)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("X-Mensaje-Error", "Historial pedidos no encontrado")
                        .body(Map.of(
                                "error", "Not found",
                                "mensaje", "El historial pedidos no ha sido encontrado",
                                "timestamp", Instant.now().toString()
                        ));
            }
            return ResponseEntity.ok().body(Map.of(
                    "status", "Proceso completado",
                    "message", "Historial pedidos eliminado exitosamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",
                    "message", "Error al eliminar el historial pedidos",
                    "detail", e.getMessage()
            ));
        }
    }
}


