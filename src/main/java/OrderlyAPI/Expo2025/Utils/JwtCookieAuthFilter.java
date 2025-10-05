package OrderlyAPI.Expo2025.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtCookieAuthFilter extends OncePerRequestFilter {

    // ==== Configuración de validación del JWT ====
    @Value("${security.jwt.secret}")
    private String jwtSecret;

    @Value("${security.jwt.issuer:}")
    private String jwtIssuer; // opcional

    // ==== Rutas públicas: este filtro NO se aplica ====
    private static final List<String> PUBLIC_PATHS = List.of(
            // auth abierto
            "/auth/**",          // <--- todo recovery, ping, reset, etc.
            "/api/auth/**",      // <--- si usas /api/auth/login|logout

            // endpoints públicos existentes
            "/apiDocumentoIdentidad/**",
            "/apiPersona/**",
            "/apiUsuario/**",
            "/apiEmpleado/**",
            "/apiReserva/**",
            "/apiTipoReserva/**",
            "/apiMesa/**",
            "/apiPedido/**",
            "/apiEstadoMesa/**",
            "/apiEstadoPedido/**",
            "/apiEstadoReserva/**",
            "/apiPlatillo/**",
            "/apiCategoria/**"
    );


    private final AntPathMatcher matcher = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // No filtrar preflight
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) return true;

        String uri = request.getRequestURI();
        return PUBLIC_PATHS.stream().anyMatch(p -> matcher.match(p, uri));
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {

        // 1) intentar extraer token (cookie "token"/"jwt" o Authorization: Bearer ...)
        String token = extractToken(request);

        // 2) Si NO hay token => continuar como anónimo (NO 401)
        if (token == null || token.isBlank()) {
            chain.doFilter(request, response);
            return;
        }

        try {
            // 3) Validar y construir Authentication
            Authentication auth = buildAuthenticationFromJwt(token, request);

            // Si el token fue válido, setear contexto
            if (auth != null) {
                org.springframework.security.core.context.SecurityContextHolder.getContext()
                        .setAuthentication(auth);
            }

            // 4) Continuar la cadena
            chain.doFilter(request, response);

        } catch (Exception ex) {
            // 5) Solo si venía token pero es inválido => 401
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Token inválido\"}");
        }
    }

    // ====== Helpers ======

    private String extractToken(HttpServletRequest req) {
        // Cookie
        if (req.getCookies() != null) {
            Optional<Cookie> c = Arrays.stream(req.getCookies())
                    .filter(k -> "token".equals(k.getName()) || "jwt".equals(k.getName()))
                    .findFirst();
            if (c.isPresent()) return c.get().getValue();
        }
        // Header Authorization
        String h = req.getHeader("Authorization");
        if (h != null && h.startsWith("Bearer ")) return h.substring(7);
        return null;
    }

    private Authentication buildAuthenticationFromJwt(String token, HttpServletRequest request) {
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        var parser = Jwts.parserBuilder().setSigningKey(key);
        if (jwtIssuer != null && !jwtIssuer.isBlank()) {
            parser.requireIssuer(jwtIssuer);
        }

        Claims claims = parser.build().parseClaimsJws(token).getBody();

        String subject = claims.getSubject(); // email/username
        if (subject == null || subject.isBlank()) return null;

        // Roles opcionales en claim "roles" (String "ADMIN,USER" o List)
        Collection<SimpleGrantedAuthority> auths = extractAuthorities(claims);

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(subject, null, auths);
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return auth;
    }

    @SuppressWarnings("unchecked")
    private Collection<SimpleGrantedAuthority> extractAuthorities(Claims claims) {
        Object rolesObj = claims.get("roles");
        if (rolesObj == null) return Collections.emptyList();

        List<String> roles;
        if (rolesObj instanceof String s) {
            roles = Arrays.stream(s.split(","))
                    .map(String::trim)
                    .filter(r -> !r.isEmpty())
                    .toList();
        } else if (rolesObj instanceof Collection<?> col) {
            roles = col.stream().map(String::valueOf).toList();
        } else {
            roles = List.of(String.valueOf(rolesObj));
        }

        // Spring Security espera "ROLE_X"
        return roles.stream()
                .filter(r -> !r.isBlank())
                .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }
}
