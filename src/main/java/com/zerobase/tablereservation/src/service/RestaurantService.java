package com.zerobase.tablereservation.src.service;

import com.zerobase.tablereservation.common.exceptions.BaseException;
import com.zerobase.tablereservation.common.exceptions.ExceptionCode;
import com.zerobase.tablereservation.src.model.RestaurantRegisterDTO;
import com.zerobase.tablereservation.src.persist.ManagerRepository;
import com.zerobase.tablereservation.src.persist.ReservationRepository;
import com.zerobase.tablereservation.src.persist.RestaurantRepository;
import com.zerobase.tablereservation.src.persist.entity.Manager;
import com.zerobase.tablereservation.src.persist.entity.Restaurant;
import com.zerobase.tablereservation.src.model.RestaurantDTO;
import com.zerobase.tablereservation.src.model.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final ManagerRepository managerRepository;
    private final ReservationRepository reservationRepository;

    private final AuthService authService;

    /*
    식당 등록하기
     */
    @Transactional
    public void registerRestaurant(RestaurantRegisterDTO dto){
        UserVO userVO = authService.getCurrentUserVO();

        if(restaurantRepository.findByManager_Id(userVO.getUserId()).isPresent()){
            throw new BaseException(ExceptionCode.MANAGER_ALREADY_REGISTERED);
        }

        Manager manager = managerRepository.findById(userVO.getUserId())
                .orElseThrow(() -> new BaseException(ExceptionCode.NOT_FIND_USER));

        Restaurant restaurant = dto.toEntity();
        restaurant.designateManager(manager);

        restaurantRepository.save(restaurant);
    }

    /*
    식당 수정하기
     */
    @Transactional
    public void updateRestaurant(RestaurantRegisterDTO dto){
        UserVO userVO = authService.getCurrentUserVO();

        Restaurant restaurant = restaurantRepository.findByManager_Id(userVO.getUserId()).orElseThrow(
                () -> new BaseException(ExceptionCode.MANAGER_EMPTY_RESTAURANT));

        restaurant.update(dto);
    }

    /*
    자신의 식당 조회하기 (UserVO 기반)
     */
    public RestaurantDTO readRestaurant() {
        UserVO userVO = authService.getCurrentUserVO();
        Restaurant restaurant = restaurantRepository.findByManager_Id(userVO.getUserId()).orElseThrow(
                () -> new BaseException(ExceptionCode.MANAGER_EMPTY_RESTAURANT));

        return RestaurantDTO.fromEntity(restaurant);
    }


    /*
    식당 삭제하기
     */
    @Transactional
    public void deleteRestaurant(){
        UserVO userVO = authService.getCurrentUserVO();
        Restaurant restaurant = restaurantRepository.findByManager_Id(userVO.getUserId()).orElseThrow(
            () -> new BaseException(ExceptionCode.MANAGER_EMPTY_RESTAURANT)
        );
        // 해당 식당의 예약정보-리뷰들 모두 삭제.
        reservationRepository.deleteAllByRestaurant_Id(restaurant.getId());

        // 해당 식당 삭제
        restaurantRepository.delete(restaurant);
    }

}
