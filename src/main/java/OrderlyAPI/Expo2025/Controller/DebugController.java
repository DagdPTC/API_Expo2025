package OrderlyAPI.Expo2025.Controller;

import OrderlyAPI.Expo2025.Utils.JWTUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * TEMPORAL - ELIMINAR DESPUÉS DE DIAGNOSTICAR
 * Solo para debugging del problema JWT
 */
@RestController
@RequestMapping("/debug")
public class DebugController {

    private final JWTUtils jwtUtils;

    @Value("${security.jwt.secret}")
    private String jwtSecret;

    public DebugController(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    /**
     * Endpoint para verificar que el secret esté correctamente configurado
     * ELIMINAR EN PRODUCCIÓN
     */
    @GetMapping("/jwt-config")
    public ResponseEntity<?> checkJwtConfig() {
        return ResponseEntity.ok(Map.of(
                "secretLength", jwtSecret.length(),
                "secretFirstChar", jwtSecret.substring(0, 1),
                "secretLastChar", jwtSecret.substring(jwtSecret.length() - 1),
                "hasLeadingSpace", jwtSecret.startsWith(" "),
                "hasTrailingSpace", jwtSecret.endsWith(" ")
        ));
    }

    /**
     * Endpoint para generar y validar un token en el mismo request
     */
    @PostMapping("/test-token")
    public ResponseEntity<?> testToken() {
        try {
            // Generar token
            String token = jwtUtils.create("test@test.com", "999", "ADMIN");

            // Validar inmediatamente
            boolean isValid = jwtUtils.validate(token);

            // Extraer claims
            String subject = jwtUtils.getValue(token);
            String rol = jwtUtils.extractRol(token);

            return ResponseEntity.ok(Map.of(
                    "tokenGenerated", token.substring(0, 30) + "...",
                    "tokenLength", token.length(),
                    "isValid", isValid,
                    "subject", subject,
                    "rol", rol,
                    "message", "✓ Token generado y validado exitosamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "error", e.getMessage(),
                    "type", e.getClass().getSimpleName(),
                    "message", "✗ Error generando/validando token"
            ));
        }
    }
}
