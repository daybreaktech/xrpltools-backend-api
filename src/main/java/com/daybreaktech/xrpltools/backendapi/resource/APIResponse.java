package com.daybreaktech.xrpltools.backendapi.resource;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class APIResponse {

    private ApiResponseStatus status;
    private Integer code;
    private Object data;
    private String errorMessage;

}
