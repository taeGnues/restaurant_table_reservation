package com.zerobase.tablereservation.src.web;

import com.zerobase.tablereservation.src.service.RestaurantSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurant-search")
@Transactional(readOnly = true)
public class RestaurantSearchController {

    private final RestaurantSearchService restaurantSearchService;

    @GetMapping
    public ResponseEntity<?> searchAllRestaurants(){
        return ResponseEntity.ok(restaurantSearchService.findAllRestaurants());
    }

}
