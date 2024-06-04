package com.zerobase.tablereservation.src.web;

import com.zerobase.tablereservation.src.model.ReservationRegisterDTO;
import com.zerobase.tablereservation.src.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    /*
    현재 유저 id 확인 후 매장 id를 통해 예약 등록하기
     */
    @PostMapping
    public ResponseEntity<?> registerReservation(
            @RequestBody ReservationRegisterDTO dto
    ){
        reservationService.registerReservation(dto);
        return ResponseEntity.ok("예약에 성공했습니다.");
    }

    /*
    현재 고객이 예약한 예약 모든 정보 조회하기 (사용완료 포함)
     */
    @GetMapping
    public ResponseEntity<?> getReservations(){
        return ResponseEntity.ok(reservationService.findAllReservations());
    }

    /*
    현재 고객이 키오스크에서 예약 확인 방문처리 및 검증
     */
    @PutMapping("/{restaurantId}")
    public ResponseEntity<?> checkVisitReservation(@PathVariable Long restaurantId){
        reservationService.checkVisitReservation(restaurantId);
        return ResponseEntity.ok("방문에 성공했습니다.");
    }



}
