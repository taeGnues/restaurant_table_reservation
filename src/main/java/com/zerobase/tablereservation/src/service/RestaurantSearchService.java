package com.zerobase.tablereservation.src.service;

import com.zerobase.tablereservation.common.exceptions.BaseException;
import com.zerobase.tablereservation.common.exceptions.ExceptionCode;
import com.zerobase.tablereservation.src.persist.RestaurantRepository;
import com.zerobase.tablereservation.src.model.RestaurantDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantSearchService {
    private final RestaurantRepository restaurantRepository;

    /*
    등록된 모든 식당 조회
     */
    public List<RestaurantDTO> findAllRestaurants(){
        return restaurantRepository.findAll()
                .stream().map(RestaurantDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<RestaurantDTO> findAllRestaurantsByKeyword(String keyword, int pageNo) {
        Pageable limit = PageRequest.of(pageNo, 10);
        return restaurantRepository.findAllByNameContains(keyword, limit)
                .stream().map(RestaurantDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<RestaurantDTO> findAllRestaurantsByPaging(int pageNo) {
        Pageable limit = PageRequest.of(pageNo, 10);
        return restaurantRepository.findAll(limit).stream()
                .map(RestaurantDTO::fromEntity).collect(Collectors.toList());


    }

    public List<RestaurantDTO> findAllRestaurantsOrderByRatings(int pageNo){
        Pageable limit = PageRequest.of(pageNo, 10, Sort.by(Sort.Direction.DESC, "rating"));
        return restaurantRepository.findAll(limit)
                .stream().map(RestaurantDTO::fromEntity).collect(Collectors.toList());
    }

    public RestaurantDTO findRestaurantDetailById(Long restaurantId) {
        return RestaurantDTO.fromEntity(restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new BaseException(ExceptionCode.RESTAURANT_NOT_FOUND)
        ));
    }


}
