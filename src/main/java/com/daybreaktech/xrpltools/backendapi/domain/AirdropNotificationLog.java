package com.daybreaktech.xrpltools.backendapi.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification_log")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AirdropNotificationLog {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String airdropCode;

    @Column(name = "airdrop_notification_type")
    @Enumerated(value = EnumType.STRING)
    private AirdropNotificationType type;

    private LocalDateTime dateTimeSent;

}
