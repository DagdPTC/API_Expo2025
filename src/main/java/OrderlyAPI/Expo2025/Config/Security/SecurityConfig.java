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
                .cors(c -> {})          // habilita CORS
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Permitir preflight
                        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()

                        // Auth
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/auth/login", "/api/auth/logout").permitAll()
                        .requestMatchers("/api/auth/me").authenticated()

                        .requestMatchers("/api/test/admin-only").hasRole("Administrador")
                        .requestMatchers("/api/test/cliente-only").hasRole("Cliente")

                        // Públicos (rápido para que todo GET funcione)
                        .requestMatchers("/apiReserva/**").permitAll()
                        .requestMatchers("/apiTipoReserva/**").permitAll()
                        .requestMatchers("/apiMesa/**").permitAll()
                        .requestMatchers("/apiEmpleado/**").permitAll()        // <-- AÑADIDO
                        .requestMatchers("/apiPedido/**").permitAll()          // si así ya te funcionó pedidos
                        .requestMatchers("/apiEstadoMesa/**").permitAll()
                        .requestMatchers("/apiEstadoPedido/**").permitAll()
                        .requestMatchers("/apiEstadoReserva/**").permitAll()
                        .requestMatchers("/apiPlatillo/**").permitAll()

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
