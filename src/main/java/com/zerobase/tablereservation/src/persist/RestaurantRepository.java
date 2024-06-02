package com.zerobase.tablereservation.src.persist;

import com.zerobase.tablereservation.src.persist.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    Optional<Restaurant> findByManager_Id(Long id);

    void deleteByManager_Id(Long userId);
}
