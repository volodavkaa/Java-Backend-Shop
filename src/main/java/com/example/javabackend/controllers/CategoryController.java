package com.example.javabackend.controllers;

import com.example.javabackend.entities.Category;
import com.example.javabackend.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryRepository categoryRepository;

    // Цей ендпоінт доступний для всіх авторизованих користувачів
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
