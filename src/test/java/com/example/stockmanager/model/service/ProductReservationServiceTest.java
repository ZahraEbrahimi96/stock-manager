package com.example.stockmanager.model.service;

import com.example.stockmanager.controller.exceptions.OutOfStockException;
import com.example.stockmanager.controller.exceptions.ProductNotFoundException;
import com.example.stockmanager.model.entity.Product;
import com.example.stockmanager.model.entity.ProductReservation;
import com.example.stockmanager.model.repository.ProductRepository;
import com.example.stockmanager.model.repository.ProductReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class ProductReservationServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductReservationRepository productReservationRepository;

    @InjectMocks
    private ProductReservationService reservationService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setQuantity(100);
        product.setAvailableQuantity(100);
        product.setPrice(999);
    }

    @Test
    @DisplayName("successful reservation")
    void successful_reservation() throws ProductNotFoundException, OutOfStockException {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        ProductReservation reservation = reservationService.save(1L, 6, 5);

        verify(productReservationRepository).save(reservation);
        assertNotNull(reservation);
        assertEquals(5,reservation.getReservedQuantity());
        assertEquals(product,reservation.getProduct());
        assertTrue(reservation.getExpiresAt().isAfter(LocalDateTime.now()));
        assertTrue(reservation.getExpiresAt().isBefore(LocalDateTime.now().plusHours(5)));

        verify(productRepository).save(product);
        assertEquals(950, product.getAvailableQuantity());
    }

    @Test
    @DisplayName("unsuccessful reservation - insufficient stock")
    void unsuccessful_reservation_insufficient_stock() throws ProductNotFoundException, OutOfStockException {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        OutOfStockException exception = assertThrows(OutOfStockException.class, () -> {
            reservationService.save(1L, 110, 1);
        });

        assertEquals("Amount is greater than available quantity", exception.getMessage());
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("unsuccessful reservation - product not found")
    void unsuccessful_reservation_productNotFound() throws ProductNotFoundException, OutOfStockException {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            reservationService.save(5L, 10, 1);
        });

        assertEquals("Product not found", exception.getMessage());
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("cancel reservation")
    void cancel_reservation() throws ProductNotFoundException, OutOfStockException {
        ProductReservation reservation = new ProductReservation();
        reservation.setProduct(product);
        reservation.setExpiresAt(LocalDateTime.now().plusHours(5));
        reservation.setId(5L);
        reservation.setReservedQuantity(10);
        when(productReservationRepository.findById(5L)).thenReturn(Optional.of(reservation));

        reservationService.deleteById(5L);
        assertEquals(100, product.getAvailableQuantity());
        verify(productReservationRepository).deleteById(5L);
    }

}
