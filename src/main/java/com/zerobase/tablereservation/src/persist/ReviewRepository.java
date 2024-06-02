package com.zerobase.tablereservation.src.persist;

import com.zerobase.tablereservation.src.persist.entity.Customer;
import com.zerobase.tablereservation.src.persist.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
