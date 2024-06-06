package com.zerobase.tablereservation.src.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewDTO {
    @NotNull(message = "reservationId를 입력해주세요.")
    Long reservationId;
    @NotNull(message = "restaurantId를 입력해주세요.")
    Long restaurantId;
    @NotNull(message = "rating을 입력해주세요.")
    int rating;
    @NotBlank(message = "content를 입력해주세요.")
    String content;
}
