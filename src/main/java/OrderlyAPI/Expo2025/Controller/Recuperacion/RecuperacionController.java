package OrderlyAPI.Expo2025.Controller.Recuperacion;

import OrderlyAPI.Expo2025.Services.Recuperacion.RecuperacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth/recovery")
@RequiredArgsConstructor
public class RecuperacionController {

    private final RecuperacionService service;

    @PostMapping("/request")
    public ResponseEntity<?> request(@RequestBody Map<String,String> body,
                                     @RequestHeader(value = "X-Forwarded-For", required = false) String xff) {
        try {
            String correo = body.getOrDefault("correo","").trim();
            if (correo.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error","Correo requerido"));
            }
            String ip = (xff != null && !xff.isBlank()) ? xff.split(",")[0].trim() : "0.0.0.0";
            service.solicitarCodigo(correo, ip);
            return ResponseEntity.ok(Map.of("ok", true, "message", "Código enviado"));
        } catch (IllegalArgumentException e) {
            // p.ej. "El correo no existe"
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (IllegalStateException e) {
            // p.ej. "Demasiadas solicitudes. Intenta más tarde."
            return ResponseEntity.status(429).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error interno"));
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody Map<String,String> body) {
        try {
            String correo = body.getOrDefault("correo","").trim();
            String codigo = body.getOrDefault("codigo","").trim();
            if (correo.isEmpty() || codigo.isEmpty())
                return ResponseEntity.badRequest().body(Map.of("error","Datos incompletos"));
            service.validarCodigo(correo, codigo);
            return ResponseEntity.ok(Map.of("ok", true));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage())); // p.ej. "Código incorrecto"
        } catch (IllegalStateException e) {
            // expirado, sin activo, intentos, etc.
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error interno"));
        }
    }


    @PostMapping("/reset")
    public ResponseEntity<?> reset(@RequestBody Map<String,String> body) {
        String correo = body.getOrDefault("correo","").trim();
        String nuevaContrasena = body.getOrDefault("nuevaContrasena","").trim();

        // Validaciones
        if (correo.isEmpty() || nuevaContrasena.isEmpty())
            return ResponseEntity.badRequest().body(Map.of("error","Datos incompletos"));

        if (nuevaContrasena.length() < 8)
            return ResponseEntity.badRequest().body(Map.of("error","La contraseña debe tener al menos 8 caracteres"));

        // Validación de complejidad (opcional pero recomendado)
        if (!nuevaContrasena.matches(".*[A-Z].*"))
            return ResponseEntity.badRequest().body(Map.of("error","La contraseña debe contener al menos una mayúscula"));

        if (!nuevaContrasena.matches(".*[0-9].*"))
            return ResponseEntity.badRequest().body(Map.of("error","La contraseña debe contener al menos un número"));

        try {
            service.resetearContrasena(correo, nuevaContrasena);
            return ResponseEntity.ok(Map.of("ok", true, "message", "Contraseña actualizada exitosamente"));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno al procesar la solicitud"));
        }
    }
}
