package OrderlyAPI.Expo2025.Utils.Recovery;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {
    private final JavaMailSender sender;
    public MailService(JavaMailSender s){ this.sender = s; }

    @Value("${spring.mail.from:${MAIL_FROM:${MAIL_USER}}}")
    private String from;

    @Value("${app.brand.name:Orderly}")
    private String brand;

    public void enviarCodigo(String para, String code, int ttlMin) throws Exception {
        MimeMessage msg = sender.createMimeMessage();
        MimeMessageHelper h = new MimeMessageHelper(msg, "UTF-8");
        h.setFrom(from);
        h.setTo(para);
        h.setSubject("Código de verificación • " + brand);
        h.setText(html(code, ttlMin), true);
        sender.send(msg);
    }

    private String html(String code, int ttlMin) {
        return """
      <div style="font-family:Inter,Arial,sans-serif;background:#f4f7ff;padding:24px">
        <div style="max-width:520px;margin:auto;background:#fff;border-radius:14px;
          box-shadow:0 14px 38px rgba(30,64,175,.15);overflow:hidden">
          <div style="background:linear-gradient(90deg,#234fe0,#2b5aeb);padding:18px 22px;color:#fff">
            <strong style="font-size:16px">Orderly</strong>
          </div>
          <div style="padding:22px">
            <h2 style="margin:0 0 8px 0;color:#0f172a;font-size:18px">Código de verificación</h2>
            <p style="color:#334155;margin:0 0 14px">Usa este código para recuperar tu cuenta.</p>
            <div style="font-size:28px;letter-spacing:8px;font-weight:700;color:#234fe0;text-align:center;
                        padding:14px 8px;background:#f1f4ff;border-radius:12px">%s</div>
            <p style="color:#475569;margin:16px 0 0">
              Caduca en <strong>%d minutos</strong>. Si no solicitaste este código, ignora este correo.
            </p>
          </div>
        </div>
      </div>
    """.formatted(code, ttlMin);
    }
}
