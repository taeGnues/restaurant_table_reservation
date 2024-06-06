package com.zerobase.tablereservation.src.model;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewDeleteDTO {
    @NotNull(message = "reviewId를 입력해주세요.")
    Long reviewId;
    @NotNull(message = "restaurantId을 입력해주세요.")
    Long restaurantId;
}
