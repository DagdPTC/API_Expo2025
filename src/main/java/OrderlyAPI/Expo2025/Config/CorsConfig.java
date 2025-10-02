package OrderlyAPI.Expo2025.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

    private CorsConfiguration buildConfig() {
        CorsConfiguration c = new CorsConfiguration();

        // Para desarrollo: permitir localhost (con y sin puerto) y 127.0.0.1
        c.setAllowedOriginPatterns(List.of(
                "http://localhost",
                "http://localhost:*",
                "http://127.0.0.1",
                "http://127.0.0.1:*",
                "https://orderly-api-b53514e40ebd.herokuapp.com"
                // Si sirves el front estático desde otro dominio, agrégalo aquí.
                // Ej: "https://tu-frontend.com", "https://*.netlify.app"
        ));

        c.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        c.setAllowedHeaders(List.of("Content-Type","Authorization","X-Requested-With","Accept","Origin"));
        c.setExposedHeaders(List.of("Set-Cookie"));
        c.setAllowCredentials(true);       // Necesario para cookies
        c.setMaxAge(3600L);                // cache preflight 1h
        return c;
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig());
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE); // que corra antes que Spring Security
        return bean;
    }
}
