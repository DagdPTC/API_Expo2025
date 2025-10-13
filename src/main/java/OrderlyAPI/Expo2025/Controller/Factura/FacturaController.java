package OrderlyAPI.Expo2025.Controller.Factura;

import OrderlyAPI.Expo2025.Models.ApiResponse.APIResponse;
import OrderlyAPI.Expo2025.Models.DTO.FacturaDTO;
import OrderlyAPI.Expo2025.Services.Factura.FacturaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/apiFactura")
@CrossOrigin
public class FacturaController {

    @Autowired
    private FacturaService service;

    @GetMapping("/getDataFactura")
    public ResponseEntity<Page<FacturaDTO>> getData(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        if (size <= 0 || size > 50){
            return ResponseEntity.badRequest().build();
        }
        Page<FacturaDTO> datos = service.getAllFacturas(page, size);
        return ResponseEntity.ok(datos);
    }

    @PostMapping("/createFactura")
    public ResponseEntity<APIResponse<FacturaDTO>> crear(@Valid @RequestBody FacturaDTO factura){
        try{
            FacturaDTO resp = service.createFacturas(factura);
            if (resp == null){
                return ResponseEntity.badRequest().body(
                        new APIResponse<>(false, "Inserción incorrecta", null)
                );
            }
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new APIResponse<>(true, "success", resp));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(false, "Error al registrar: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/eliminarFactura/{id}")
    public ResponseEntity<APIResponse<Map<String,Object>>> eliminar(@PathVariable Long id){
        try{
            boolean ok = service.deleteFactura(id);
            if (!ok){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new APIResponse<>(false, "Factura no encontrada", null));
            }
            return ResponseEntity.ok(new APIResponse<>(true, "Factura eliminada exitosamente", null));
        }catch (Exception e){
            return ResponseEntity.internalServerError()
                    .body(new APIResponse<>(false, "Error al eliminar: " + e.getMessage(), null));
        }
    }

    /**
     * ÚNICO UPDATE (transaccional):
     *  Body ejemplo:
     *  {
     *    "IdPedido": 123,
     *    "IdPlatillo": 45,       // opcional
     *    "Cantidad": 2,          // opcional (>=1)
     *    "DescuentoPct": 10.0    // 0..100 (0 permitido)
     *  }
     */
    @PutMapping("/actualizarCompleto/{idFactura}")
    public ResponseEntity<APIResponse<Map<String, Object>>> actualizarCompleto(
            @PathVariable Long idFactura,
            @RequestBody Map<String, Object> body) {

        Long idPedido   = toLong(body.get("IdPedido"), toLong(body.get("idPedido"), null));
        Long idPlatillo = toLong(body.get("IdPlatillo"), toLong(body.get("idPlatillo"), null));
        Long cantidad   = toLong(body.get("Cantidad"),   toLong(body.get("cantidad"),   null));
        Double descPct  = toDouble(body.get("DescuentoPct"), toDouble(body.get("descuentoPct"), 0.0));

        if (idPedido == null) {
            return ResponseEntity.badRequest()
                    .body(new APIResponse<>(false, "IdPedido es requerido", null));
        }

        try {
            Map<String, Object> result = service.actualizarCompleto(idFactura, idPedido, idPlatillo, cantidad, descPct);
            return ResponseEntity.ok(new APIResponse<>(true, "Actualización completa OK", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(false, "Error en actualización: " + e.getMessage(), null));
        }
    }

    // Helpers para castear sin DTO adicional
    private Long toLong(Object a, Long defVal) {
        try {
            if (a == null) return defVal;
            if (a instanceof Number n) return n.longValue();
            return Long.parseLong(a.toString());
        } catch (Exception e) { return defVal; }
    }
    private Double toDouble(Object a, Double defVal) {
        try {
            if (a == null) return defVal;
            if (a instanceof Number n) return n.doubleValue();
            return Double.parseDouble(a.toString());
        } catch (Exception e) { return defVal; }
    }
}