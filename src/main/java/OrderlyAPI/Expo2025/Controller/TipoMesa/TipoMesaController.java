package OrderlyAPI.Expo2025.Controller.TipoMesa;

import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatosDuplicados;
import OrderlyAPI.Expo2025.Models.DTO.RolDTO;
import OrderlyAPI.Expo2025.Models.DTO.TipoMesaDTO;
import OrderlyAPI.Expo2025.Services.Rol.RolService;
import OrderlyAPI.Expo2025.Services.TipoMesa.TipoMesaService;
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
@RequestMapping("/apiTipoMesa")
@CrossOrigin
public class TipoMesaController {

    @Autowired
    private TipoMesaService service;

    @GetMapping("/getDataTipoMesa")
    public ResponseEntity<Page<TipoMesaDTO>> getData(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        if (size <= 0 || size > 50){
            ResponseEntity.badRequest().body(Map.of(
                    "status", "El tamaño de la pagina debe estar entre 1 y 50"
            ));
            return ResponseEntity.ok(null);
        }
        Page<TipoMesaDTO> datos = service.getAllTipoMesas(page, size);
        if (datos == null){
            ResponseEntity.badRequest().body(Map.of(
                    "status", "No hay tipo mesas registrados"
            ));
        }
        return ResponseEntity.ok(datos);
    }
    @PostMapping("/createTipoMesa")
    public ResponseEntity<Map<String, Object>> crear(@Valid @RequestBody TipoMesaDTO tipoMesa){
        try{
            TipoMesaDTO respuesta = service.createTipomesa(tipoMesa);
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

    @PutMapping("/modificarTipoMesa/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody TipoMesaDTO tipoMesa,
            BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try{
            TipoMesaDTO tipoMesaActualizado = service.updatetipoMesa(id, tipoMesa);
            return ResponseEntity.ok(tipoMesaActualizado);
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

    @DeleteMapping("/eliminarTipoMesa/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id){
        try{
            if (!service.deleteTipoMesa(id)){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .header("X-Mensaje-Error", "Tipo Mesa no encontrada")
                        .body(Map.of(
                                "error", "Not found",
                                "mensaje", "El Tipo Mesa no ha sido encontrada",
                                "timestamp", Instant.now().toString()
                        ));
            }
            return ResponseEntity.ok().body(Map.of(
                    "status", "Proceso completado",
                    "message", "Tipo Mesa eliminado exitosamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "Error",
                    "message", "Error al eliminar el Tipo Mesa",
                    "detail", e.getMessage()
            ));
        }
    }
}
