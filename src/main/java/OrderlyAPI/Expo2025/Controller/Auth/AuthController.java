package OrderlyAPI.Expo2025.Controller.Auth;

import OrderlyAPI.Expo2025.Entities.Usuario.UsuarioEntity;
import OrderlyAPI.Expo2025.Services.Auth.AuthService;
import OrderlyAPI.Expo2025.Utils.JWTUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JWTUtils jwtUtils;

    public AuthController(AuthService authService, JWTUtils jwtUtils) {
        this.authService = authService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body, HttpServletResponse response) {
        String correo = body.get("correo");
        String contrasenia = body.get("contrasenia");
        if (correo == null || contrasenia == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Faltan campos"));
        }

        boolean ok = authService.login(correo, contrasenia);
        if (!ok) return ResponseEntity.status(401).body(Map.of("error", "Credenciales inválidas"));

        Optional<UsuarioEntity> userOpt = authService.obtenerUsuario(correo);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "Usuario no encontrado"));
        }
        UsuarioEntity user = userOpt.get();

        String token = jwtUtils.create(
                String.valueOf(user.getId()),
                user.getCorreo(),
                user.getRol().getRol()
        );

        Cookie cookie = new Cookie("authToken", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // en local http -> false (en prod https -> true + SameSite=None)
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24); // 1 día
        response.addCookie(cookie);

        return ResponseEntity.ok(Map.of(
                "status", "ok",
                "rol", user.getRol().getRol(),
                "correo", user.getCorreo()
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("authToken", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // prod https: true
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok(Map.of("status", "bye"));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(@CookieValue(name = "authToken", required = false) String token) {
        if (token == null || token.isBlank()) {
            return ResponseEntity.status(401).body(Map.of("error", "No autenticado"));
        }
        try {
            var claims = jwtUtils.parseToken(token);
            String correo = claims.getSubject();
            String rol = jwtUtils.extractRol(token);
            return ResponseEntity.ok(Map.of("correo", correo, "rol", rol));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Token inválido"));
        }
    }
}
