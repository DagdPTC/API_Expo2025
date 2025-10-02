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
        c.setAllowedOriginPatterns(List.of(
                "http://localhost:*",
                "http://127.0.0.1:*",
                // agrega aquí tu front en producción si aplica, por ejemplo:
                // "https://tu-frontend.com",
                // "https://*.netlify.app",
                // "https://*.github.io"
                // Si alguna vez sirves el front desde file://, podrías añadir "null"
                // pero es mejor NO usar file:// en desarrollo.
                "null"
        ));
        c.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        c.setAllowedHeaders(List.of("Content-Type","Authorization","X-Requested-With"));
        c.setAllowCredentials(true); // imprescindible para cookies
        c.setMaxAge(3600L);          // cachea preflights 1h

        UrlBasedCorsConfigurationSource s = new UrlBasedCorsConfigurationSource();
        s.registerCorsConfiguration("/**", c);
        return s;
    }

}
