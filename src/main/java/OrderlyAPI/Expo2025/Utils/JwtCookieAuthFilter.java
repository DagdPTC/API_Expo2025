package OrderlyAPI.Expo2025.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Filtro JWT:
 * - Normaliza la URI (remueve context-path) antes de chequear rutas públicas.
 * - SOLO son públicos: POST /api/auth/login (y opcional POST /api/auth/logout).
 * - /api/auth/me NO es público: requiere cookie y autenticación.
 * - Cookie usada: "authToken".
 */
public class JwtCookieAuthFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtCookieAuthFilter.class);

    private static final String AUTH_COOKIE_NAME = "authToken";
    private final JWTUtils jwtUtils;

    public JwtCookieAuthFilter(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 1) Rutas públicas + preflight
        if (isPublicEndpoint(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 2) Token desde cookie
            String token = extractToken(request);
            if (token == null || token.isBlank()) {
                sendError(response, "Token no encontrado", HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // 3) Validar token y construir Authentication
            Claims claims = jwtUtils.parseToken(token);
            String rol = jwtUtils.extractRol(token);

            Collection<? extends GrantedAuthority> authorities =
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + rol));

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);

            SecurityContextHolder.getContext().setAuthentication(auth);

            // 4) Continuar
            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            log.warn("Token expirado: {}", e.getMessage());
            sendError(response, "Token expirado", HttpServletResponse.SC_UNAUTHORIZED);
        } catch (MalformedJwtException e) {
            log.warn("Token malformado: {}", e.getMessage());
            sendError(response, "Token inválido", HttpServletResponse.SC_FORBIDDEN);
        } catch (Exception e) {
            log.error("Error de autenticación", e);
            sendError(response, "Error de autenticación", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private boolean isPublicEndpoint(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String ctx = request.getContextPath();
        if (ctx != null && !ctx.isEmpty() && uri.startsWith(ctx)) {
            uri = uri.substring(ctx.length());
        }
        String method = request.getMethod();
        if ("OPTIONS".equalsIgnoreCase(method)) return true;
        if ("/api/auth/login".equals(uri) && "POST".equalsIgnoreCase(method)) return true;
        if ("/api/auth/logout".equals(uri) && "POST".equalsIgnoreCase(method)) return true;

        // ---------- PÚBLICOS ----------
        if (uri.startsWith("/apiReserva")) return true;
        if (uri.startsWith("/apiTipoReserva")) return true;
        if (uri.startsWith("/apiMesa")) return true;
        if (uri.startsWith("/apiPedido")) return true;           // <-- NUEVO
        if (uri.startsWith("/apiEstadoMesa")) return true;       // (labels)
        if (uri.startsWith("/apiEstadoPedido")) return true;
        if (uri.startsWith("/apiEstadoReserva")) return true;
        if (uri.startsWith("/apiPlatillo")) return true;
        // ------------------------------

        return false;
    }



    private String extractTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        return Arrays.stream(cookies)
                .filter(c -> AUTH_COOKIE_NAME.equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    private void sendError(HttpServletResponse response, String message, int status) throws IOException {
        response.setContentType("application/json");
        response.setStatus(status);
        response.getWriter().write(String.format("{\"error\":\"%s\",\"status\":%d}", message, status));
    }

    // NUEVO
    private String extractToken(HttpServletRequest request) {
        // 1) Cookie primero
        String token = extractTokenFromCookies(request);
        if (token != null && !token.isBlank()) return token;

        // 2) Fallback: Authorization: Bearer <jwt>
        String auth = request.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {
            return auth.substring(7);
        }
        return null;
    }

}
