package com.daybreaktech.xrpltools.backendapi.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "push_notification_subscription")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PushNotificationSubscription {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "VARCHAR(500)")
    private String endpoint;
    private String p256dh;
    private String auth;
    private LocalDateTime dateSubscribed;

}
