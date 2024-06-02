package com.zerobase.tablereservation.src.persist;

import com.zerobase.tablereservation.src.persist.entity.Restaurant;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    Optional<Restaurant> findByManager_Id(Long id);

    List<Restaurant> findAllByNameContains(String keyword, Pageable pageable);
}
