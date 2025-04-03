package com.example.javabackend.controllers;

import com.example.javabackend.entities.User;
import com.example.javabackend.repositories.UserRepository;
import com.example.javabackend.security.JwtService;   // Імпортуємо наш JwtService
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;      // Додаємо JwtService

    // Невеликі DTO для запитів
    public record RegisterRequest(String email, String password) {}
    public record LoginRequest(String email, String password) {}

    // 1) Реєстрація
    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {

        // Перевірити, чи email не зайнятий
        if (userRepository.existsByEmail(request.email())) {
            return "Email already in use!";
        }

        // Хешуємо пароль
        String hashedPassword = passwordEncoder.encode(request.password());

        User user = new User();
        user.setEmail(request.email());
        user.setPassword(hashedPassword);
        user.setRole("ROLE_USER"); // при реєстрації всі отримують ROLE_USER

        // Зберігаємо в БД
        userRepository.save(user);

        return "User registered successfully!";
    }

    // 2) Логін
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("User not found"));
        boolean matches = passwordEncoder.matches(request.password(), user.getPassword());
        if (!matches) {
            return "Invalid password!";
        }
        // Генеруємо токен з урахуванням і email, і role
        String token = jwtService.generateToken(user.getEmail(), user.getRole());
        return token;
    }

}
