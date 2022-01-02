package com.daybreaktech.xrpltools.backendapi.controller;

import com.daybreaktech.xrpltools.backendapi.dto.XrplUserDetails;
import com.daybreaktech.xrpltools.backendapi.exceptions.XrplToolsException;
import com.daybreaktech.xrpltools.backendapi.helpers.ResourceResponseUtil;
import com.daybreaktech.xrpltools.backendapi.resource.AuthenticatedResource;
import com.daybreaktech.xrpltools.backendapi.resource.LoginResource;
import com.daybreaktech.xrpltools.backendapi.security.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${v1API}/auth")
@CrossOrigin("${web-ui}")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/login/")
    private ResponseEntity login(@RequestBody LoginResource loginResource) throws XrplToolsException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginResource.getUsername(), loginResource.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtService.generateJwtToken(authentication);

        XrplUserDetails xrplUserDetails = (XrplUserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(new AuthenticatedResource(jwt, xrplUserDetails));
    }

}
