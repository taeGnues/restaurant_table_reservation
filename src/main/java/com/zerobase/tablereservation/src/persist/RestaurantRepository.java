package com.zerobase.tablereservation.src.persist;

import com.zerobase.tablereservation.src.persist.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
