package com.zerobase.tablereservation.src.web;

import com.zerobase.tablereservation.src.model.RestaurantRegisterDTO;
import com.zerobase.tablereservation.src.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurant-manage")
public class RestaurantController {
    private final RestaurantService restaurantService;


    @PostMapping
    public ResponseEntity<?> registerRestaurant(@RequestBody RestaurantRegisterDTO dto){
        restaurantService.registerRestaurant(dto);
        return ResponseEntity.ok("등록에 성공했습니다.");
    }


}
