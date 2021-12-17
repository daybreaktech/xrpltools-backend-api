package com.daybreaktech.xrpltools.backendapi.domain;

import lombok.*;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;

@Entity
@Table(name = "airdrop_categories", uniqueConstraints={
        @UniqueConstraint(columnNames = {"airdrop_category", "category_order"})
})
@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "airdrop_category")
    @Enumerated(value = EnumType.STRING)
    private AirdropCategories category;

    @Column(name = "category_order")
    private Integer categoryOrder;

    @Lazy
    @OneToOne
    @JoinColumn(name = "airdrop_schedule_id")
    private AirdropSchedule airdropSchedule;

}
