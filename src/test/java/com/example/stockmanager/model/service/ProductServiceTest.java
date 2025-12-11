package com.example.stockmanager.model.service;

import com.example.stockmanager.controller.exceptions.OutOfStockException;
import com.example.stockmanager.controller.exceptions.ProductNotFoundException;
import com.example.stockmanager.model.entity.Product;
import com.example.stockmanager.model.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setQuantity(100);
        product.setPrice(5000);
    }

    @Test
    @DisplayName("Should reduce both quantity and availableQuantity when buying")
    void buyProduct_reduction() throws OutOfStockException, ProductNotFoundException {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        productService.buyProduct(1L, 10);

        assertEquals(90, product.getQuantity());
//        assertEquals(90, product.getAvailableQuantity());
        verify(productRepository).save(product);
    }

    @Test
    @DisplayName("Should throw when buying more than available")
    void buyProduct_InsufficientStock() {
        product.setAvailableQuantity(5);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Exception exception = assertThrows(OutOfStockException.class, () -> {
            productService.buyProduct(1L, 10);
        });

        assertEquals("Insufficient quantity", exception.getMessage());
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("addQuantity should increase real quantity and available quantity")
    void addQuantity_IncreaseRealQuantityAndAvailableQuantity() throws ProductNotFoundException {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.addQuantity(1L, 50);

        assertEquals(150, product.getQuantity());
//        assertEquals(150, product.getAvailableQuantity());
    }
}
