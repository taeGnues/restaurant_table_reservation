package com.zerobase.tablereservation.src.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReservationRegisterDTO {

    @NotNull(message = "restaurantId가 필요합니다.")
    private Long restaurantId;
    @NotNull(message = "예약시간을 입력해주세요.")
    private LocalDateTime time;
    @NotNull(message = "예약에 대한 설명이 필요합니다.")
    private String details;
}
