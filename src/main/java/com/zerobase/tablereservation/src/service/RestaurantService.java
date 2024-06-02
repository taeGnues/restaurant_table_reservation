package com.zerobase.tablereservation.src.service;

import com.zerobase.tablereservation.src.persist.ManagerRepository;
import com.zerobase.tablereservation.src.persist.RestaurantRepository;
import com.zerobase.tablereservation.src.persist.entity.Manager;
import com.zerobase.tablereservation.src.persist.entity.Restaurant;
import com.zerobase.tablereservation.src.model.RestaurantRegisterDTO;
import com.zerobase.tablereservation.src.model.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final ManagerRepository managerRepository;
    private final AuthService authService;

    /*
    식당 등록하기
     */
    @Transactional
    public void registerRestaurant(RestaurantRegisterDTO dto){
        UserVO userVO = authService.getCurrentUserVO();

        Manager manager = managerRepository.findById(userVO.getUserId())
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다."));

        Restaurant restaurant = dto.toEntity();
        restaurant.designateManager(manager);

        restaurantRepository.save(restaurant);
    }

    /*
    식당 수정하기
     */



    /*
    식당 삭제하기
     */


}
