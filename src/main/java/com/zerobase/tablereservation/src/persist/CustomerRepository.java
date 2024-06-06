package com.zerobase.tablereservation.src.persist;

import com.zerobase.tablereservation.src.persist.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByUsername(String username);
}
