package com.zerobase.tablereservation.src.domain;

import com.zerobase.tablereservation.src.domain.entity.Customer;
import com.zerobase.tablereservation.src.domain.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
    Optional<Manager> findByUsername(String username);
}
