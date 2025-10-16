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

        // === ORÍGENES PERMITIDOS ===
        c.setAllowedOriginPatterns(List.of(
                "http://localhost:*",
                "https://localhost:*",
                "http://127.0.0.1:*",
                "https://127.0.0.1:*",
                "http://10.0.2.2:*",
                "https://10.0.2.2:*",
                "http://192.168.*:*",
                "https://192.168.*:*",
                "https://expo-tecnica-orderly-sistema-web-ve.vercel.app",
                "https://orderly-api-b53514e40ebd.herokuapp.com",
                "capacitor://localhost",
                "ionic://localhost",
                "null"
        ));

        // === MÉTODOS PERMITIDOS ===
        c.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        // === CABECERAS PERMITIDAS ===
        c.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "X-Requested-With",
                "Accept",
                "Origin",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers"
        ));

        // === CABECERAS EXPUESTAS ===
        c.setExposedHeaders(List.of(
                "Authorization",
                "Set-Cookie",
                "Access-Control-Allow-Origin",
                "Access-Control-Allow-Credentials"
        ));

        // === CREDENCIALES Y DURACIÓN ===
        c.setAllowCredentials(true);
        c.setMaxAge(3600L);

        // === REGISTRO GLOBAL ===
        UrlBasedCorsConfigurationSource s = new UrlBasedCorsConfigurationSource();
        s.registerCorsConfiguration("/**", c);
        return s;
    }
}
