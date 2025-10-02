package OrderlyAPI.Expo2025.Controller.Auth;

import OrderlyAPI.Expo2025.Entities.Usuario.UsuarioEntity;
import OrderlyAPI.Expo2025.Entities.Empleado.EmpleadoEntity;
import OrderlyAPI.Expo2025.Repositories.Empleado.EmpleadoRepository;
import OrderlyAPI.Expo2025.Services.Auth.AuthService;
import OrderlyAPI.Expo2025.Utils.JWTUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import java.time.Duration;


import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JWTUtils jwtUtils;
    private final EmpleadoRepository empleadoRepository;

    public AuthController(AuthService authService, JWTUtils jwtUtils, EmpleadoRepository empleadoRepository) {
        this.authService = authService;
        this.jwtUtils = jwtUtils;
        this.empleadoRepository = empleadoRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body, HttpServletResponse response, HttpServletRequest request) {
        String correo = body.get("correo");
        String contrasenia = body.get("contrasenia");
        if (correo == null || contrasenia == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Faltan campos"));
        }

        boolean ok = authService.login(correo, contrasenia);
        if (!ok) return ResponseEntity.status(401).body(Map.of("error", "Credenciales inválidas"));

        var userOpt = authService.obtenerUsuario(correo);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "Usuario no encontrado"));
        }
        var user = userOpt.get();

        String token = jwtUtils.create(
                String.valueOf(user.getId()),
                user.getCorreo(),
                user.getRol().getRol()
        );

        // IMPORTANTE: cookie cross-site para Heroku
        ResponseCookie cookie = ResponseCookie.from("authToken", token)
                .httpOnly(true)
                .secure(true)              // Heroku es HTTPS → Secure obligatorio con SameSite=None
                .sameSite("None")          // permite enviar cookie en requests cross-site (fetch)
                .path("/")
                .maxAge(Duration.ofDays(1))// alínealo a tu expiración JWT si quieres
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(Map.of(
                "status", "ok",
                "rol", user.getRol().getRol(),
                "correo", user.getCorreo()
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        ResponseCookie clear = ResponseCookie.from("authToken", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, clear.toString());
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

            Optional<UsuarioEntity> userOpt = authService.obtenerUsuario(correo);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(401).body(Map.of("error", "Usuario no encontrado"));
            }
            UsuarioEntity user = userOpt.get();

            String username = java.util.Optional.ofNullable(user.getNombreusuario()).orElse("Usuario");
            Long usuarioId = user.getId();

            // Busca un IdEmpleado asociado al usuario (si existe)
            Long idEmpleado = null;
            try {
                var empleados = user.getUsuario(); // List<EmpleadoEntity>
                if (empleados != null && !empleados.isEmpty()) {
                    var first = empleados.get(0);
                    if (first != null) idEmpleado = first.getId();
                }
            } catch (Exception ignored) { }

            var body = new java.util.HashMap<String, Object>();
            body.put("correo", correo);
            body.put("rol", rol);
            body.put("username", username);
            body.put("usuarioId", usuarioId);
            if (idEmpleado != null) body.put("idEmpleado", idEmpleado);

            return ResponseEntity.ok()
                    .cacheControl(org.springframework.http.CacheControl.noStore())
                    .body(body);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Token inválido"));
        }
    }
}
