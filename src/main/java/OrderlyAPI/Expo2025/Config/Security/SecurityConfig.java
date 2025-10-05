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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.ForwardedHeaderFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtCookieAuthFilter jwtCookieAuthFilter;

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
                        // ============ CRÍTICO: OPTIONS PRIMERO ============
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // ============ RUTAS PÚBLICAS (ORDEN IMPORTA) ============
                        .requestMatchers(HttpMethod.GET, "/", "/actuator/health").permitAll()

                        // Auth endpoints (login/logout)
                        .requestMatchers(HttpMethod.POST, "/api/auth/login", "/api/auth/logout").permitAll()

                        // ⭐ RECUPERACIÓN - DEBE IR ANTES DE /auth/** GENÉRICO
                        .requestMatchers(HttpMethod.POST, "/auth/recovery/request").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/recovery/verify").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/recovery/reset").permitAll()

                        // Cualquier otra ruta bajo /auth/**
                        .requestMatchers("/auth/**").permitAll()

                        // ============ API ENDPOINTS PÚBLICOS ============
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
                                "/apiUsuario/**").permitAll()

                        // ============ TODO LO DEMÁS REQUIERE AUTH ============
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

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*")); // Producción: especifica tus dominios
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}