package com.example.stockmanager.model.service.serviceImp;

import com.example.stockmanager.model.entity.Product;
import com.example.stockmanager.model.repository.ProductRepository;
import com.example.stockmanager.model.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImp implements ProductService {
    final private ProductRepository productRepository;

    public ProductServiceImp(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void save(Product product) {
        productRepository.save(product);
    }

    @Override
    public void delete(Product product) {
        productRepository.delete(product);
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public void update(Product product) {
        productRepository.save(product);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public int getQuantityById(Long id) {
        return findById(id).map(Product::getQuantity).orElseThrow(()-> new RuntimeException("Product not found"));
    }

    @Override
    @Transactional
    public void addQuantity(Long id, int amount) {
        Product product = productRepository.findById(id).orElseThrow(()-> new RuntimeException("Product not found"));
        product.setQuantity(product.getQuantity() + amount);
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void buyProduct(Long id, int amount) {
        Product product = productRepository.findById(id).orElseThrow(()-> new RuntimeException("Product not found"));
        if (amount > product.getQuantity()){
            throw new RuntimeException("Insufficient quantity");
        }
            product.setQuantity(product.getQuantity() - amount);
            productRepository.save(product);
    }



}
