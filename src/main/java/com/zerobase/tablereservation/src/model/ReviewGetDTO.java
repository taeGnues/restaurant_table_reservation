package com.zerobase.tablereservation.src.model;

import com.zerobase.tablereservation.src.persist.entity.Review;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewGetDTO {

    Long reviewId;
    int rating;
    String content;

    public static ReviewGetDTO fromEntity(Review review){
        return ReviewGetDTO.builder()
                .reviewId(review.getId())
                .rating(review.getRating())
                .content(review.getContent())
                .build();

    }

}
