package OrderlyAPI.Expo2025.Controller.Recuperacion;

import OrderlyAPI.Expo2025.Services.Recuperacion.RecuperacionService;
import lombok.RequiredArgsConstructor;
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
                                     @RequestHeader(value = "X-Forwarded-For", required = false) String xff) throws Exception {
        String correo = body.getOrDefault("correo","").trim();
        if (correo.isEmpty()) return ResponseEntity.badRequest().body(Map.of("error","Correo requerido"));
        String ip = (xff != null && !xff.isBlank()) ? xff.split(",")[0].trim() : "0.0.0.0";
        service.solicitarCodigo(correo, ip);
        return ResponseEntity.ok(Map.of("ok", true, "message", "Código enviado"));
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody Map<String,String> body) {
        String correo = body.getOrDefault("correo","").trim();
        String codigo = body.getOrDefault("codigo","").trim();
        if (correo.isEmpty() || codigo.isEmpty())
            return ResponseEntity.badRequest().body(Map.of("error","Datos incompletos"));
        service.validarCodigo(correo, codigo);
        return ResponseEntity.ok(Map.of("ok", true));
    }
}
