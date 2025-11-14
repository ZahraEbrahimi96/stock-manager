package com.example.stockmanager.controller;

import com.example.stockmanager.model.entity.Product;
import com.example.stockmanager.model.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    final private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

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

    @PostMapping("/{id}/add")
    public void addProduct(@PathVariable Long id, @RequestParam int amount) {
        productService.addQuantity(id, amount);
    }

    @PostMapping("/{id}/buy")
    public void buyProduct(@PathVariable Long id, @RequestParam int amount) {
        productService.buyProduct(id, amount);
    }

}
