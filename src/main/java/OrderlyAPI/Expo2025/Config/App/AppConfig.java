package OrderlyAPI.Expo2025.Config.App;

import OrderlyAPI.Expo2025.Utils.JWTUtils;
import OrderlyAPI.Expo2025.Utils.JwtCookieAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public JwtCookieAuthFilter jwtCookieAuthFilter(JWTUtils jwtUtils){
        return new JwtCookieAuthFilter(jwtUtils);
    }
}
