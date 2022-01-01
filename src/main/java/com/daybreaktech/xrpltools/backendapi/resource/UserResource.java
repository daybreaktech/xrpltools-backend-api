package com.daybreaktech.xrpltools.backendapi.resource;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class UserResource {

    private Long id;
    private String username;
    private String email;
    private String password;
    private List<String> roles;

}
