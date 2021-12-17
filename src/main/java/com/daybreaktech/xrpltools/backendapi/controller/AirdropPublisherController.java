package com.daybreaktech.xrpltools.backendapi.controller;

import com.daybreaktech.xrpltools.backendapi.service.AirdropPublisherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${v1API}/airdrop/publisher")
public class AirdropPublisherController {

    @Autowired
    private AirdropPublisherService airdropPublisherService;

    @PostMapping("/")
    public void publishAirdrops() throws JsonProcessingException {
        airdropPublisherService.generateAndSave();
    }

    @GetMapping("/")
    public ResponseEntity getOrderedSchedules() throws JsonProcessingException {
        return ResponseEntity.ok(airdropPublisherService.getOrderedSchedules());
    }

}
