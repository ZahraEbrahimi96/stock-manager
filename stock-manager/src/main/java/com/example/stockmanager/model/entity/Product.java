package com.example.stockmanager.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity(name = "productEntity")
@Table(name = "product-tbl")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int quantity;
    private int price;
}
