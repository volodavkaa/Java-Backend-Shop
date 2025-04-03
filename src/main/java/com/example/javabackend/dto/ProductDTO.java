package com.example.javabackend.dto;

import lombok.Data;
import java.util.List;

@Data
public class ProductDTO {
    private String title;
    private String description;
    private Double price;
    private Long categoryId; // ID категорії, до якої відноситься продукт
    private List<String> photoUrls; // або інший спосіб для фото
}
