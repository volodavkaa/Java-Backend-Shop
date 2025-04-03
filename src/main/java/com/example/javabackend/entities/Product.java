package com.example.javabackend.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;         // Заголовок продукту
    private String description;   // Опис продукту
    private Double price;

    // Встановлюємо зв’язок з категорією
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // Для фото, як було запропоновано раніше, можна зробити окрему сутність ProductPhoto
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductPhoto> photos = new ArrayList<>();

    public void addPhoto(ProductPhoto photo) {
        photos.add(photo);
        photo.setProduct(this);
    }
}
