package com.zerobase.tablereservation.src.persist.entity;

import com.zerobase.tablereservation.common.entity.BaseEntity;
import com.zerobase.tablereservation.src.model.ReviewDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.AuditOverride;

@Entity
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class Review extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int rating;
    private String content;

    @OneToOne(mappedBy = "review", fetch = FetchType.LAZY)
    private Reservation reservation;

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public void update(ReviewDTO dto) {
        this.rating = dto.getRating();
        this.content = dto.getContent();
    }
}
