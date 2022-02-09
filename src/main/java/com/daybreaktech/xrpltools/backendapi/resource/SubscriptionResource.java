package com.daybreaktech.xrpltools.backendapi.resource;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class SubscriptionResource {

    private String endpoint;
    private SubscriptionKeysResource keys;

}
