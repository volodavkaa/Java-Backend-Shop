package com.example.javabackend.config;

import com.example.javabackend.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;  // важливо: для withDefaults()
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Імпорти для CORS
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Увімкнути обробку CORS, використовуючи дефолтну конфігурацію
                .cors(Customizer.withDefaults())
                // Вимкнути CSRF (у REST API зазвичай не потрібен)
                .csrf(csrf -> csrf.disable())
                // Налаштування дозволів
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/categories", "/api/categories/**").permitAll()
                        .anyRequest().authenticated()
                )
                // Додаємо JWT-фільтр
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Налаштування CORS (Bean, який використовуємо в .cors(withDefaults()))
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Дозволяємо звернення з фронтенду на 5173
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        // Дозволяємо методи
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Дозволяємо будь-які заголовки
        configuration.setAllowedHeaders(List.of("*"));
        // Дозволити надсилати cookie чи інші credentials
        configuration.setAllowCredentials(true);

        // Реєструємо для всіх шляхів (/**)
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
