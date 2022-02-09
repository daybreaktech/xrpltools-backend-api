package com.daybreaktech.xrpltools.backendapi.resource;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
public class PushNotificationScheduleResource {

    private Long id;
    private String title;
    private String description;
    private String iconUrl;
    private String actionButtonLabel;
    private String actionButtonLink;
    private LocalDateTime targetDateTime;
    private String pushMode;
    private Boolean isSent;

}
