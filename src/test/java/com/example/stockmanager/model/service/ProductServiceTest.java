package com.example.stockmanager.model.service;

import com.example.stockmanager.model.entity.Product;
import com.example.stockmanager.model.repository.ProductRepository;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest

public class ProductServiceTest {

    @Resource
    private ProductService productService;

    @Resource
    private ProductRepository productRepository;



    private Product testProduct;


    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
//        Product testProduct = new Product();
//        testProduct.setName("book");
//        testProduct.setQuantity(12);
//        testProduct.setPrice(78);
//       productService.save(testProduct);
    }



    @Test
    void getAllProducts() {

        Product testProduct = new Product();
        testProduct.setName("book");
        testProduct.setQuantity(12);
        testProduct.setPrice(78);
        productService.save(testProduct);

        Product product =  new Product();
        product.setName("phone");
        product.setQuantity(965);
        product.setPrice(10);
        productRepository.save(product);
        productService.save(product);

        List<Product> products = productService.findAll();
        assertEquals(2, products.size());
        assertTrue(products.stream().anyMatch(p -> p.getName().equals("book")),
                "Should contain book");
        assertTrue(products.stream().anyMatch(p -> p.getName().equals("phone")),
                "Should contain phone");
    }

    @Test
    void getProductById() {
        Product testProduct = new Product();
        testProduct.setName("book");
        testProduct.setQuantity(12);
        testProduct.setPrice(78);
        productService.save(testProduct);

        Optional<Product> product = productService.findById(testProduct.getId());

        assertTrue(product.isPresent());
        assertEquals(testProduct.getId(), product.get().getId());
        assertEquals("book", product.get().getName());
        assertEquals(78, product.get().getPrice());
    }

    @Test
    void getProductById_notFound() {
        Product testProduct = new Product();
        testProduct.setName("book");
        testProduct.setQuantity(12);
        testProduct.setPrice(78);
        productService.save(testProduct);

        Optional<Product> product = productService.findById(999L);
        assertFalse(product.isPresent(), "Product should not be found");
    }

    @Test
    void getQuantityById() {

        Product testProduct = new Product();
        testProduct.setName("book");
        testProduct.setQuantity(12);
        testProduct.setPrice(78);
        productService.save(testProduct);

        int stock = productService.getQuantityById(testProduct.getId());
        assertEquals(12, stock, "Stock should be 12");
    }

    @Test
    void getQuantityById_notFound() {

        Product testProduct = new Product();
        testProduct.setName("book");
        testProduct.setQuantity(12);
        testProduct.setPrice(78);
        productService.save(testProduct);

        Exception exception = assertThrows(RuntimeException.class,
                () -> productService.getQuantityById(999L)
        );
    }

    @Test
    void getAvailableQuantityById() {
        Product testProduct = new Product();
        testProduct.setName("book");
        testProduct.setQuantity(12);
        testProduct.setPrice(78);
        productService.save(testProduct);

        int stock = productService.getAvailableQuantityById(testProduct.getId());
        assertEquals(12, stock, "Stock should be 12");
    }

    @Test
    void getAvailableQuantityById_notFound() {

        Product testProduct = new Product();
        testProduct.setName("book");
        testProduct.setQuantity(12);
        testProduct.setPrice(78);
        productService.save(testProduct);

        Exception exception = assertThrows(RuntimeException.class,
                () -> productService.getAvailableQuantityById(999L)
        );
    }

    @Test
    void addQuantity() {

        Product testProduct = new Product();
        testProduct.setName("book");
        testProduct.setQuantity(12);
        testProduct.setPrice(78);
        productService.save(testProduct);

        productService.addQuantity(testProduct.getId(), 50);

        Product updatedProduct = productRepository.findById(testProduct.getId()).get();
        assertEquals(62, updatedProduct.getQuantity());
        assertEquals(62, updatedProduct.getAvailableQuantity());
    }

    @Test
    void buyProduct_success() {
        Product testProduct = new Product();
        testProduct.setName("book");
        testProduct.setQuantity(12);
        testProduct.setPrice(78);
        productService.save(testProduct);

        productService.buyProduct(testProduct.getId(),6);

        assertEquals(6, testProduct.getQuantity(), "Stock should be decreased by 5");
        assertEquals(6, testProduct.getAvailableQuantity());
    }

    @Test
    void buyProduct_insufficientQuantity() {

        Product testProduct = new Product();
        testProduct.setName("book");
        testProduct.setQuantity(12);
        testProduct.setPrice(78);
        productService.save(testProduct);

        Exception exception = assertThrows(RuntimeException.class,
                () -> productService.buyProduct(testProduct.getId(), 150),
                "Should throw exception for insufficient quantity"
        );

        assertEquals("Insufficient quantity", exception.getMessage(), "Exception message should match");
        Product product = productRepository.findById(testProduct.getId()).get();
        assertEquals(12, product.getAvailableQuantity(), "Stock should remain unchanged");
    }


}
