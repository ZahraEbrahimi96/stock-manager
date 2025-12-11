package com.example.stockmanager.model.service;

import com.example.stockmanager.controller.exceptions.OutOfStockException;
import com.example.stockmanager.controller.exceptions.ProductNotFoundException;
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


    public Product save(final Product product) {return productRepository.save(product);
    }

    public void deleteById(final Long id) {
        productRepository.deleteById(id);
    }


    public void update(final Product product) {productRepository.save(product);}


    public List<Product> findAll() {
        return productRepository.findAll();
    }


    public Optional<Product> findById(final Long id) {
        return productRepository.findById(id);
    }


    public int getQuantityById(final Long id) throws ProductNotFoundException {
        return findById(id).map(Product::getQuantity).orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }


    public int getAvailableQuantityById(final Long id) throws ProductNotFoundException {
        return findById(id).map(Product::getAvailableQuantity).orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }


    public void addQuantity(final Long id, final int amount) throws ProductNotFoundException {
        final Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found"));
        product.setQuantity(product.getQuantity() + amount);
        product.setAvailableQuantity(product.getAvailableQuantity() + amount);
        productRepository.save(product);
    }


    public void buyProduct(final Long id, final int amount) throws ProductNotFoundException, OutOfStockException {
        final Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found"));
        if (amount > product.getAvailableQuantity()) {
            throw new OutOfStockException("Insufficient quantity");
        }
        product.setQuantity(product.getQuantity() - amount);
        product.setAvailableQuantity(product.getAvailableQuantity() - amount);
        productRepository.save(product);
    }
}
