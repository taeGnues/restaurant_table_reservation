package com.zerobase.tablereservation.src.service;

import com.zerobase.tablereservation.common.exceptions.BaseException;
import com.zerobase.tablereservation.common.exceptions.ExceptionCode;
import com.zerobase.tablereservation.src.model.ReservationDTO;
import com.zerobase.tablereservation.src.model.ReservationRegisterDTO;
import com.zerobase.tablereservation.src.model.UserVO;
import com.zerobase.tablereservation.src.persist.CustomerRepository;
import com.zerobase.tablereservation.src.persist.ReservationRepository;
import com.zerobase.tablereservation.src.persist.RestaurantRepository;
import com.zerobase.tablereservation.src.persist.entity.Customer;
import com.zerobase.tablereservation.src.persist.entity.Reservation;
import com.zerobase.tablereservation.src.persist.entity.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/*
예약 관련 서비스를 처리한다. (CUSTOMER)
 */
@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final RestaurantRepository restaurantRepository;
    private final CustomerRepository customerRepository;
    private final AuthService authService;

    /*
    예약 등록은
    1. 해당 식당에 이미 예약이 존재하는지 확인한다.
    2. 예약 시간을 검증한다. (예약 가능한 시각은 현재시각으로부터 최소 15분 이상이다.)
    3. 현재 유저와 식당의 존재 여부를 검증한다.
    4. dto 기반으로 예약 내용을 저장한다.
     */
    @Transactional
    public void registerReservation(ReservationRegisterDTO dto) {
        UserVO userVO = authService.getCurrentUserVO();

        checkAlreadyReservation(userVO.getUserId(), dto.getRestaurantId());
        checkReservationTime(dto);

        Customer customer = customerRepository.findById(userVO.getUserId()).orElseThrow(
                () -> new BaseException(ExceptionCode.NOT_FIND_USER)
        );

        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId()).orElseThrow(
                () -> new BaseException(ExceptionCode.RESTAURANT_NOT_FOUND)
        );

        Reservation reservation = Reservation.builder()
                .time(dto.getTime())
                .details(dto.getDetails())
                .visit(false)
                .restaurant(restaurant)
                .customer(customer)
                .build();

        reservationRepository.save(reservation);

    }

    private void checkReservationTime(ReservationRegisterDTO dto) {
        if (!checkReservationTimePossible(dto.getTime())){
            throw new BaseException(ExceptionCode.RESERVATION_TOO_QUICK);
        }
    }
    private void checkAlreadyReservation(Long customerId, Long restaurantId) {
        if(reservationRepository.findByCustomer_IdAndRestaurant_IdAndVisit(customerId, restaurantId, false).isPresent()){
            throw new BaseException(ExceptionCode.RESERVATION_ALREADY_DONE);
        }
    }
    private boolean checkReservationTimePossible(LocalDateTime time) {
          // 주어진 예약시간이 현재 시각으로부터 15분 이후인지 확인함.
            return time.isAfter(LocalDateTime.now().plusMinutes(15));
    }

    /*
     현재 등록된 나의 모든 예약을 조회한다.
     */
    public List<ReservationDTO> findAllReservations() {
        UserVO userVO = authService.getCurrentUserVO();

        return reservationRepository.findAllByCustomer_Id(userVO.getUserId())
                .stream().map(ReservationDTO::fromEntity)
                .collect(Collectors.toList());

    }

    /*
    키오스크에서 보낸 정보를 토대로
    해당 예약에 대해 방문(사용완료)으로 처리한다.
    1. 현재 로그인한 고객 정보와 restaurantId 로 reservation을 가져온다.
    2. 현재 시각이 예약 시각보다 10분 더 빠른지 확인한다.
      > 만약, 고객이 늦게왔다면, 방문처리를 중단한다.
    3. 모든 조건 충족 시 앞에서 찾은 reservation에 대해 방문처리를 한다. (setvisit())
     */
    @Transactional
    public void checkVisitReservation(Long restaurantId) {
        UserVO userVO = authService.getCurrentUserVO();

        // 1. 현재 resId, userId로 예약이 존재하는지확인
        Reservation reservation = reservationRepository.findByCustomer_IdAndRestaurant_IdAndVisit(userVO.getUserId(), restaurantId, false).orElseThrow(
                () -> new BaseException(ExceptionCode.RESERVATION_NOT_FOUND)
        );

        // 2. 현재 시각이 예약 시간보다 10분 더 빠른지 확인
        if(LocalDateTime.now().isAfter(reservation.getTime().minusMinutes(10))){
            throw new BaseException(ExceptionCode.RESERVATION_TOO_LATE);
        }

        // 3. 모든 조건 충족 시 현재 예약 방문 상태로 바꾸기.
        reservation.checkVisit();
    }
}
