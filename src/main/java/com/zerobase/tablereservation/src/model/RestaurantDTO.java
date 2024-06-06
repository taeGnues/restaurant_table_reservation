package com.zerobase.tablereservation.src.model;

import com.zerobase.tablereservation.src.persist.entity.Restaurant;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestaurantDTO {

    private Long id;
    private double rating;
    private String name;
    private String position;
    private String description;

    public static RestaurantDTO fromEntity(Restaurant restaurant){
        return RestaurantDTO.builder()
                .id(restaurant.getId())
                .rating(restaurant.getRating())
                .name(restaurant.getName())
                .position(restaurant.getPosition())
                .description(restaurant.getDescription())
                .build();
    }

}
