package com.example.javabackend.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_photos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
