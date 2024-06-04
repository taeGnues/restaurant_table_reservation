package com.zerobase.tablereservation.src.persist;

import com.zerobase.tablereservation.src.persist.entity.Customer;
import com.zerobase.tablereservation.src.persist.entity.Reservation;
import com.zerobase.tablereservation.src.persist.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    void deleteAllByRestaurant_Id(Long id);

    Optional<Reservation> findByCustomer_IdAndRestaurant_IdAndVisit(Long customerId, Long restaurantId, boolean isVisit);
    List<Reservation> findAllByRestaurant_Id(Long id);
    List<Reservation> findAllByCustomer_Id(Long id);
}
