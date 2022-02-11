package com.daybreaktech.xrpltools.backendapi.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class NotificationDisplay {

    private String title;
    private String description;
    private String iconUrl;
    private String actionButtonLabel;
    private String actionButtonLink;

}
