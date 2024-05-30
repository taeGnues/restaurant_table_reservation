package com.zerobase.tablereservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class RestaurantTableReservationApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestaurantTableReservationApplication.class, args);
    }

}
