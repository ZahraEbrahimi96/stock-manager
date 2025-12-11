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
    @DisplayName("success save and reduce available quantity")
    void successSaveReserveProduct() throws OutOfStockException, ProductNotFoundException {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productReservationRepository.save(any(ProductReservation.class)))
                .thenAnswer(inv -> inv.getArgument(0)); //CHECK

        ProductReservation result = reservationService.save(1L, 25, 3);

        assertNotNull(result);
        assertEquals(25, result.getReservedQuantity());
        assertEquals(product, result.getProduct());
        assertTrue(result.getExpiresAt().isAfter(LocalDateTime.now()));
        assertTrue(result.getExpiresAt().isBefore(LocalDateTime.now().plusHours(4)));

        verify(productRepository).save(product);
        assertEquals(75, product.getAvailableQuantity());
    }

    @Test
    @DisplayName("should throw ProductNotFoundException")
    void save_ProductNotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty()); //CHECK

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            reservationService.save(999L, 10, 1);
        });

        assertEquals("Product not found", exception.getMessage());
        verify(productRepository, never()).save(any());
    }

//    @Test
//    @DisplayName("save- should throw OutOfStockException")
//    void save_ShouldThrowOutOfStock() {
//        product.setAvailableQuantity(5);
//        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
//
//        OutOfStockException exception = assertThrows(OutOfStockException.class, () -> {
//            reservationService.save(1L, 10, 1);
//        });
//
//        assertEquals("Amount is greater than available quantity", exception.getMessage());
//        verify(productRepository, never()).save(any());
//    }
//
//    @Test
//    @DisplayName("success deleteById - should restore available quantity")
//    void deleteById_ShouldRestoreStockAndDelete() {
//        ProductReservation reservation = new ProductReservation();
//        reservation.setId(5L);
//        reservation.setProduct(product);
//        reservation.setReservedQuantity(30);
//        reservation.setExpiresAt(LocalDateTime.now().plusHours(1));
//
//        when(productReservationRepository.findById(5L)).thenReturn(Optional.of(reservation));
//
//        reservationService.deleteById(5L);
//
//        assertEquals(130, product.getAvailableQuantity()); // 100 + 30
//        verify(productRepository).save(product);
//        verify(productReservationRepository).deleteById(5L);
//    }
//
//    @Test
//    @DisplayName("deleteById failed")
//    void deleteById_NotFound() {
//        when(productReservationRepository.findById(999L)).thenReturn(Optional.empty());
//
//        reservationService.deleteById(999L);
//
//        verify(productRepository, never()).save(any());
//        verify(productReservationRepository, never()).deleteById(any());
//    }
//
//    @Test
//    @DisplayName("success findById")
//    void findById_Success() throws ProductNotFoundException {
//        ProductReservation reservation = new ProductReservation();
//        when(productReservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
//
//        ProductReservation result = reservationService.findById(1L);
//
//        assertSame(reservation, result);
//    }
//
//    @Test
//    @DisplayName("findById() - should throw productNotFoundException")
//    void findById_NotFound_ShouldThrow() {
//        when(productReservationRepository.findById(999L)).thenReturn(Optional.empty());
//
//        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
//            reservationService.findById(999L);
//        });
//
//        assertEquals("Product not found", exception.getMessage());
//    }
//
//    @Test
//    @DisplayName("findAll-success")
//    void findAll_ShouldReturnList() {
//        List<ProductReservation> list = List.of(new ProductReservation(), new ProductReservation());
//        when(productReservationRepository.findAll()).thenReturn(list);
//
//        assertEquals(2, reservationService.findAll().size());
//    }
//
//    @Test
//    @DisplayName("deleteAllExpired() - should restore available quantity")
//    void deleteAllExpired_ShouldRestoreStockForExpiredReservations() {
//        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);
//
//        ProductReservation expired1 = new ProductReservation();
//        expired1.setId(10L);
//        expired1.setReservedQuantity(20);
//        expired1.setProduct(product);
//        expired1.setExpiresAt(fiveMinutesAgo.minusHours(2));
//
//        ProductReservation expired2 = new ProductReservation();
//        expired2.setId(11L);
//        expired2.setReservedQuantity(30);
//        expired2.setProduct(product);
//        expired2.setExpiresAt(fiveMinutesAgo.minusHours(1));
//
//        when(productReservationRepository.findAllByExpiresAtBefore(fiveMinutesAgo))
//                .thenReturn(List.of(expired1, expired2));
//
//        doNothing().when(productReservationRepository).deleteAll(List.of(expired1, expired2));
//
//        reservationService.deleteAllExpired();
//
//        assertEquals(150, product.getAvailableQuantity());
//
//        verify(productRepository, times(2)).save(product);
//        verify(productReservationRepository).deleteAll(List.of(expired1, expired2));
//    }

}
