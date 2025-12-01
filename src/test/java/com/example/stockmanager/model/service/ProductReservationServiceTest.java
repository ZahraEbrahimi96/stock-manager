package com.example.stockmanager.model.service;

import com.example.stockmanager.model.entity.Product;
import com.example.stockmanager.model.entity.ProductReservation;
import com.example.stockmanager.model.repository.ProductRepository;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ProductReservationServiceTest {

    @Resource
    private ProductReservationService productReservationService;

    @Resource
    private ProductService productService;

    @Resource
    private ProductRepository  productRepository;



    @Test
    void reserveAndAutoRelease() throws Exception {
        Product testProduct = new Product();
        testProduct.setName("book");
        testProduct.setQuantity(12);
        testProduct.setPrice(78);
        productService.save(testProduct);

        ProductReservation p = productReservationService.save(testProduct.getId(),5,6);

        assertEquals(12, testProduct.getQuantity());
        assertEquals(7, testProduct.getAvailableQuantity());
        assertNotNull(p.getExpiresAt());
    }
}
