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
    1. 해당 유저가 이미 등록한 식당이 있는지 확인한다.
    2. 해당 유저가 존재하는지 확인한다.
    3. 식당을 생성하고, 해당 식당과 유저를 매핑해준다.
    4. 식당을 저장한다.
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
    1. 현재 로그인한 유저 정보로 식당을 조회하고,
    2. 해당 식당 정보를 수정한다.
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
        // 1. 예약과 연관된 리뷰들을 모두 관계를 해제한다.
        reservationRepository.findAllByRestaurant_Id(restaurant.getId())
                .forEach(m->m.setReview(null));

        // 2. 예약을 모두 지운다.
        reservationRepository.deleteAllByRestaurant_Id(restaurant.getId());

        // 해당 식당 삭제
        restaurantRepository.delete(restaurant);
    }

}
