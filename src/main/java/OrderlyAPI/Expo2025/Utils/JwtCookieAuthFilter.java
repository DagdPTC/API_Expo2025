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
            String token = extractTokenFromCookies(request);
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

    /** Quita el context-path y evalúa si la ruta es pública. */
    private boolean isPublicEndpoint(HttpServletRequest request) {
        String uri = request.getRequestURI();      // p.ej. /Expo2025/api/auth/login
        String ctx = request.getContextPath();     // p.ej. /Expo2025 (o "")
        if (ctx != null && !ctx.isEmpty() && uri.startsWith(ctx)) {
            uri = uri.substring(ctx.length());     // queda /api/auth/login
        }
        String method = request.getMethod();

        log.info("Evaluando ruta: {} | Método: {}", uri, method);

        // Preflight CORS
        if ("OPTIONS".equalsIgnoreCase(method)) return true;

        // Rutas realmente públicas (login/logout)
        if ("/api/auth/login".equals(uri) && "POST".equalsIgnoreCase(method)) return true;
        if ("/api/auth/logout".equals(uri) && "POST".equalsIgnoreCase(method)) return true;

        // Endpoints abiertos por GET para el dashboard sin login
        if ("GET".equalsIgnoreCase(method)) {
            if (uri.startsWith("/apiMesa"))          return true;
            if (uri.startsWith("/apiReserva"))       return true;
            if (uri.startsWith("/apiTipoReserva"))   return true;
            if (uri.startsWith("/apiPedido"))        return true;
            if (uri.startsWith("/apiEstadoMesa"))    return true;
            if (uri.startsWith("/apiEstadoPedido"))  return true;
            if (uri.startsWith("/apiEstadoReserva")) return true;
            if (uri.startsWith("/apiPlatillo"))      return true;
        }

        // /api/auth/me NO es público
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
}
