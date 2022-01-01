package com.daybreaktech.xrpltools.backendapi.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class AirdropItem {

    private Long id;

    private String issuerAddress;
    private String currencyCode;
    private String limit;
    private String tokenName;
    private String twitterUrl;
    private String websiteUrl;

    private String code;
    private String timezone;
    private LocalDateTime snapshotDate;
    private LocalDateTime airdropDate;
    private String shortDesc;
    private String longDesc;
    private Integer order;
    private List<String> tags;

}
