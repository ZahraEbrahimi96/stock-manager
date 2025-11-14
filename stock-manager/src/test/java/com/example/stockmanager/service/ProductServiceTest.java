package com.example.stockmanager.service;

import com.example.stockmanager.model.entity.Product;
import com.example.stockmanager.model.repository.ProductRepository;
import com.example.stockmanager.model.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProductServiceTest {

//    private final ProductService productService;
//    private final ProductRepository productRepository;
//
//    public ProductServiceTest(ProductService productService, ProductRepository productRepository) {
//        this.productService = productService;
//        this.productRepository = productRepository;
//    }
//
//    private Product testProduct;
//
//    @BeforeEach
//    void setUp() {
//        productRepository.deleteAll();
//        testProduct = Product.builder().name("book").price(78).quantity(12).build();
//        productService.save(testProduct);
//    }
//
//    @Test
//    void getAllProducts() {
//
//        Product product = Product.builder().name("phone").price(965).quantity(10).build();
//        productService.save(product);
//        List<Product> products = productService.findAll();
//        assertEquals(2, products.size());
//        assertTrue(products.stream().anyMatch(p -> p.getName().equals("book")),
//                "Should contain book");
//        assertTrue(products.stream().anyMatch(p -> p.getName().equals("phone")),
//                "Should contain phone");
//    }
//
//    @Test
//    void getProductById() {
//        Optional<Product> product = productService.findById(testProduct.getId());
//
//        assertTrue(product.isPresent());
//        assertEquals(testProduct.getId(), product.get().getId());
//        assertEquals("book", product.get().getName());
//        assertEquals(965, product.get().getPrice(), "Product price should match");
//    }
//
//    @Test
//    void getProductById_notFound() {
//        Optional<Product> product = productService.findById(999L);
//        assertFalse(product.isPresent(), "Product should not be found");
//    }
//
//    @Test
//    void getQuantityById() {
//        int stock = productService.getQuantityById(testProduct.getId());
//
//        assertEquals(12, stock, "Stock should be 12");
//    }
//
//    @Test
//    void getQuantityById_notFound() {
//        Exception exception = assertThrows(RuntimeException.class,
//                () -> productService.getQuantityById(999L)
//        );
//    }
//
//    @Test
//    void addQuantity() {
//        productService.addQuantity(testProduct.getId(), 50);
//
//        Product updatedProduct = productRepository.findById(testProduct.getId()).get();
//        assertEquals(62, updatedProduct.getQuantity());
//    }
//
//    @Test
//    void buyProduct_success() {
//        productService.buyProduct(testProduct.getId(), 5);
//
//        Product updatedProduct = productRepository.findById(testProduct.getId()).get();
//        assertEquals(7, updatedProduct.getQuantity(), "Stock should be decreased by 5");
//    }
//
//    @Test
//    void buyProduct_insufficientQuantity() {
//        Exception exception = assertThrows(RuntimeException.class,
//                () -> productService.buyProduct(testProduct.getId(), 150),
//                "Should throw exception for insufficient quantity"
//        );
//
//        assertEquals("Insufficient quantity", exception.getMessage(), "Exception message should match");
//        Product product = productRepository.findById(testProduct.getId()).get();
//        assertEquals(12, product.getQuantity(), "Stock should remain unchanged");
//    }


}
