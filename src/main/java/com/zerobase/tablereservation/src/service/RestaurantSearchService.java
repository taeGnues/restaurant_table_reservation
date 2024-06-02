package com.zerobase.tablereservation.src.service;

import com.zerobase.tablereservation.src.persist.RestaurantRepository;
import com.zerobase.tablereservation.src.model.RestaurantRegisterDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public List<RestaurantRegisterDTO> findAllRestaurants(){
        return restaurantRepository.findAll()
                .stream().map(RestaurantRegisterDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<RestaurantRegisterDTO> findAllRestaurantsByKeyword(String keyword, int pageNo) {
        Pageable limit = PageRequest.of(pageNo, 10);
        return restaurantRepository.findAllByNameContains(keyword, limit)
                .stream().map(RestaurantRegisterDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<RestaurantRegisterDTO> findAllRestaurantsByPaging(int pageNo) {
        Pageable limit = PageRequest.of(pageNo, 10);
        return restaurantRepository.findAll(limit).stream()
                .map(RestaurantRegisterDTO::fromEntity).collect(Collectors.toList());


    }

    public RestaurantRegisterDTO findRestaurantDetailById(Long restaurantId) {
        return RestaurantRegisterDTO.fromEntity(restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new IllegalStateException("해당 식당은 존재하지 않습니다.")
        ));
    }
}
