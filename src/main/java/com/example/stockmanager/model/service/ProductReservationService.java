package com.example.stockmanager.model.service;

import com.example.stockmanager.controller.exceptions.OutOfStockException;
import com.example.stockmanager.controller.exceptions.ProductNotFoundException;
import com.example.stockmanager.model.entity.Product;
import com.example.stockmanager.model.entity.ProductReservation;
import com.example.stockmanager.model.repository.ProductRepository;
import com.example.stockmanager.model.repository.ProductReservationRepository;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Scheduled;
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



    public ProductReservation save(final Long productId, final int amount, final int hours) throws ProductNotFoundException, OutOfStockException {
        final Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Product not found"));

        if (amount > product.getAvailableQuantity()) {
            throw new OutOfStockException("Amount is greater than available quantity");
        }
        product.setAvailableQuantity(product.getAvailableQuantity() - amount);
        productRepository.save(product);

        final ProductReservation productReservation = new ProductReservation();
        productReservation.setProduct(product);
        productReservation.setExpiresAt(LocalDateTime.now().plusHours(hours));
        productReservation.setReservedQuantity(amount);
        return productReservationRepository.save(productReservation);
    }

    public void deleteById(final Long id){
        productReservationRepository.findById(id).ifPresent(productReservation -> {
            final Product product = productReservation.getProduct();
            product.setAvailableQuantity((product.getAvailableQuantity() + productReservation.getReservedQuantity()));
            productRepository.save(product);
            productReservationRepository.deleteById(id);
        });
    }

    public ProductReservation findById(final Long id) throws ProductNotFoundException {
        return productReservationRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    public List<ProductReservation> findAll(){
        return productReservationRepository.findAll();
    }

    @Scheduled(fixedRate = 60000)
    public void deleteAllExpired (){
        productReservationRepository.deleteAllByExpiresAtBefore(LocalDateTime.now().minusMinutes(5));
    }
}
