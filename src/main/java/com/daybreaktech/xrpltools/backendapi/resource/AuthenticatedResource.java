package com.daybreaktech.xrpltools.backendapi.resource;

import com.daybreaktech.xrpltools.backendapi.dto.XrplUserDetails;

import java.util.List;

public class AuthenticatedResource {

    private Long id;
    private String username;
    private String email;
    private List<String> roles;
    private String jwtToken;

    public AuthenticatedResource(String jwtToken, XrplUserDetails xrplUserDetails) {
        this.id = xrplUserDetails.getId();
        this.username = xrplUserDetails.getUsername();
        this.email = xrplUserDetails.getEmail();
        this.roles = xrplUserDetails.getRolesAsStringList();
        this.jwtToken = jwtToken;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public String getJwtToken() {
        return jwtToken;
    }
}
