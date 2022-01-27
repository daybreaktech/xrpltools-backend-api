package com.daybreaktech.xrpltools.backendapi.domain;

import lombok.*;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "airdrop_schedules")
@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class AirdropSchedule {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String shortDesc;

    @Column(columnDefinition = "LONGTEXT")
    private String longDesc;

    private LocalDateTime snapshotDate;
    private LocalDateTime airdropDate;
    private String timeZone;

    @Column(name = "airdrop_status")
    @Enumerated(value = EnumType.STRING)
    private Status status = Status.ACTIVE;

    private String tags;

    private LocalDateTime dateAdded;

    @ManyToOne
    @JoinColumn(name = "trustline_id", nullable = true)
    private Trustline trustline;

    @Lazy
    @OneToOne(mappedBy = "airdropSchedule", cascade = CascadeType.REMOVE)
    private ScheduleCategory scheduleCategory;

    private String refsUrl;
    private String formUrl;
    private String imageUrl;
    private Boolean useTrustlineImg;

}
