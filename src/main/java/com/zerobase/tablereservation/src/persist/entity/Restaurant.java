package com.zerobase.tablereservation.src.persist.entity;


import com.zerobase.tablereservation.common.entity.BaseEntity;
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
    private String position;
    private String description;

    @OneToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

    public void designateManager(Manager manager){
        this.manager = manager;
    }
}
