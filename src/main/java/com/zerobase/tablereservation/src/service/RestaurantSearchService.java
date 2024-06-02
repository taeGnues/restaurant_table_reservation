package com.zerobase.tablereservation.src.service;

import com.zerobase.tablereservation.src.persist.RestaurantRepository;
import com.zerobase.tablereservation.src.model.RestaurantRegisterDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantSearchService {
    private final RestaurantRepository restaurantRepository;

    /*
    등록된 모든 식당 조회
     */
    public List<RestaurantRegisterDTO> findAllRestaurants(){
        return restaurantRepository.findAll()
                .stream().map(RestaurantRegisterDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
