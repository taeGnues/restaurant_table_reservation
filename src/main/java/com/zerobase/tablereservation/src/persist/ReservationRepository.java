package com.zerobase.tablereservation.src.persist;

import com.zerobase.tablereservation.src.persist.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    void deleteAllByRestaurant_Id(Long id);

    Optional<Reservation> findByCustomer_IdAndRestaurant_IdAndVisit(Long customerId, Long restaurantId, boolean isVisit);
    List<Reservation> findAllByRestaurant_Id(Long id);

    @Query("select r from Reservation r join fetch r.review where r.restaurant.id = :restaurantid")
    List<Reservation> findAllByRestaurant_IdWithFetch(@Param("restaurantid") Long restaurantid);
    List<Reservation> findAllByCustomer_Id(Long id);

    Optional<Reservation> findByIdAndCustomer_Id(Long reservationId, Long customerId);
}
