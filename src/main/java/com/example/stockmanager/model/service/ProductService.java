package com.example.stockmanager.model.service;

import com.example.stockmanager.model.entity.Product;
import com.example.stockmanager.model.entity.ProductReservation;
import com.example.stockmanager.model.repository.ProductRepository;
import com.example.stockmanager.model.repository.ProductReservationRepository;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {

    @Resource
    private ProductRepository productRepository;


    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }


    public void update(Product product) {productRepository.save(product);}


    public List<Product> findAll() {
        return productRepository.findAll();
    }


    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }


    public int getQuantityById(Long id) {
        return findById(id).map(Product::getQuantity).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public int getAvailableQuantityById(Long id) {
        return findById(id).map(Product::getAvailableQuantity).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public void addQuantity(Long id, int amount) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setQuantity(product.getQuantity() + amount);
        productRepository.save(product);
    }

    //TODO: CHECK
    public void buyProduct(Long id, int amount) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        if (amount > product.getAvailableQuantity()) {
            throw new RuntimeException("Insufficient quantity");
        }
        product.setQuantity(product.getQuantity() - amount);
        productRepository.save(product);
    }
}
