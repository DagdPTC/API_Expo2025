package OrderlyAPI.Expo2025.Services.Recuperacion;

import OrderlyAPI.Expo2025.Entities.RecuperacionContrasena.RecuperacionEntity;
import OrderlyAPI.Expo2025.Entities.Usuario.UsuarioEntity;
import OrderlyAPI.Expo2025.Repositories.RecuperaconContrasena.RecuperacionRepository;
import OrderlyAPI.Expo2025.Repositories.Usuario.UsuarioRepository;
import OrderlyAPI.Expo2025.Utils.Recovery.MailService;
import OrderlyAPI.Expo2025.Utils.Recovery.OtpUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecuperacionService {

    private final UsuarioRepository usuarioRepo;
    private final RecuperacionRepository recRepo;
    private final OtpUtil otp;
    private final MailService mail;

    @Value("${OTP_TTL_MIN:10}")
    private int ttlMin;

    @Value("${OTP_MAX_ATTEMPTS:5}")
    private int maxAttempts;

    /* ======= Paso A: solicitar código ======= */
    @Transactional
    public void solicitarCodigo(String correo, String ip) throws Exception {
        // 1) Buscar usuario por correo
        UsuarioEntity u = usuarioRepo.findByCorreoIgnoreCase(correo)
                .orElseThrow(() -> new IllegalArgumentException("El correo no existe"));

        // 2) Throttling simple (máx X por hora)
        int enviados = recRepo.countUltimaHora(u.getId());
        if (enviados >= 5) {
            throw new IllegalStateException("Demasiadas solicitudes. Intenta más tarde.");
        }

        // 3) Generar y hashear código
        String code = otp.generarCodigo6();
        String hash = otp.hash(code);

        // 4) Guardar registro
        RecuperacionEntity r = new RecuperacionEntity();
        r.setUsuarioId(u.getId());
        r.setCodigoHash(hash);
        r.setEstado("PENDIENTE");
        r.setIntentos(0);
        r.setCreadoEn(new Date());
        r.setExpira(Date.from(Instant.now().plus(Duration.ofMinutes(ttlMin))));
        r.setCanal("EMAIL");
        r.setIpSolicitante(ip);
        recRepo.saveAndFlush(r);

        // 5) Enviar correo
        mail.enviarCodigo(correo, code, ttlMin);
    }

    /* ======= Paso B: validar código ======= */
    @Transactional
    public void validarCodigo(String correo, String codigo) {
        // 1) Usuario
        UsuarioEntity u = usuarioRepo.findByCorreoIgnoreCase(correo)
                .orElseThrow(() -> new IllegalArgumentException("Correo no existe"));

        // 2) Último pendiente
        List<RecuperacionEntity> pend = recRepo.findPendientes(u.getId());
        if (pend.isEmpty()) throw new IllegalStateException("No hay código activo");

        RecuperacionEntity r = pend.get(0);

        // 3) Expiración / intentos
        if (new Date().after(r.getExpira())) {
            recRepo.actualizarEstado(r.getIdRecuperacion(), "EXPIRADO");
            throw new IllegalStateException("Código expirado");
        }
        if (r.getIntentos() >= maxAttempts) {
            throw new IllegalStateException("Límite de intentos alcanzado");
        }

        // 4) Comparar hash
        boolean ok = r.getCodigoHash().equals(otp.hash(codigo));
        if (!ok) {
            recRepo.incrementarIntentos(r.getIdRecuperacion());
            throw new IllegalArgumentException("Código incorrecto");
        }

        // 5) Marcar verificado
        recRepo.actualizarEstado(r.getIdRecuperacion(), "VERIFICADO");
    }
}
