package com.example.stockmanager.controller;

import com.example.stockmanager.controller.exceptions.OutOfStockException;
import com.example.stockmanager.controller.exceptions.ProductNotFoundException;
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
    public ResponseEntity<Product> getProductById(@PathVariable final Long id) {
        return productService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/quantity")
    public int getQuantityById(@PathVariable final Long id) throws ProductNotFoundException {
        return productService.getQuantityById(id);
    }

    @GetMapping("/{id}/availableQuantity")
    public int getAvailableQuantityById(@PathVariable final Long id) throws ProductNotFoundException {
        return productService.getAvailableQuantityById(id);
    }

    @PostMapping
    public Product createProduct(@RequestBody final Product product) {
        return productService.save(product);
    }

    @PostMapping("/{id}/reserve")
    public ResponseEntity<String> reserveProduct(@PathVariable final Long id,
                                                 @RequestParam final int amount,
                                                 @RequestParam (defaultValue = "1") final int hour) throws OutOfStockException, ProductNotFoundException {
        productReservationService.save(id, amount, hour);
        return ResponseEntity.ok("Product Reserved");
    }

    @PutMapping("/{id}/add")
    public void addProduct(@PathVariable final Long id, @RequestParam final int amount) throws ProductNotFoundException {
        productService.addQuantity(id, amount);
    }

    @PutMapping("/{id}/buy")
    public void buyProduct(@PathVariable final Long id, @RequestParam final int amount) throws OutOfStockException, ProductNotFoundException {
        productService.buyProduct(id, amount);
    }

    @DeleteMapping("/{id}/remove")
    public void deleteProductById(@PathVariable final Long id) {
        productService.deleteById(id);
    }

    @DeleteMapping("/{id}/cancel")
    public void cancelReservationById(@PathVariable final Long id) {
        productReservationService.deleteById(id);
    }

    @DeleteMapping
    public void releaseExpiredProduct(){
        productReservationService.deleteAllExpired();
    }

}
