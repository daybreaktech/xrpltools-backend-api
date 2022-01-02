package com.daybreaktech.xrpltools.backendapi.security.jwt;

import com.daybreaktech.xrpltools.backendapi.resource.APIResponse;
import com.daybreaktech.xrpltools.backendapi.resource.ApiResponseStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AppEntryPoint implements AuthenticationEntryPoint {

    @SneakyThrows
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_OK);

        APIResponse apiResponse = APIResponse.builder()
                .code(401)
                .status(ApiResponseStatus.FAIL)
                .errorMessage(authException.getMessage())
                .build();

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), apiResponse);
    }
}
