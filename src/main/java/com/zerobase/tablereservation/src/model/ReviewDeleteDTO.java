package com.zerobase.tablereservation.src.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewDeleteDTO {
    Long reviewId;
    Long restaurantId;
}
