package com.zerobase.tablereservation.src.web;

import com.zerobase.tablereservation.src.service.RestaurantSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurant-search")
public class RestaurantSearchController {

    private final RestaurantSearchService restaurantSearchService;

    /*
    등록된 모든 식당을 조회하기
     */
    @GetMapping("/all")
    public ResponseEntity<?> searchAllRestaurants(){
        return ResponseEntity.ok(restaurantSearchService.findAllRestaurants());
    }

    /*
    등록된 식당을 pageNo 기준으로 10개씩 조회하기
     */
    @GetMapping("/restaurants")
    public ResponseEntity<?> searchAllRestaurants(@RequestParam int pageNo){
        return ResponseEntity.ok(restaurantSearchService.findAllRestaurantsByPaging(pageNo));
    }

    /*
    restaurantId로 상세 조회하기
     */
    @GetMapping("/restaurants/{restaurantId}")
    public ResponseEntity<?> searchRestaurantDetail(@PathVariable Long restaurantId){
        return ResponseEntity.ok(restaurantSearchService.findRestaurantDetailById(restaurantId));
    }

    /*
    해당 keyword 포함하는 등록된 식당을 pageNo 기준으로 10개씩 조회하기
     */
    @GetMapping
    public ResponseEntity<?> searchRestaurantsByKeyword(@RequestParam String keyword, @RequestParam int pageNo){
        return ResponseEntity.ok(restaurantSearchService.findAllRestaurantsByKeyword(keyword, pageNo));
    }

    /*
    별점 순으로 내림차순 정렬하여 pageNo 기준으로 10개씩 조회하기
     */
    @GetMapping("/ratings")
    public ResponseEntity<?> searchRestaurantsOrderByRatings(@RequestParam int pageNo){
        return ResponseEntity.ok(restaurantSearchService.findAllRestaurantsOrderByRatings(pageNo));
    }



}
