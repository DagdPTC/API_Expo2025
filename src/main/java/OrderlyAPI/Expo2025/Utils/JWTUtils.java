package OrderlyAPI.Expo2025.Utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtils {

    @Value("${security.jwt.secret}")
    private String jwtSecreto;

    @Value("${security.jwt.issuer}")
    private String issuer;

    @Value("${security.jwt.expiration}")
    private long expiracionMs;

    private final Logger log = LoggerFactory.getLogger(JWTUtils.class);

    /**
     * CRÍTICO: Obtiene la clave de firma de forma consistente
     * Sanitiza el secret para evitar problemas con espacios
     */
    private SecretKey getSigningKey() {
        // Sanitizar el secret: eliminar espacios y asegurar UTF-8
        String cleanSecret = jwtSecreto.trim();

        // Log solo en desarrollo (eliminar en producción)
        log.debug("Secret length: {}", cleanSecret.length());

        return Keys.hmacShaKeyFor(cleanSecret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Crea JWT con el correo como subject
     */
    public String create(String correo, String id, String rol) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expiracionMs);

        String token = Jwts.builder()
                .setId(correo)
                .setIssuedAt(now)
                .setSubject(correo)
                .claim("id", id)
                .claim("rol", rol)
                .setIssuer(issuer)
                .setExpiration(expiracionMs >= 0 ? expiration : null)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

        log.info("Token generado para usuario: {}", correo);
        return token;
    }

    /**
     * Extrae el rol del token JWT
     */
    public String extractRol(String token) {
        Claims claims = parseToken(token);
        return claims.get("rol", String.class);
    }

    /**
     * Obtiene el subject (correo) del JWT
     */
    public String getValue(String jwt) {
        Claims claims = parseClaims(jwt);
        return claims.getSubject();
    }

    /**
     * Obtiene el ID del JWT
     */
    public String getKey(String jwt) {
        Claims claims = parseClaims(jwt);
        return claims.getId();
    }

    /**
     * Parsea y valida el token
     */
    public Claims parseToken(String jwt) throws ExpiredJwtException, MalformedJwtException {
        return parseClaims(jwt);
    }

    /**
     * Validación del token
     */
    public boolean validate(String token) {
        try {
            parseClaims(token);
            log.debug("Token validado exitosamente");
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("Token expirado: {}", e.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            log.warn("Token malformado: {}", e.getMessage());
            return false;
        } catch (JwtException e) {
            log.error("Error validando token: {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            log.warn("Token inválido o vacío");
            return false;
        }
    }

    /**
     * CRÍTICO: Parsea claims usando la misma clave que create()
     */
    private Claims parseClaims(String jwt) {
        try {
            // Sanitizar el token (eliminar espacios)
            String cleanToken = jwt.trim();

            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())  // Misma clave que create()
                    .build()
                    .parseClaimsJws(cleanToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.warn("Token expirado para subject: {}", e.getClaims().getSubject());
            throw e;
        } catch (SignatureException e) {
            log.error("Firma inválida - El secret puede no coincidir");
            throw e;
        } catch (Exception e) {
            log.error("Error parseando token: {}", e.getMessage());
            throw e;
        }
    }
}