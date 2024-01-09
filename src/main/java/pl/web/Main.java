package pl.web;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class Main {
    private final static String clientHost = "http://localhost:3000";

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NotNull CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins(clientHost);
                // links with is responsible for operation on users
                registry.addMapping("/api/user/users").allowedOrigins(clientHost);
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
}
