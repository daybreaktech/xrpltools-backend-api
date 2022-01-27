package com.daybreaktech.xrpltools.backendapi.controller;

import com.daybreaktech.xrpltools.backendapi.exceptions.XrplErrorHandlerUtil;
import com.daybreaktech.xrpltools.backendapi.exceptions.XrplToolsException;
import com.daybreaktech.xrpltools.backendapi.helpers.ResourceResponseUtil;
import com.daybreaktech.xrpltools.backendapi.resource.APIResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(XrplToolsException.class)
    public ResponseEntity handleGeneralException(XrplToolsException e) {
        return ResponseEntity.ok(ResourceResponseUtil.error(e.getCode(), e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse> handleException(Exception e) throws XrplToolsException {
        e.printStackTrace();
        return ResponseEntity.ok(ResourceResponseUtil.error(500, e.toString()));
    }

}
