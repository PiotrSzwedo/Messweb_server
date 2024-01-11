package pl.web.Security.Configuration;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig {

    private final static String clientHost = "http://localhost:3000";

    @Bean
    // Function that configuring permission to connect with server
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NotNull CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins(clientHost);
                // links with is responsible for operation on users
                registry.addMapping("/api/user/register").allowedOrigins(clientHost);
                registry.addMapping("/api/user/login").allowedOrigins(clientHost);
                registry.addMapping("/api/user/user-delete").allowedOrigins(clientHost);
                registry.addMapping("/api/user/admin-autorize").allowedOrigins(clientHost);
                //changing data
                registry.addMapping("/api/user/user-data-change").allowedOrigins(clientHost);
                //downloand data
                registry.addMapping("/api/user/user-data").allowedOrigins(clientHost);
                registry.addMapping("/api/user/user-data-id").allowedOrigins(clientHost);
                registry.addMapping("/api/user/user").allowedOrigins(clientHost);
                registry.addMapping("/settings").allowedOrigins(clientHost);
                // links with is responsible for operation on posts
                registry.addMapping("/posts").allowedOrigins(clientHost);
                registry.addMapping("/post-id").allowedOrigins(clientHost);
                registry.addMapping("/post-title").allowedOrigins(clientHost);
                registry.addMapping("/post-add").allowedOrigins(clientHost);
                registry.addMapping("/post-delete").allowedOrigins(clientHost);
                registry.addMapping("/post-owner").allowedOrigins(clientHost);
            }
        };
    }

    @Bean
    // Function that configuring of the password encoding type
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}