package OrderlyAPI.Expo2025.Controller.Auth;

import OrderlyAPI.Expo2025.Entities.Usuario.UsuarioEntity;
import OrderlyAPI.Expo2025.Models.DTO.UsuarioDTO;
import OrderlyAPI.Expo2025.Services.Auth.AuthService;
import OrderlyAPI.Expo2025.Utils.JWTUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @Autowired
    private JWTUtils jwtUtils;

    @PostMapping("/login")
    private ResponseEntity<String> login(@Valid @RequestBody UsuarioDTO data, HttpServletResponse response){
        if (data.getCorreo() == null || data.getCorreo().isBlank() ||
                data.getContrasenia() == null || data.getContrasenia().isBlank()) {
            return ResponseEntity.status(401).body("Error: Credenciales incompletas");
        }

        if (service.login(data.getCorreo(), data.getContrasenia())){
            addTokenCookie(response, data.getCorreo());
            return ResponseEntity.ok("Inicio de sesion exitoso");
        }
        return ResponseEntity.status(401).body("Credenciales incorrectas");
    }

    private void addTokenCookie(jakarta.servlet.http.HttpServletResponse response, String correo) {
        // Obtener el usuario completo de la base de datos
        Optional<UsuarioEntity> userOpt = service.obtenerUsuario(correo);

        if (userOpt.isPresent()) {
            UsuarioEntity user = userOpt.get();
            String token = jwtUtils.create(
                    String.valueOf(user.getId()),
                    user.getCorreo(),
                    user.getRol().getRol() // ← Usar el nombre real del tipo
            );

            jakarta.servlet.http.Cookie cookie = new Cookie("authToken", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(86400);
            response.addCookie(cookie);
        }
    }

}
