package pl.web.Security.Configuration;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pl.web.Repository.UserRepository;
import pl.web.Security.JWT.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer {
    @Autowired
    private JwtAuthenticationFilter jwtFilter;

    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private UserRepository userRepository;
    private final static String[] WHITE_LIST_URL = {
            "/api/user/login",
            "/api/user/register",
            "/api/admin/forgot-password"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(passwordEncoder());
        authProvider.setUserDetailsService(username -> userRepository.findById(Long.valueOf(username)).orElseThrow());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authorizationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll() // Obsługa pre-flight requests
                .requestMatchers(HttpMethod.POST,"/api/user/login", "/api/user/register", "/api/admin/forgot-password").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(); // Włączenie obsługi CORS

        return httpSecurity.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        final String clientHost = "http://localhost:3000/";
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NotNull CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins(clientHost, "*");
                // links with is responsible for operation on users
                registry.addMapping("/api/user/**").allowedOrigins(clientHost);
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