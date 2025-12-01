package com.example.stockmanager.model.service;

import com.example.stockmanager.model.entity.Product;
import com.example.stockmanager.model.entity.ProductReservation;
import com.example.stockmanager.model.repository.ProductRepository;
import com.example.stockmanager.model.repository.ProductReservationRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ProductReservationService {

    @Resource
    private ProductReservationRepository productReservationRepository;
    @Resource
    private ProductRepository productRepository;



    public ProductReservation save(Long productId, int amount, int hours) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

        if (amount > product.getAvailableQuantity()) {
            throw new RuntimeException("Amount is greater than available quantity");
        }
        product.setAvailableQuantity(product.getAvailableQuantity() - amount);
        productRepository.save(product);

        ProductReservation productReservation = new ProductReservation();
        productReservation.setProduct(product);
        productReservation.setExpiresAt(LocalDateTime.now().plusHours(hours));
        productReservation.setReservedQuantity(amount);
        return productReservationRepository.save(productReservation);
    }

    public void deleteById(Long id){
        ProductReservation productReservation = productReservationRepository.findById(id).get();
        Product product = productReservationRepository.findById(id).get().getProduct();
        product.setAvailableQuantity((product.getAvailableQuantity() + productReservation.getReservedQuantity()));
        productRepository.save(product);

        productReservationRepository.deleteById(id);
    }

    public ProductReservation findById(Long id){
        return productReservationRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public List<ProductReservation> findAll(){
        return productReservationRepository.findAll();
    }

}
