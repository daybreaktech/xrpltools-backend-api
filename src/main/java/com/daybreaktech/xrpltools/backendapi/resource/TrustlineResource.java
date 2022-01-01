package com.daybreaktech.xrpltools.backendapi.resource;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
public class TrustlineResource {

    private Long id;
    private String name;
    private String issuerAddress;
    private String currencyCode;
    private String limit;
    private String twitterUrl;
    private String website;
    private LocalDateTime dateAdded;

}
