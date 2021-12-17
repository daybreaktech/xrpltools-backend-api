package com.daybreaktech.xrpltools.backendapi.resource;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class AirdropScheduleResource {

    private Long id;
    private String code;
    private String shortDesc;
    private String longDesc;
    private LocalDateTime snapshotDate;
    private LocalDateTime airdropDate;
    private String timeZone;
    private String status;
    private String category;
    private List<String> tags;
    private Long trustlineId;

}
