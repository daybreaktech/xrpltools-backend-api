package com.daybreaktech.xrpltools.backendapi.controller;

import com.daybreaktech.xrpltools.backendapi.exceptions.XrplToolsException;
import com.daybreaktech.xrpltools.backendapi.helpers.ResourceResponseUtil;
import com.daybreaktech.xrpltools.backendapi.resource.UserResource;
import com.daybreaktech.xrpltools.backendapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${v1API}/users")
@CrossOrigin("${web-ui}")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register/")
    private ResponseEntity registerUser(@RequestBody UserResource userResource)
            throws XrplToolsException {
        userService.registerUser(userResource);
        return ResponseEntity.ok(ResourceResponseUtil.success());
    }

}
