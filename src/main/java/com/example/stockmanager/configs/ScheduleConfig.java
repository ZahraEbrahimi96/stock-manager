package com.example.stockmanager.configs;

import com.example.stockmanager.model.entity.ProductReservation;
import com.example.stockmanager.model.repository.ProductRepository;
import com.example.stockmanager.model.repository.ProductReservationRepository;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ScheduleConfig{

    @Resource
    private ProductReservationRepository productReservationRepository;
    @Resource
    private ProductRepository productRepository;


    @Scheduled(fixedRate = 60000)
    @Transactional
    public void releasedExpireReservation(){
        List<ProductReservation> expired = productReservationRepository.findAllByExpiresAtBefore(LocalDateTime.now());

        for (ProductReservation r : expired) {
            r.getProduct().setQuantity(r.getProduct().getQuantity() + r.getReservedQuantity());
            productRepository.save(r.getProduct());
        }

        if (!expired.isEmpty()) {
            productReservationRepository.deleteAll(expired);
            System.out.println("Released " + expired.size() + " expired reservations");
        }
    }

}
