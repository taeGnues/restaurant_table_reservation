package com.zerobase.tablereservation.src.persist;

import com.zerobase.tablereservation.src.persist.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r WHERE r.reservation.id = :reservationId")
    Optional<Review> findByReservationId(@Param("reservationId") Long reservationId);

}
