package com.zerobase.tablereservation.src.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewDTO {
    Long reservationId;
    Long restaurantId;
    int rating;
    String content;
}
