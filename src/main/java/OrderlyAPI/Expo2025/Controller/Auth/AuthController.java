package OrderlyAPI.Expo2025.Controller.Auth;

import OrderlyAPI.Expo2025.Entities.Usuario.UsuarioEntity;
import OrderlyAPI.Expo2025.Repositories.Empleado.EmpleadoRepository;
import OrderlyAPI.Expo2025.Services.Auth.AuthService;
import OrderlyAPI.Expo2025.Utils.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {

        String correo = body.get("correo");
        String contrasenia = body.get("contrasenia");
        if (correo == null || contrasenia == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Faltan campos"));
        }

        if (!authService.login(correo, contrasenia)) {
            return ResponseEntity.status(401).body(Map.of("error", "Credenciales inválidas"));
        }

        UsuarioEntity user = authService.obtenerUsuario(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String token = jwtUtils.create(
                user.getCorreo(),
                String.valueOf(user.getId()),
                user.getRol().getRol()
        );

        // Detecta si es desarrollo (localhost) o producción
        String origin = request.getHeader("Origin");
        boolean isDev = origin != null && (origin.contains("localhost") || origin.contains("127.0.0.1"));

        // SIEMPRE establece la cookie (para otros clientes)
        ResponseCookie.ResponseCookieBuilder cookieBuilder = ResponseCookie.from("token", token)
                .httpOnly(true)
                .path("/")
                .maxAge(Duration.ofDays(1));

        String setCookie;
        if (isDev) {
            cookieBuilder.secure(false).sameSite("Lax");
            setCookie = cookieBuilder.build().toString();
        } else {
            cookieBuilder.secure(true).sameSite("None");
            setCookie = cookieBuilder.build().toString() + "; Partitioned";
        }

        response.addHeader(HttpHeaders.SET_COOKIE, setCookie);

        // TAMBIÉN devuelve el token en el body para clientes cross-domain
        return ResponseEntity.ok(Map.of(
                "status", "ok",
                "rol", user.getRol().getRol(),
                "correo", user.getCorreo(),
                "token", token  // <--- AÑADIDO
        ));
    }


    // LOGOUT
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        String origin = request.getHeader("Origin");
        boolean isDev = origin != null && (origin.contains("localhost") || origin.contains("127.0.0.1"));

        ResponseCookie.ResponseCookieBuilder cookieBuilder = ResponseCookie.from("token", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0);

        String delCookie;
        if (isDev) {
            cookieBuilder.secure(false).sameSite("Lax");
            delCookie = cookieBuilder.build().toString();
        } else {
            cookieBuilder.secure(true).sameSite("None");
            delCookie = cookieBuilder.build().toString() + "; Partitioned";
        }

        response.addHeader(HttpHeaders.SET_COOKIE, delCookie);
        return ResponseEntity.ok(Map.of("status", "bye"));
    }


    @GetMapping("/me")
    public ResponseEntity<?> me(
            @CookieValue(name = "token", required = false) String cookieToken,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        // Intenta cookie primero, luego header
        String token = cookieToken;
        if ((token == null || token.isBlank()) && authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        if (token == null || token.isBlank()) {
            return ResponseEntity.status(401).body(Map.of("error", "No autenticado - no hay token"));
        }

        try {
            var claims = jwtUtils.parseToken(token);
            String correo = claims.getSubject();

            if (correo == null || correo.isBlank()) {
                return ResponseEntity.status(401).body(Map.of("error", "Token sin subject"));
            }

            String rol = jwtUtils.extractRol(token);

            Optional<UsuarioEntity> userOpt = authService.obtenerUsuario(correo);
            if (userOpt.isEmpty()) {
                return ResponseEntity.status(401).body(Map.of("error", "Usuario no encontrado: " + correo));
            }
            UsuarioEntity user = userOpt.get();

            String username = java.util.Optional.ofNullable(user.getNombreusuario()).orElse("Usuario");
            Long usuarioId = user.getId();

            Long idEmpleado = null;
            try {
                var empleados = user.getUsuario();
                if (empleados != null && !empleados.isEmpty()) {
                    var first = empleados.get(0);
                    if (first != null) idEmpleado = first.getId();
                }
            } catch (Exception ignored) { }

            var responseBody = new java.util.HashMap<String, Object>();
            responseBody.put("correo", correo);
            responseBody.put("rol", rol);
            responseBody.put("username", username);
            responseBody.put("usuarioId", usuarioId);
            if (idEmpleado != null) responseBody.put("idEmpleado", idEmpleado);

            return ResponseEntity.ok()
                    .cacheControl(org.springframework.http.CacheControl.noStore())
                    .body(responseBody);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Token invalido: " + e.getMessage()));
        }
    }
}