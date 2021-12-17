package com.daybreaktech.xrpltools.backendapi.helpers;

import com.daybreaktech.xrpltools.backendapi.resource.APIResponse;
import com.daybreaktech.xrpltools.backendapi.resource.ApiResponseStatus;

public class ResourceResponseUtil {

    private ResourceResponseUtil(){}

    public static APIResponse forObject(Object obj) {
        return null;
    }

    public static APIResponse success() {
        return APIResponse.builder()
                .code(200)
                .status(ApiResponseStatus.SUCCESS)
                .build();
    }

}
