package com.zerobase.tablereservation.src.domain;

import com.zerobase.tablereservation.src.domain.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
