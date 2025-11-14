package com.example.stockmanager.model.service;

import com.example.stockmanager.model.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    void save( Product product);
    void delete ( Product  product);
    void deleteById (Long id);
    void update ( Product  product);
    List< Product> findAll();
    Optional<Product> findById (Long id);
    int getQuantityById (Long id);
    void addQuantity (Long id, int amount);
    void buyProduct( Long id, int amount);
}

