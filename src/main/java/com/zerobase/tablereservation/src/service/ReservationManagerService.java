package com.zerobase.tablereservation.src.service;

import com.zerobase.tablereservation.src.model.ReservationDTO;
import com.zerobase.tablereservation.src.model.UserVO;
import com.zerobase.tablereservation.src.persist.ReservationRepository;
import com.zerobase.tablereservation.src.persist.RestaurantRepository;
import com.zerobase.tablereservation.src.persist.entity.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationManagerService {
    private final AuthService authService;
    private final RestaurantRepository restaurantRepository;
    private final ReservationRepository reservationRepository;

    /*
    모든 예약을 조회하고, DTO로 변환해 리턴해준다.
     */
    public List<ReservationDTO> findAllReservations() {
        UserVO userVO = authService.getCurrentUserVO();

        Restaurant restaurant = restaurantRepository.findByManager_Id(userVO.getUserId())
                .orElseThrow(() -> new IllegalStateException("식당이 존재하지 않습니다."));

        return reservationRepository.findAllByRestaurant_Id(restaurant.getId())
                .stream().map(ReservationDTO::fromEntity)
                .collect(Collectors.toList());

    }
}
