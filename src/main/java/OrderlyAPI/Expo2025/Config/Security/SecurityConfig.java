package OrderlyAPI.Expo2025.Config.Security;

import OrderlyAPI.Expo2025.Utils.JwtCookieAuthFilter;
import jakarta.servlet.Filter;
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

    public SecurityConfig(JwtCookieAuthFilter jwtCookieAuthFilter) {
        this.jwtCookieAuthFilter = jwtCookieAuthFilter;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CORS debe ir primero
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // ⚠️ CRÍTICO: Permitir OPTIONS para todas las rutas (preflight requests)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Endpoints de autenticación
                        .requestMatchers(HttpMethod.POST, "/api/auth/login", "/api/auth/logout").permitAll()
                        .requestMatchers("/api/auth/me").authenticated()

                        // Endpoints por rol
                        .requestMatchers("/api/test/admin-only").hasRole("Administrador")
                        .requestMatchers("/api/test/cliente-only").hasRole("Cliente")

                        // Registro público
                        .requestMatchers(HttpMethod.POST,
                                "/apiDocumentoIdentidad/createDocumentoIdentidad",
                                "/apiPersona/createPersona",
                                "/apiUsuario/createUsuario",
                                "/apiEmpleado/createEmpleado"
                        ).permitAll()

                        // Endpoints públicos
                        .requestMatchers("/apiReserva/**").permitAll()
                        .requestMatchers("/apiTipoReserva/**").permitAll()
                        .requestMatchers("/apiMesa/**").permitAll()
                        .requestMatchers("/apiPedido/**").permitAll()
                        .requestMatchers("/apiEstadoMesa/**").permitAll()
                        .requestMatchers("/apiEstadoPedido/**").permitAll()
                        .requestMatchers("/apiEstadoReserva/**").permitAll()
                        .requestMatchers("/apiPlatillo/**").permitAll()

                        // Todo lo demás requiere autenticación
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore((Filter) jwtCookieAuthFilter, UsernamePasswordAuthenticationFilter.class);

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