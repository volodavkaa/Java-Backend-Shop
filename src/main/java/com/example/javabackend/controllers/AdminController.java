package com.example.javabackend.controllers;

import com.example.javabackend.entities.User;
import com.example.javabackend.entities.Announcement;
import com.example.javabackend.repositories.UserRepository;
import com.example.javabackend.repositories.AnnouncementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final AnnouncementRepository announcementRepository;

    // --- Управління користувачами ---

    // Отримати список користувачів
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Отримати користувача за id
    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id=" + id));
    }

    // Редагувати користувача (наприклад, змінити роль)
    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id=" + id));
        existing.setRole(updatedUser.getRole());
        return userRepository.save(existing);
    }


}

