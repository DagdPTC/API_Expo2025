package OrderlyAPI.Expo2025.Controller.Platillo;

import OrderlyAPI.Expo2025.Exceptions.ExceptionDatosDuplicados;
import OrderlyAPI.Expo2025.Exceptions.ExceptionDatoNoEncontrado;
import OrderlyAPI.Expo2025.Models.DTO.PlatilloDTO;
import OrderlyAPI.Expo2025.Services.Platillo.PlatilloService;
import OrderlyAPI.Expo2025.Services.Cloudinary.CloudinaryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/apiPlatillo")
@CrossOrigin(origins = {"http://localhost:5500", "http://127.0.0.1:5500", "http://localhost"})
public class PlatilloController {

    @Autowired
    private PlatilloService service;

    @Autowired
    private CloudinaryService cloudinaryService;

    // ===================== GET =====================
    @GetMapping("/getDataPlatillo")
    public ResponseEntity<?> getData(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        if (size <= 0 || size > 50) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "El parámetro size debe estar entre 1 y 50"
            ));
        }
        try {
            Page<PlatilloDTO> datos = service.getAllPlatillos(page, size);
            return ResponseEntity.ok(datos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Error al obtener platillos",
                    "detail", e.getMessage()
            ));
        }
    }

    // ===================== CREATE =====================
    @PostMapping("/createPlatillo")
    public ResponseEntity<?> crear(@Valid @RequestBody PlatilloDTO platillo, HttpServletRequest request) {
        try {
            PlatilloDTO respuesta = service.createPlatillo(platillo);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "success",
                    "data", respuesta
            ));
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of(
                    "status", "error",
                    "message", ex.getReason()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Error al registrar platillo",
                    "detail", e.getMessage()
            ));
        }
    }

    // ===================== CREATE WITH IMAGE =====================
    @PostMapping("/createPlatilloWithImage")
    public ResponseEntity<?> crearConImagen(
            @RequestPart("platillo") PlatilloDTO platilloDTO,
            @RequestPart(value = "image", required = false) MultipartFile file
    ) {
        try {
            if (file != null && !file.isEmpty()) {
                String imageUrl = cloudinaryService.uploadImage(file); // se guarda en folder "menu"
                platilloDTO.setImagenUrl(imageUrl);
                String publicId = extractPublicIdFromUrl(imageUrl);
                if (publicId != null) platilloDTO.setPublicId(publicId);
            }
            PlatilloDTO resultado = service.createPlatillo(platilloDTO);
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

    // ===================== UPDATE =====================
    @PutMapping("/modificarPlatillo/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PlatilloDTO platillo,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try {
            PlatilloDTO actualizado = service.updatePlatillo(id, platillo);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", actualizado
            ));
        } catch (ExceptionDatoNoEncontrado e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "error",
                    "message", "Platillo no encontrado"
            ));
        } catch (ExceptionDatosDuplicados e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                    "status", "error",
                    "message", "Datos duplicados",
                    "campo", e.getCampoDuplicado()
            ));
        }
    }

    // ===================== UPDATE WITH IMAGE =====================
    @PutMapping("/updatePlatilloWithImage/{id}")
    public ResponseEntity<?> actualizarConImagen(
            @PathVariable Long id,
            @RequestPart("platillo") PlatilloDTO platilloDTO,
            @RequestPart(value = "image", required = false) MultipartFile file
    ) {
        try {
            if (file != null && !file.isEmpty()) {
                String imageUrl = cloudinaryService.uploadImage(file); // se guarda en folder "menu"
                platilloDTO.setImagenUrl(imageUrl);
                String publicId = extractPublicIdFromUrl(imageUrl);
                if (publicId != null) platilloDTO.setPublicId(publicId);
            }
            PlatilloDTO resultado = service.updatePlatillo(id, platilloDTO);
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

    // ===================== DELETE =====================
    @DeleteMapping("/eliminarPlatillo/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            if (!service.deletePlatillo(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "status", "error",
                        "message", "Platillo no encontrado",
                        "timestamp", Instant.now().toString()
                ));
            }
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Platillo eliminado exitosamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Error al eliminar el Platillo",
                    "detail", e.getMessage()
            ));
        }
    }

    // ===================== Utils =====================
    /**
     * Extrae el publicId de una secure_url de Cloudinary.
     * Ej: .../upload/v1720000000/menu/img_abc123.jpg  ->  menu/img_abc123
     */
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
