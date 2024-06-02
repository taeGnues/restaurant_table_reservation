package com.zerobase.tablereservation.src.model;

import com.zerobase.tablereservation.src.persist.entity.Restaurant;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestaurantRegisterDTO {

    private Long id;
    @NotNull(message = "이름을 입력해주세요.")
    private String name;
    @NotNull(message = "위치를 입력해주세요.")
    private String position;
    @NotNull(message = "설명을 입력해주세요.")
    private String description;

    public Restaurant toEntity(){
        return Restaurant.builder()
                .name(name)
                .position(position)
                .description(description)
                .build();
    }

}
