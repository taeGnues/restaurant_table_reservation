package com.zerobase.tablereservation.src.model;

import com.zerobase.tablereservation.src.persist.entity.Reservation;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReservationDTO {

    private Long reservationId;
    private LocalDateTime time;
    private String details;
    private boolean complete;

    public static ReservationDTO fromEntity(Reservation m) {
        return ReservationDTO.builder()
                .reservationId(m.getId())
                .time(m.getTime())
                .details(m.getDetails())
                .complete(m.isVisit())
                .build();
    }
}
