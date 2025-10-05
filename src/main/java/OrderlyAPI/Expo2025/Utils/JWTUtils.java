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
    private String jwtSecreto;                  // 32+ caracteres por seguridad

    @Value("${security.jwt.issuer}")
    private String issuer;                      // Firma del token

    @Value("${security.jwt.expiration}")
    private long expiracionMs;                  // Tiempo de expiración

    private final Logger log = LoggerFactory.getLogger(JWTUtils.class);

    /**
     * Obtiene la clave de firma de forma consistente
     * IMPORTANTE: Este método asegura que siempre se use UTF-8
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecreto.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Metodo para crear JWT
     * @param correo - Email del usuario (será el subject)
     * @param id - ID del usuario
     * @param rol - Rol del usuario
     * @return Token JWT
     */
    public String create(String correo, String id, String rol) {
        // Obtiene la fecha actual y calcula la fecha de expiración
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expiracionMs);

        // Construye el token con sus componentes
        return Jwts.builder()
                .setId(correo)                                          // ID único ahora es el correo
                .setIssuedAt(now)                                       // Fecha de emisión
                .setSubject(correo)                                     // Subject = correo (IMPORTANTE)
                .claim("id", id)                                        // ID de usuario como claim
                .claim("rol", rol)                                      // Rol del usuario
                .setIssuer(issuer)                                      // Emisor del token
                .setExpiration(expiracionMs >= 0 ? expiration : null)   // Expiración (si es >= 0)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)    // Firma con algoritmo HS256
                .compact();                                             // Convierte a String compacto
    }

    /**
     * Extrae el rol del token JWT
     * @param token Token JWT
     * @return Rol del usuario
     */
    public String extractRol(String token) {
        Claims claims = parseToken(token);
        return claims.get("rol", String.class);
    }

    /**
     * Obtiene el subject (correo) del JWT
     * @param jwt Token JWT como String
     * @return String con el subject del token
     */
    public String getValue(String jwt) {
        Claims claims = parseClaims(jwt);
        return claims.getSubject();
    }

    /**
     * Obtiene el ID del JWT
     * @param jwt Token JWT
     * @return ID del token
     */
    public String getKey(String jwt) {
        Claims claims = parseClaims(jwt);
        return claims.getId();
    }

    /**
     * Parsea y valida el token
     * @param jwt Token JWT
     * @return Claims del token
     * @throws ExpiredJwtException Si el token expiró
     * @throws MalformedJwtException Si el token está malformado
     */
    public Claims parseToken(String jwt) throws ExpiredJwtException, MalformedJwtException {
        return parseClaims(jwt);
    }

    /**
     * Validación del token
     * @param token Token a validar
     * @return true si el token es válido, false en caso contrario
     */
    public boolean validate(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Token inválido: {}", e.getMessage());
            return false;
        }
    }

    // ######################## METODOS COMPLEMENTARIOS ########################

    /**
     * Metodo privado para parsear los claims de un JWT
     * CORREGIDO: Ahora usa UTF-8 de forma consistente con create()
     * @param jwt Token a parsear
     * @return Claims del token
     */
    private Claims parseClaims(String jwt) {
        // Configura el parser con la clave de firma y parsea el token
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())  // Usa el mismo método que create()
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }
}