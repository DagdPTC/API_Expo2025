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

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(JwtCookieAuthFilter.class);

    @Value("${security.jwt.secret}")
    private String jwtSecret;

    @Value("${security.jwt.issuer:}")
    private String jwtIssuer;

    private static final List<String> PUBLIC_PATHS = List.of(
            "/api/auth/login",
            "/api/auth/logout",
            "/auth/**",  // CRÍTICO: Incluye /auth/recovery/**
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
            "/", "/actuator/health"
    );

    private final AntPathMatcher matcher = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) return true;

        String uri = request.getRequestURI();

        // DEBUG: Log explícito para ver qué está pasando
        boolean shouldSkip = PUBLIC_PATHS.stream().anyMatch(p -> matcher.match(p, uri));

        log.info("shouldNotFilter: URI={}, shouldSkip={}", uri, shouldSkip);

        if (shouldSkip) {
            log.info("✓ Path {} coincide con rutas públicas - OMITIENDO filtro JWT", uri);
        } else {
            log.warn("✗ Path {} NO coincide - REQUIERE autenticación", uri);
        }

        return shouldSkip;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {

        String uri = request.getRequestURI();
        String method = request.getMethod();

        log.info("JwtCookieAuthFilter.doFilterInternal: {} {}", method, uri);

        String token = extractToken(request);

        if (token == null || token.isBlank()) {
            log.warn("⚠ No se encontró token para: {} - Rechazando request", uri);
            // IMPORTANTE: No continuar la cadena si no hay token en ruta protegida
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Token requerido\"}");
            return;
        }

        log.info("Token encontrado: {}...", token.substring(0, Math.min(20, token.length())));

        try {
            Authentication auth = buildAuthenticationFromJwt(token, request);
            if (auth != null) {
                log.info("✓ Autenticación exitosa: {}", auth.getName());
                org.springframework.security.core.context.SecurityContextHolder.getContext()
                        .setAuthentication(auth);
            } else {
                log.error("✗ buildAuthenticationFromJwt retornó null");
            }
            chain.doFilter(request, response);

        } catch (Exception ex) {
            log.error("✗ Error parseando token: {}", ex.getMessage(), ex);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Token inválido\"}");
        }
    }

    private String extractToken(HttpServletRequest req) {
        // 1. Buscar en cookies
        if (req.getCookies() != null) {
            Optional<Cookie> c = Arrays.stream(req.getCookies())
                    .filter(k -> "token".equals(k.getName()) || "jwt".equals(k.getName()) || "jwt-token".equals(k.getName()))
                    .findFirst();
            if (c.isPresent()) {
                String val = c.get().getValue();
                if (val != null && !val.isBlank()) {
                    log.info("✓ Token encontrado en cookie");
                    return val;
                }
            }
        }

        // 2. Buscar en header Authorization
        String h = req.getHeader("Authorization");
        if (h != null) {
            log.info("Authorization header presente: {}...", h.substring(0, Math.min(30, h.length())));
        }

        if (h != null && h.startsWith("Bearer ")) {
            String val = h.substring(7);
            if (!val.isBlank()) {
                log.info("✓ Token encontrado en Authorization header");
                return val;
            }
        }

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
        if (rolesObj == null) {
            rolesObj = claims.get("rol");
        }

        if (rolesObj == null) {
            log.warn("No se encontraron roles en el token");
            return Collections.emptyList();
        }

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