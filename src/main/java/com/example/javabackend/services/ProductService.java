package com.example.javabackend.services;

import com.example.javabackend.dto.ProductDTO;
import com.example.javabackend.entities.Category;
import com.example.javabackend.entities.Product;
import com.example.javabackend.entities.ProductPhoto;
import com.example.javabackend.repositories.CategoryRepository;
import com.example.javabackend.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public Product createProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setTitle(productDTO.getTitle());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());

        // Завантаження категорії за ID
        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found with id=" + productDTO.getCategoryId()));
        product.setCategory(category);

        // Додаємо фото, якщо є список URL
        if (productDTO.getPhotoUrls() != null) {
            productDTO.getPhotoUrls().forEach(url -> {
                ProductPhoto photo = new ProductPhoto();
                photo.setUrl(url);
                product.addPhoto(photo);
            });
        }

        return productRepository.save(product);
    }
}
