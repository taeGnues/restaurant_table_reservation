package com.zerobase.tablereservation.src.web;


import com.zerobase.tablereservation.src.service.ReservationManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservation-manage")
@RequiredArgsConstructor
public class ReservationManagerController {
    private final ReservationManagerService reservationManagerService;

    /*
    해당 식당에 등록된 모든 예약 조회 (사용완료 포함)
     */
    @GetMapping
    public ResponseEntity<?> searchAllReservations(){
        return ResponseEntity.ok(reservationManagerService.findAllReservations());
    }
}
