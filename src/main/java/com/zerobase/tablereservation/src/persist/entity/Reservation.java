package com.zerobase.tablereservation.src.persist.entity;

import com.zerobase.tablereservation.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.AuditOverride;

import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class Reservation extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime time;
    private String details;
    private boolean visit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "review_id")
    private Review review;
    // 리뷰의 존재 이전에 예약이 존재할 수 없음.... 따라서 예약삭제->리뷰삭제가 강요되지 않음.

    public void checkVisit(){
        this.visit = true;
    }

    public void setReview(Review review) {
        this.review = review;
    }
}
