package com.example.javabackend.controllers;

import com.example.javabackend.dto.ProductDTO;
import com.example.javabackend.entities.Product;
import com.example.javabackend.repositories.ProductRepository;
import com.example.javabackend.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductService productService;

    // 1) Get all products
    @GetMapping
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    // 2) Get product by ID
    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id=" + id));
    }

    // 3) Create product (POST) – використовуємо DTO
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductDTO productDTO) {
        Product createdProduct = productService.createProduct(productDTO);
        return ResponseEntity.ok(createdProduct);
    }

    // 4) Update product (PUT)
    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @RequestBody Product updatedProduct) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id=" + id));

        // Використовуємо поля title та description замість name
        existing.setTitle(updatedProduct.getTitle());
        existing.setDescription(updatedProduct.getDescription());
        existing.setPrice(updatedProduct.getPrice());
        // За потреби, додайте обробку інших полів

        return productRepository.save(existing);
    }

    // 5) Delete product (DELETE)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productRepository.deleteById(id);
    }
}
