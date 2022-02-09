package com.daybreaktech.xrpltools.backendapi.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class NotificationMessage {

    private String title;
    private String body;
    private String icon;

}
