package com.zerobase.tablereservation.src.persist.entity;


import com.zerobase.tablereservation.common.entity.BaseEntity;
import com.zerobase.tablereservation.src.model.RestaurantRegisterDTO;
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
public class Restaurant extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double rating;
    private String position;
    private String description;

    @OneToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

    public void designateManager(Manager manager){
        this.manager = manager;
    }
    public void update(RestaurantRegisterDTO dto){
        this.name = dto.getName();
        this.position = dto.getPosition();
        this.description = dto.getDescription();
    }

    public void evaluated(double rating){
        this.rating = rating;
    }
}
