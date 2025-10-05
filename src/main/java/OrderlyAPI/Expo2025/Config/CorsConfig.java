package OrderlyAPI.Expo2025.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration c = new CorsConfiguration();

        // Orígenes permitidos
        c.setAllowedOriginPatterns(List.of(
                "http://localhost",
                "http://localhost:*",
                "http://127.0.0.1",
                "http://127.0.0.1:*",
                "https://orderly-api-b53514e40ebd.herokuapp.com",
                "null"
        ));

        c.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
        ));

        c.setAllowedHeaders(List.of("*"));

        c.setExposedHeaders(List.of(
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials",
                "Set-Cookie",
                "Authorization"
        ));

        c.setAllowCredentials(true);
        c.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource s = new UrlBasedCorsConfigurationSource();
        s.registerCorsConfiguration("/**", c);
        return s;
    }
}