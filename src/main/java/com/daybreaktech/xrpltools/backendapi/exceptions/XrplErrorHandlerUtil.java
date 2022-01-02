package com.daybreaktech.xrpltools.backendapi.exceptions;

import org.springframework.security.authentication.BadCredentialsException;

public class XrplErrorHandlerUtil {

    public static void HandleException(Exception e) throws XrplToolsException {
        if (e instanceof BadCredentialsException) {
            throw new XrplToolsException(403, "Invalid username and password");
        } else {
            throw new XrplToolsException(500, e.getMessage());
        }
    }

}
