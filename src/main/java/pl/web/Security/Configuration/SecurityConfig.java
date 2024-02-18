package pl.web.Security.Configuration;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pl.web.Repository.UserRepository;
import pl.web.Security.JWT.TokenFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    public SecurityConfig(TokenFilter tokenFilter, UserRepository repository) {
        this.tokenFilter = tokenFilter;
        this.repository = repository;
    }

    private final UserRepository repository;
    private final TokenFilter tokenFilter;

    private AuthenticationProvider authenticationProvider;

    @Bean
    // Function that configuring permission to connect with server
    public WebMvcConfigurer corsConfigurer() {
        final String clientHost = "http://localhost:3000";
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
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> repository.findById(Long.valueOf(username))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    // Function that configuring of the password encoding type
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}