package com.zerobase.tablereservation.src.web;

import com.zerobase.tablereservation.src.model.RestaurantRegisterDTO;
import com.zerobase.tablereservation.src.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping
    public ResponseEntity<?> updateRestaurant(@RequestBody RestaurantRegisterDTO dto){
        restaurantService.updateRestaurant(dto);
        return ResponseEntity.ok("수정에 성공했습니다.");
    }

    /*
    식당 - 주인은 일대일 관계이므로 user 정보만 확인하면, 그 식당을 삭제하면 된다.
    식당의 삭제의 경우 예약 정보 및 리뷰들까지 모두 같이 삭제가 되어야한다.
     */
    @DeleteMapping
    public ResponseEntity<?> deleteRestaurant(){
        restaurantService.deleteRestaurant();
        return ResponseEntity.ok("삭제에 성공했습니다.");
    }

    @GetMapping
    public ResponseEntity<?> readRestaurant(){
        var result = restaurantService.readRestaurant();
        return ResponseEntity.ok(result);
    }
}
