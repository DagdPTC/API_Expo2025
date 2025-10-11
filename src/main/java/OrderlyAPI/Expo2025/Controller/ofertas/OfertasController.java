package OrderlyAPI.Expo2025.Controller.ofertas;

import OrderlyAPI.Expo2025.Models.DTO.OfertasDTO;
import OrderlyAPI.Expo2025.Services.OfertasService.OfertasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/apiOfertas")
@CrossOrigin
public class OfertasController {

    @Autowired
    private OfertasService service;

    // ===== GET ALL (paginado) =====
    @GetMapping("/getDataOfertas")
    public ResponseEntity<Map<String, Object>> getData(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<OfertasDTO> ofertas = service.getAllOfertas(page, size);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", ofertas.getContent(),
                "totalElements", ofertas.getTotalElements(),
                "totalPages", ofertas.getTotalPages(),
                "currentPage", ofertas.getNumber()
        ));
    }

    // ===== CREATE =====
    @PostMapping("/createOfertas")
    public ResponseEntity<Map<String, Object>> crear(@Valid @RequestBody OfertasDTO dto) {
        OfertasDTO creada = service.createOfertas(dto);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", creada,
                "timestamp", Instant.now().toString()
        ));
    }

    // ===== UPDATE =====
    @PutMapping("/modificarOfertas/{id}")
    public ResponseEntity<Map<String, Object>> actualizar(@PathVariable Long id,
                                                          @Valid @RequestBody OfertasDTO dto) {
        OfertasDTO actualizada = service.updateOfertas(id, dto);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", actualizada,
                "timestamp", Instant.now().toString()
        ));
    }

    // ===== DELETE =====
    @DeleteMapping("/eliminarOfertas/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id) {
        boolean eliminado = service.deleteOfertas(id);

        if (!eliminado) {
            return ResponseEntity.status(404).body(Map.of(
                    "error", "Not found",
                    "mensaje", "La oferta no ha sido encontrada",
                    "timestamp", Instant.now().toString()
            ));
        }

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "mensaje", "Oferta eliminada correctamente",
                "timestamp", Instant.now().toString()
        ));
    }
}
