package OrderlyAPI.Expo2025.Utils;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtCookieAuthFilter extends OncePerRequestFilter {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(JwtCookieAuthFilter.class);

    // CRÍTICO: Usar JWTUtils en lugar de duplicar lógica
    private final JWTUtils jwtUtils;

    // Constructor injection
    public JwtCookieAuthFilter(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    private static final List<String> PUBLIC_PATHS = List.of(
            "/api/auth/login",
            "/api/auth/logout",
            "/auth/recovery/request",
            "/auth/recovery/verify",
            "/auth/recovery/reset",
            "/auth/**",
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
            "/debug/**",
            "/", "/actuator/health"
    );

    private final AntPathMatcher matcher = new AntPathMatcher();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) return true;

        String uri = request.getRequestURI();
        boolean shouldSkip = PUBLIC_PATHS.stream().anyMatch(p -> matcher.match(p, uri));
        log.info("shouldNotFilter: URI={}, shouldSkip={}", uri, shouldSkip);
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
            // No hay token: NO cortar. Deja que Spring Security aplique permitAll()/auth.
            log.info("Sin token para {} {} - continuando cadena de filtros", method, uri);
            chain.doFilter(request, response);
            return;
        }

        log.info("Token encontrado (primeros 30 chars): {}...",
                token.substring(0, Math.min(30, token.length())));

        try {
            Authentication auth = buildAuthenticationFromJwt(token, request);
            if (auth != null) {
                log.info("✓ Autenticación exitosa: {}", auth.getName());
                org.springframework.security.core.context.SecurityContextHolder.getContext()
                        .setAuthentication(auth);
            } else {
                log.error("✗ buildAuthenticationFromJwt retornó null");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Token inválido\"}");
                return;
            }
            chain.doFilter(request, response);

        } catch (io.jsonwebtoken.ExpiredJwtException ex) {
            log.error("✗ Token expirado: {}", ex.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Token expirado\"}");
        } catch (io.jsonwebtoken.security.SignatureException ex) {
            log.error("✗ Firma del token no coincide - Posible problema con JWT_SECRET");
            log.error("   Mensaje: {}", ex.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Token inválido\"}");
        } catch (Exception ex) {
            log.error("✗ Error parseando token: {}", ex.getMessage(), ex);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Token inválido\"}");
        }
    }

    private String extractToken(HttpServletRequest req) {
        // CRÍTICO: Priorizar Authorization header sobre cookies
        // Esto evita que cookies corruptas interfieran

        // 1. Buscar en header Authorization PRIMERO
        String h = req.getHeader("Authorization");
        if (h != null && h.startsWith("Bearer ")) {
            String val = h.substring(7);
            if (!val.isBlank()) {
                log.info("✓ Token encontrado en Authorization header (prioridad alta)");
                return val.trim();
            }
        }

        // 2. Solo si no hay header, buscar en cookies
        if (req.getCookies() != null) {
            Optional<Cookie> c = Arrays.stream(req.getCookies())
                    .filter(k -> "token".equals(k.getName()) ||
                            "jwt".equals(k.getName()) ||
                            "jwt-token".equals(k.getName()))
                    .findFirst();
            if (c.isPresent()) {
                String val = c.get().getValue();
                if (val != null && !val.isBlank()) {
                    log.info("✓ Token encontrado en cookie (fallback)");
                    return val.trim();
                }
            }
        }

        log.warn("✗ No se encontró token en Authorization header ni en cookies");
        return null;
    }

    /**
     * CRÍTICO: Usa JWTUtils.parseToken() para garantizar misma lógica
     */
    private Authentication buildAuthenticationFromJwt(String token, HttpServletRequest request) {
        try {
            // Usar JWTUtils en lugar de duplicar lógica
            Claims claims = jwtUtils.parseToken(token);

            String subject = claims.getSubject();
            if (subject == null || subject.isBlank()) {
                log.error("Token sin subject");
                return null;
            }

            log.info("Token parseado - Subject: {}, Issuer: {}",
                    subject, claims.getIssuer());

            Collection<SimpleGrantedAuthority> auths = extractAuthorities(claims);

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(subject, null, auths);
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            return auth;
        } catch (Exception e) {
            log.error("Error en buildAuthenticationFromJwt: {}", e.getMessage());
            throw e;
        }
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
            roles = Arrays.stream(s.split(","))
                    .map(String::trim)
                    .filter(r -> !r.isEmpty())
                    .toList();
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