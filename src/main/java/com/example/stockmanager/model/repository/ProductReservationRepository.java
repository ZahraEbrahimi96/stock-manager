package com.example.stockmanager.model.repository;

import com.example.stockmanager.model.entity.ProductReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductReservationRepository extends JpaRepository<ProductReservation,Long> {

    List<ProductReservation> findAllByExpiresAtBefore(LocalDateTime now);

    void deleteAllByExpiresAtBefore(LocalDateTime now);

}
