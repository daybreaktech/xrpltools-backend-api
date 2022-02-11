package com.daybreaktech.xrpltools.backendapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;


@RestController
@RequestMapping("${v1API}/misc")
@CrossOrigin({"${web-ui}", "${web-ui-main}"})
public class MiscController {

    @GetMapping("/servertime/now")
    public ResponseEntity getServerTime() {
        return ResponseEntity.ok(LocalDateTime.now());
    }

}
