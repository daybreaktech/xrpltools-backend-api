package com.daybreaktech.xrpltools.backendapi.resource;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
public class TrustlineResource {

    private Long id;
    private String issuerAddress;
    private String currencyCode;
    private String limit;
    private String twitterUrl;
    private String website;

}
