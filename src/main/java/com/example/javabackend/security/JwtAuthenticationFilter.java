package com.example.javabackend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    // Якщо потрібно доступ до юзерів (наприклад, для перевірки ролей), можна розкоментувати:
    // private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // 1) Витягуємо заголовок Authorization
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Якщо немає токена або він не починається з "Bearer ", продовжуємо фільтр без аутентифікації
            filterChain.doFilter(request, response);
            return;
        }

        // 2) Витягти сам JWT
        String jwt = authHeader.substring(7); // "Bearer ".length() == 7
        String email;
        String role = null;
        try {
            // Витягуємо email із токена
            email = jwtService.extractUsername(jwt);
            // Витягуємо кастомний claim "role"
            role = jwtService.extractClaim(jwt, claims -> claims.get("role", String.class));
        } catch (Exception e) {
            // Якщо сталася помилка під час парсингу (наприклад, токен прострочений або некоректний)
            filterChain.doFilter(request, response);
            return;
        }

        // 3) Перевіряємо, що користувача ще не аутентифіковано в SecurityContext
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // 4) Валідація: перевірити, чи токен ще дійсний
            if (jwtService.validateToken(jwt, email)) {
                // 5) Створюємо список GrantedAuthority, додамо роль, якщо вона існує
                List<GrantedAuthority> authorities = new ArrayList<>();
                if (role != null) {
                    authorities.add(new SimpleGrantedAuthority(role));
                }
                // Створюємо об'єкт аутентифікації з email та встановленими ролями (authority)
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(email, null, authorities);
                // Прописуємо його в SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // Продовжуємо обробку запиту
        filterChain.doFilter(request, response);
    }
}
