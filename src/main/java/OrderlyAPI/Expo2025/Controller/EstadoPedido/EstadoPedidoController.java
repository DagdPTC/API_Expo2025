package OrderlyAPI.Expo2025.Controller.EstadoPedido;

import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatosDuplicados;
import OrderlyAPI.Expo2025.Models.DTO.EstadoPedidoDTO;
import OrderlyAPI.Expo2025.Models.DTO.RolDTO;
import OrderlyAPI.Expo2025.Services.EstadoPedido.EstadoPedidoService;
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
@RequestMapping("/apiEstadoPedido")
public class EstadoPedidoController {

    @Autowired
    private EstadoPedidoService service;

    @GetMapping("/getDataEstadoPedido")
    public List<EstadoPedidoDTO> getData(){
        return service.getAllEstadoPedidos();
    }

    @PostMapping("/createEstadoPedido")
    public ResponseEntity<Map<String, Object>> crear(@Valid @RequestBody EstadoPedidoDTO estadoPedido, HttpServletRequest request){
        try{
            EstadoPedidoDTO respuesta = service.createEstadoPedido(estadoPedido);
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

    @PutMapping("/modificarEstadoPedido/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody EstadoPedidoDTO estadoPedido,
            BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try{
            EstadoPedidoDTO estadoPedidoActualizado = service.updateEstadoPedido(id, estadoPedido);
            return ResponseEntity.ok(estadoPedidoActualizado);
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

    @DeleteMapping("/eliminarEstadoPedido/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id){
        try{
            if (!service.deleteEstadoPedido(id)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("X-Mensaje-Error", "Estado Pedido no encontrado")
                        .body(Map.of(
                                "error", "Not found",
                                "mensaje", "El Estado Pedido no ha sido encontrado",
                                "timestamp", Instant.now().toString()
                        ));
            }
            return ResponseEntity.ok().body(Map.of(
                    "status", "Proceso completado",
                    "message", "Estado Pedido eliminado exitosamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",
                    "message", "Error al eliminar el Estado Pedido",
                    "detail", e.getMessage()
            ));
        }
    }
}
