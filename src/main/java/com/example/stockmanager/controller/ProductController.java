package com.example.stockmanager.controller;

import com.example.stockmanager.model.entity.Product;
import com.example.stockmanager.model.service.ProductReservationService;
import com.example.stockmanager.model.service.ProductService;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Resource
    private ProductService productService;

    @Resource
    private ProductReservationService productReservationService;


    @GetMapping
    public List<Product> getAllProducts() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/quantity")
    public int getQuantityById(@PathVariable Long id) {
        return productService.getQuantityById(id);
    }

    @GetMapping("/{id}/availableQuantity")
    public int getAvailableQuantityById(@PathVariable Long id) {
        return productService.getAvailableQuantityById(id);
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.save(product);
    }

    @PostMapping("/{id}/reserve")
    public ResponseEntity<String> reserveProduct(@PathVariable Long id,
                                                 @RequestParam int amount,
                                                 @RequestParam (defaultValue = "1") int hour) {
        productReservationService.save(id, amount, hour);
        return ResponseEntity.ok("Product Reserved");
    }

    @PutMapping("/{id}/add")
    public void addProduct(@PathVariable Long id, @RequestParam int amount) {
        productService.addQuantity(id, amount);
    }

    @PutMapping("/{id}/buy")
    public void buyProduct(@PathVariable Long id, @RequestParam int amount) {
        productService.buyProduct(id, amount);
    }

    @DeleteMapping
    public void deleteProductById(@PathVariable Long id) {
        productService.deleteById(id);
    }

    @DeleteMapping("/{id}/cancel")
    public void cancelReservationById(@PathVariable Long id) {
        productReservationService.deleteById(id);
    }



}
