package com.daybreaktech.xrpltools.backendapi.controller;

import com.daybreaktech.xrpltools.backendapi.exceptions.XrplToolsException;
import com.daybreaktech.xrpltools.backendapi.service.AirdropPublisherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${v1API}/airdrop/publisher")
@CrossOrigin("${web-ui-main}")
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

    @GetMapping("/{code}")
    public ResponseEntity getScheduleByCode(@PathVariable("code") String code) throws XrplToolsException {
        return ResponseEntity.ok(airdropPublisherService.findByCode(code));
    }

}
