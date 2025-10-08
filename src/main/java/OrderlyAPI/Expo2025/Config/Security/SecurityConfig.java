package OrderlyAPI.Expo2025.Config.Security;

import OrderlyAPI.Expo2025.Utils.JwtCookieAuthFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.ForwardedHeaderFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtCookieAuthFilter jwtCookieAuthFilter;

    // Constructor injection - Spring lo resuelve automáticamente
    public SecurityConfig(JwtCookieAuthFilter jwtCookieAuthFilter) {
        this.jwtCookieAuthFilter = jwtCookieAuthFilter;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // OPTIONS siempre permitido
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Health check
                        .requestMatchers(HttpMethod.GET, "/", "/actuator/health").permitAll()

                        // Auth endpoints
                        .requestMatchers(HttpMethod.POST, "/api/auth/login", "/api/auth/logout").permitAll()

                        // Recovery endpoints - DEBEN IR ANTES de /auth/**
                        .requestMatchers(HttpMethod.POST,
                                "/auth/recovery/request",
                                "/auth/recovery/verify",
                                "/auth/recovery/reset").permitAll()

                        // Catch-all para /auth/**
                        .requestMatchers("/auth/**").permitAll()

                        // APIs públicos
                        .requestMatchers("/apiReserva/**",
                                "/apiTipoReserva/**",
                                "/apiMesa/**",
                                "/apiPedido/**",
                                "/apiEstadoMesa/**",
                                "/apiEstadoPedido/**",
                                "/apiEstadoReserva/**",
                                "/apiPlatillo/**",
                                "/apiCategoria/**",
                                "/apiEmpleado/**",
                                "/apiDocumentoIdentidad/**",
                                "/apiPersona/**",
                                "/apiHistorialPedido/",
                                "/apiUsuario/**").permitAll()

                        // Debug endpoint (ELIMINAR EN PRODUCCIÓN)
                        .requestMatchers("/debug/**").permitAll()

                        // Todo lo demás requiere autenticación
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, e) -> {
                            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            res.setContentType("application/json");
                            res.getWriter().write("{\"error\": \"No autorizado - Token requerido\"}");
                        })
                        .accessDeniedHandler((req, res, e) -> {
                            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            res.setContentType("application/json");
                            res.getWriter().write("{\"error\": \"Acceso denegado - Permisos insuficientes\"}");
                        })
                )
                .addFilterBefore(jwtCookieAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public ForwardedHeaderFilter forwardedHeaderFilter() {
        return new ForwardedHeaderFilter();
    }
}