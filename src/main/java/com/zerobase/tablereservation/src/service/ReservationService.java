package com.zerobase.tablereservation.src.service;

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

import static java.time.LocalTime.now;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final RestaurantRepository restaurantRepository;
    private final CustomerRepository customerRepository;
    private final AuthService authService;

    @Transactional
    public void registerReservation(ReservationRegisterDTO dto) {
        UserVO userVO = authService.getCurrentUserVO();


        if (!checkReservationTimePossible(dto.getTime())){
            throw new IllegalStateException("최소 현재 시각으로부터 15분 이후부터 예약이 가능합니다.");
        }


        Customer customer = customerRepository.findById(userVO.getUserId()).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 유저입니다.")
        );

        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId()).orElseThrow(
                () -> new IllegalStateException("존재하지 않는 식당입니다.")
        );

        Reservation reservation = Reservation.builder()
                .time(dto.getTime())
                .details(dto.getDetails())
                .complete(false)
                .restaurant(restaurant)
                .customer(customer)
                .build();

        reservationRepository.save(reservation);

    }

    // 주어진 예약시간이 현재 시각으로부터 15분 이후인지 확인함.
    private boolean checkReservationTimePossible(LocalDateTime time) {
            return time.isAfter(LocalDateTime.now().plusMinutes(15));
    }


    public List<ReservationDTO> findAllReservations() {
        UserVO userVO = authService.getCurrentUserVO();

        return reservationRepository.findAllByCustomer_Id(userVO.getUserId())
                .stream().map(ReservationDTO::fromEntity)
                .collect(Collectors.toList());

    }
}
