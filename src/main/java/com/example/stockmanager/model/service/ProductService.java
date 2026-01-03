package com.example.stockmanager.model.service;

import com.example.stockmanager.controller.exceptions.OutOfStockException;
import com.example.stockmanager.controller.exceptions.ProductNotFoundException;
import com.example.stockmanager.model.entity.Product;
import com.example.stockmanager.model.repository.ProductRepository;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class ProductService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    @Resource
    private ProductRepository productRepository;


    public Product save(final Product product) {return productRepository.save(product);
    }

    public void deleteById(final Long id) {
        productRepository.deleteById(id);
    }


    public void update(final Product product) {productRepository.save(product);}


    public List<Product> findAll() {
        final List<Product> allProducts = productRepository.findAll();
        String a = "test";
         a = a + "ok";
        allProducts.forEach(p -> {
//            if (LOGGER.isDebugEnabled()) {
//                LOGGER.debug("Product is" + p.getName());
//            }
                LOGGER.debug("Product is {}", p.getName());
        });
        return allProducts;
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


    public Product addQuantity(final Long id, final int amount) throws ProductNotFoundException {
        final Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException());
        product.setQuantity(product.getQuantity() + amount);
        product.setAvailableQuantity(product.getAvailableQuantity() + amount);
        return productRepository.save(product);
    }


    public Product buyProduct(final Long id, final int amount) throws ProductNotFoundException, OutOfStockException {
        final Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found"));
        if (amount > product.getAvailableQuantity()) {
            throw new OutOfStockException("Insufficient quantity");
        }
        product.setQuantity(product.getQuantity() - amount);
        product.setAvailableQuantity(product.getAvailableQuantity() - amount);
        return productRepository.save(product);
    }

    public Optional<Product> addTenStockToProduct(final Long id) throws ProductNotFoundException {
       try {
           return Optional.of(addQuantity(id, 10));
       } catch(ProductNotFoundException e) {
           // LOGGER.info("Product with " + id + " id not found");
           LOGGER.info("Product with {} id not found", id);//BEST PRACTICE
           LOGGER.info("Product with {} id not found", id, e);//WILL SHOW STACKTRACE
           // throw e; //OPTIONAL, VERY COMMON
       }
        return Optional.empty();
    }
}
