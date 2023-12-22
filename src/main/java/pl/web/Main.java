package pl.web;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pl.web.Entity.User;
import pl.web.Repository.UserRepository;

import java.util.List;

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
                // links with is responsible for operation on users
                registry.addMapping("/users").allowedOrigins(clientHost);
                registry.addMapping("/login").allowedOrigins(clientHost);
                registry.addMapping("/user-delete").allowedOrigins(clientHost);
                //changing data
                registry.addMapping("/user-password").allowedOrigins(clientHost);
                registry.addMapping("/user-name").allowedOrigins(clientHost);
                registry.addMapping("/user-email").allowedOrigins(clientHost);
                //downloand data
                registry.addMapping("/user-data").allowedOrigins(clientHost);
                registry.addMapping("/user-data-id").allowedOrigins(clientHost);
                registry.addMapping("/user").allowedOrigins(clientHost);
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
