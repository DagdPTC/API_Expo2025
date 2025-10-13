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

    // ========== TUS ENDPOINTS ORIGINALES (SIN CAMBIOS) ==========

    @GetMapping("/getDataPedido")
    public ResponseEntity<?> getData(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        if (size <= 0 || size > 50){
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "BAD_REQUEST",
                    "message", "El tamaño de la página debe estar entre 1 y 50"
            ));
        }

        Page<PedidoDTO> datos = service.getDataPedido(page, size);
        return ResponseEntity.ok(datos);
    }

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
                        "status", "SERVICE_ERROR",
                        "message", "No se pudo registrar el pedido"
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
            PedidoDTO pedidoActualizado = service.modificarPedido(id, pedido);
            return ResponseEntity.ok(pedidoActualizado);
        }
        catch (ExceptionDatoNoEncontrado e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "Not found", "message", "Pedido no encontrado")
            );
        }
        catch (ExceptionDatosDuplicados e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("error", "Datos duplicados", "campo", e.getCampoDuplicado())
            );
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body(
                    Map.of("error", "Error", "message", e.getMessage())
            );
        }
    }

    @DeleteMapping("/eliminarPedido/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id){
        try{
            if (!service.eliminarPedido(id)){
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

    // ========== ENDPOINT NUEVO PARA FINALIZAR PEDIDO ==========

    @PutMapping("/finalizarPedido/{id}")
    public ResponseEntity<?> finalizarPedido(@PathVariable Long id) {
        try {
            System.out.println("📞 [CONTROLLER] Llamada a finalizarPedido ID: " + id);

            PedidoDTO pedidoFinalizado = service.finalizarPedido(id);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Pedido finalizado y factura generada exitosamente",
                    "data", pedidoFinalizado
            ));

        } catch (ExceptionDatoNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("status", "Not Found", "message", "Pedido no encontrado"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("status", "Error", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("status", "Error", "message", "Error al finalizar el pedido", "detail", e.getMessage()));
        }
    }

    @GetMapping("/{id}/tieneFactura")
    public ResponseEntity<?> verificarFactura(@PathVariable Long id) {
        try {
            boolean tieneFactura = service.tieneFactura(id);
            return ResponseEntity.ok(Map.of(
                    "idPedido", id,
                    "tieneFactura", tieneFactura
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("status", "Error", "message", "Error al verificar factura"));
        }
    }
}