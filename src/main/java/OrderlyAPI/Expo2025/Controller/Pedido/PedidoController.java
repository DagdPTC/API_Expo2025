package OrderlyAPI.Expo2025.Controller.Pedido;

import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatosDuplicados;
import OrderlyAPI.Expo2025.Models.DTO.PedidoDTO;
import OrderlyAPI.Expo2025.Services.Pedido.PedidoService;
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
import java.util.Map;

@RestController
@RequestMapping("/apiPedido")
@CrossOrigin
public class PedidoController {

    @Autowired
    private PedidoService service;

    @GetMapping("/getDataPedido")
    public ResponseEntity<Page<PedidoDTO>> getData(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        if (size <= 0 || size > 50){
            ResponseEntity.badRequest().body(Map.of(
                    "status", "El tamaño de la pagina debe estar entre 1 y 50"
            ));
            return ResponseEntity.ok(null);
        }
        Page<PedidoDTO> datos = service.getAllPedidos(page, size);
        if (datos == null){
            ResponseEntity.badRequest().body(Map.of(
                    "status", "No hay pedidos registrados"
            ));
        }
        return ResponseEntity.ok(datos);
    }

    // === NUEVO: obtener pedido por ID ===
    @GetMapping("/getPedidoById/{id}")
    public ResponseEntity<?> getPedidoById(@PathVariable Long id) {
        try {
            PedidoDTO dto = service.getById(id);
            return ResponseEntity.ok(dto);
        } catch (ExceptionDatoNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("status", "Not Found", "message", "Pedido no encontrado"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("status","Error","message","Error al obtener el pedido","detail",e.getMessage()));
        }
    }

    @PostMapping("/createPedido")
    public ResponseEntity<Map<String, Object>> crear(@Valid @RequestBody PedidoDTO pedido,
                                                     BindingResult bindingResult,
                                                     HttpServletRequest request){

        if (bindingResult.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "VALIDATION_ERROR",
                    "errors", errores,
                    "message", "Datos de entrada inválidos"
            ));
        }

        try{
            PedidoDTO respuesta = service.createPedido(pedido);
            if (respuesta == null){
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "Inserción incorrecta",
                        "errorType", "SERVICE_ERROR",
                        "message", "Error en el servicio"
                ));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status","success",
                    "data",respuesta));
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "error",
                            "message", "Error al registrar",
                            "detail", e.getMessage()
                    ));
        }
    }

    @PutMapping("/modificarPedido/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PedidoDTO pedido,
            BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try{
            PedidoDTO pedidoActualizado = service.updatePedido(id, pedido);
            return ResponseEntity.ok(pedidoActualizado);
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

    @DeleteMapping("/eliminarPedido/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id){
        try{
            if (!service.deletePedido(id)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("X-Mensaje-Error", "Pedido no encontrado")
                        .body(Map.of(
                                "error", "Not found",
                                "mensaje", "El Pedido no ha sido encontrado",
                                "timestamp", Instant.now().toString()
                        ));
            }
            return ResponseEntity.ok().body(Map.of(
                    "status", "Proceso completado",
                    "message", "Pedido eliminado exitosamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",
                    "message", "Error al eliminar el Pedido",
                    "detail", e.getMessage()
            ));
        }
    }
}
