package OrderlyAPI.Expo2025.Utils.Recovery;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.SecureRandom;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Component
public class OtpUtil {
    private final SecureRandom rnd = new SecureRandom();

    @Value("${OTP_PEPPER:}")
    private String pepper;

    public String generarCodigo6() {
        int n = rnd.nextInt(1_000_000);
        return String.format("%06d", n);
    }

    public String hash(String codigo) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] dig = md.digest((pepper + codigo).getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(dig.length * 2);
            for (byte b : dig) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) { throw new RuntimeException(e); }
    }
}
