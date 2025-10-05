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

    @Value("${security.jwt.secret}")
    private String jwtSecret;

    @Value("${security.jwt.issuer:}")
    private String jwtIssuer; // opcional

    // JwtCookieAuthFilter.java  (solo la lista)
    private static final List<String> PUBLIC_PATHS = List.of(
            // auth abierto necesario
            "/api/auth/login",
            "/api/auth/logout",

            // recuperación de contraseña
            "/auth/**",

            // endpoints públicos que ya tienes
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
            "/apiCategoria/**",

            // health/root
            "/", "/actuator/health"
    );


    private final AntPathMatcher matcher = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
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

        String token = extractToken(request);

        if (token == null || token.isBlank()) {
            chain.doFilter(request, response);
            return;
        }

        try {
            Authentication auth = buildAuthenticationFromJwt(token, request);
            if (auth != null) {
                org.springframework.security.core.context.SecurityContextHolder.getContext()
                        .setAuthentication(auth);
            }
            chain.doFilter(request, response);

        } catch (Exception ex) {
            // IMPORTANTE: no responder 401 aquí.
            // Deja que la cadena de seguridad decida (si la ruta es pública,
            // pasará; si es protegida, devolverá 401 más adelante).
            chain.doFilter(request, response);
        }
    }

    private String extractToken(HttpServletRequest req) {
        if (req.getCookies() != null) {
            Optional<Cookie> c = Arrays.stream(req.getCookies())
                    .filter(k -> "token".equals(k.getName()) || "jwt".equals(k.getName()) || "jwt-token".equals(k.getName()))
                    .findFirst();
            if (c.isPresent()) return c.get().getValue();
        }
        String h = req.getHeader("Authorization");
        if (h != null && h.startsWith("Bearer ")) return h.substring(7);
        return null;
    }

    private Authentication buildAuthenticationFromJwt(String token, HttpServletRequest request) {
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        var parser = Jwts.parserBuilder().setSigningKey(key);
        if (jwtIssuer != null && !jwtIssuer.isBlank()) parser.requireIssuer(jwtIssuer);

        Claims claims = parser.build().parseClaimsJws(token).getBody();

        String subject = claims.getSubject();
        if (subject == null || subject.isBlank()) return null;

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
            roles = Arrays.stream(s.split(",")).map(String::trim).filter(r -> !r.isEmpty()).toList();
        } else if (rolesObj instanceof Collection<?> col) {
            roles = col.stream().map(String::valueOf).toList();
        } else {
            roles = List.of(String.valueOf(rolesObj));
        }
        return roles.stream()
                .filter(r -> !r.isBlank())
                .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }
}
