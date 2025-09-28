package OrderlyAPI.Expo2025.Controller.TipoDocumento;

import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatosDuplicados;
import OrderlyAPI.Expo2025.Models.DTO.TipoDocumentoDTO;
import OrderlyAPI.Expo2025.Services.TipoDocumento.TipoDocumentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/apiTipoDocumento")
@CrossOrigin
@RequiredArgsConstructor
public class TipoDocumentoController {

    private final TipoDocumentoService service;

    // ====== LISTA (para combos) ======
    // GET http://localhost:8080/apiTipoDocumento/getDataTipoDocumento
    @GetMapping("/getDataTipoDocumento")
    public ResponseEntity<List<TipoDocumentoDTO>> getAll() {
        List<TipoDocumentoDTO> datos = service.getAllTipoDocumentList();
        return ResponseEntity.ok(datos);
    }

    // ====== PAGINADO (si alguna vista lo necesita) ======
    // GET http://localhost:8080/apiTipoDocumento/getDataTipoDocumentoPage?page=0&size=20
    @GetMapping("/getDataTipoDocumentoPage")
    public ResponseEntity<Page<TipoDocumentoDTO>> getPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        if (size <= 0 || size > 100) {
            return ResponseEntity.badRequest().build();
        }
        Page<TipoDocumentoDTO> datos = service.getAllTipoDocument(page, size);
        return ResponseEntity.ok(datos);
    }

    // ====== CREATE ======
    @PostMapping("/createTipoDocumento")
    public ResponseEntity<Map<String, Object>> crear(@Valid @RequestBody TipoDocumentoDTO dto) {
        try {
            TipoDocumentoDTO respuesta = service.createTipoDocument(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "success",
                    "data", respuesta
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Error al registrar TipoDocumento",
                    "detail", e.getMessage()
            ));
        }
    }

    // ====== UPDATE ======
    @PutMapping("/modificarTipoDocumento/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody TipoDocumentoDTO dto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(err ->
                    errores.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try {
            TipoDocumentoDTO actualizado = service.updateTipoDocumento(id, dto);
            return ResponseEntity.ok(actualizado);
        } catch (ExceptionDatoNoEncontrado e) {
            return ResponseEntity.notFound().build();
        } catch (ExceptionDatosDuplicados e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    Map.of("error", "Datos duplicados", "campo", e.getCampoDuplicado())
            );
        }
    }

    // ====== DELETE ======
    @DeleteMapping("/eliminarTipoDocumento/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id) {
        try {
            boolean ok = service.deleteTipoDocument(id);
            if (!ok) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "status", "not_found",
                        "mensaje", "TipoDocumento no encontrado",
                        "timestamp", Instant.now().toString()
                ));
            }
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "TipoDocumento eliminado correctamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Error al eliminar TipoDocumento",
                    "detail", e.getMessage()
            ));
        }
    }
}
