package com.daybreaktech.xrpltools.backendapi.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "push_notification_schedule")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushNotificationSchedule {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String description;

    private String iconUrl;

    private String actionButtonLabel;

    private String actionButtonLink;

    private LocalDateTime targetDateTime;

    @Column(name = "push_mode")
    @Enumerated(value = EnumType.STRING)
    private PushMode pushMode;

    private Integer sentRecipients;

    private Boolean isSent;

}
