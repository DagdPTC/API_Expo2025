package OrderlyAPI.Expo2025.Controller.ofertas;

import OrderlyAPI.Expo2025.Models.DTO.OfertasDTO;
import OrderlyAPI.Expo2025.Models.DTO.PlatilloDTO;
import OrderlyAPI.Expo2025.Services.Cloudinary.CloudinaryService;
import OrderlyAPI.Expo2025.Services.OfertasService.OfertasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/apiOfertas")
@CrossOrigin
public class OfertasController {

    @Autowired
    private OfertasService service;

    @Autowired(required = false)
    private CloudinaryService cloudinaryService;

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

    @PostMapping("/createOfertaWithImage")
    public ResponseEntity<?> crearConImagen(
            @RequestPart("oferta") OfertasDTO ofertasDTO,
            @RequestPart(value = "image", required = false) MultipartFile file
    ) {
        try {
            if (file != null && !file.isEmpty()) {
                if (cloudinaryService == null) {
                    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Map.of(
                            "status", "error",
                            "message", "Servicio de imágenes no disponible"
                    ));
                }
                String imageUrl = cloudinaryService.uploadImage(file); // se guarda en folder "menu"
                ofertasDTO.setImagenUrl(imageUrl);
                String publicId = extractPublicIdFromUrl(imageUrl);
                if (publicId != null) ofertasDTO.setPublicId(publicId);
            }
            OfertasDTO resultado = service.createOfertas(ofertasDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "success",
                    "data", resultado
            ));
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of(
                    "status", "error",
                    "message", ex.getReason()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
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

    // ===================== UPDATE WITH IMAGE =====================
    @PutMapping("/updateOfertaoWithImage/{id}")
    public ResponseEntity<?> actualizarConImagen(
            @PathVariable Long id,
            @RequestPart("oferta") OfertasDTO ofertasDTO,
            @RequestPart(value = "image", required = false) MultipartFile file
    ) {
        try {
            if (file != null && !file.isEmpty()) {
                if (cloudinaryService == null) {
                    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(Map.of(
                            "status", "error",
                            "message", "Servicio de imágenes no disponible"
                    ));
                }
                String imageUrl = cloudinaryService.uploadImage(file); // se guarda en folder "menu"
                ofertasDTO.setImagenUrl(imageUrl);
                String publicId = extractPublicIdFromUrl(imageUrl);
                if (publicId != null) ofertasDTO.setPublicId(publicId);
            }
            OfertasDTO resultado = service.updateOfertas(id, ofertasDTO);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", resultado
            ));
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of(
                    "status", "error",
                    "message", ex.getReason()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
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

    private String extractPublicIdFromUrl(String secureUrl) {
        if (secureUrl == null || secureUrl.isBlank()) return null;
        int i = secureUrl.indexOf("/upload/");
        if (i < 0) return null;

        String afterUpload = secureUrl.substring(i + "/upload/".length());

        // remover prefijo de versión si viene (v1234567890/)
        if (afterUpload.startsWith("v")) {
            int slash = afterUpload.indexOf('/');
            if (slash > 0) afterUpload = afterUpload.substring(slash + 1);
        }

        // quitar extensión (.jpg/.png/...)
        int lastSlash = afterUpload.lastIndexOf('/');
        int lastDot   = afterUpload.lastIndexOf('.');
        if (lastDot > lastSlash) {
            afterUpload = afterUpload.substring(0, lastDot);
        }

        return afterUpload.isBlank() ? null : afterUpload;
    }
}
