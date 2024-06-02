package com.zerobase.tablereservation.src.model;

import com.zerobase.tablereservation.src.persist.entity.Restaurant;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestaurantRegisterDTO {

    private Long id;
    private String name;
    private String position;
    private String description;

    public Restaurant toEntity(){
        return Restaurant.builder()
                .name(name)
                .position(position)
                .description(description)
                .build();
    }

    public static RestaurantRegisterDTO fromEntity(Restaurant restaurant){
        return RestaurantRegisterDTO.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .position(restaurant.getPosition())
                .description(restaurant.getDescription())
                .build();
    }

}
