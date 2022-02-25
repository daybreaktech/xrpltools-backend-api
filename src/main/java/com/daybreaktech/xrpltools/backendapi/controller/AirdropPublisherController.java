package com.daybreaktech.xrpltools.backendapi.controller;

import com.daybreaktech.xrpltools.backendapi.exceptions.XrplToolsException;
import com.daybreaktech.xrpltools.backendapi.service.AirdropPublisherService;
import com.daybreaktech.xrpltools.backendapi.service.AirdropScheduleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${v1API}/airdrop/publisher")
@CrossOrigin({"${web-ui}", "${web-ui-main}"})
public class AirdropPublisherController {

    @Autowired
    private AirdropPublisherService airdropPublisherService;

    @Autowired
    private AirdropScheduleService airdropScheduleService;

    @Deprecated
    @PostMapping("/")
    public void publishAirdrops() throws JsonProcessingException {
        airdropPublisherService.generateAndSave();
    }

    @Deprecated
    @GetMapping("/")
    public ResponseEntity getOrderedSchedules() throws JsonProcessingException {
        return ResponseEntity.ok(airdropPublisherService.getOrderedSchedules());
    }

    @Deprecated
    @GetMapping("/{code}")
    public ResponseEntity getScheduleByCodeOld(@PathVariable("code") String code) throws XrplToolsException {
        return ResponseEntity.ok(airdropPublisherService.findByCode(code));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity getScheduleByCodeNew(@PathVariable("code") String code) throws XrplToolsException {
        return ResponseEntity.ok(airdropScheduleService.findByCode(code));
    }

    @GetMapping("/featured")
    public ResponseEntity getFeaturedAirdrops() {
        return ResponseEntity.ok(airdropScheduleService.getFeaturedAirdrops());
    }

    @GetMapping("/holders")
    public ResponseEntity getHolderAirdrops() {
        return ResponseEntity.ok(airdropScheduleService.getHoldersAirdrops());
    }

    @GetMapping("/faucets")
    public ResponseEntity getFaucetsAirdrop() {
        return ResponseEntity.ok(airdropScheduleService.getFaucetsAirdrops());
    }

    @GetMapping("/calendar/airdrop")
    public ResponseEntity getAirdropsByAirdropDate() {
        return ResponseEntity.ok(airdropScheduleService.getAllAirdropsByAirdropDate());
    }

    @GetMapping("/calendar/expired")
    public ResponseEntity getExpiredAirdrops() {
        return ResponseEntity.ok(airdropScheduleService.getAllExpiredAirdrops());
    }

    @GetMapping("/calendar/added")
    public ResponseEntity getRecentlyAdded(@RequestParam(name = "days", required = true) Integer days) {
        return ResponseEntity.ok(airdropScheduleService.getAirdropsFromPastDays(days));
    }

    @GetMapping("/tags/{tag}")
    public ResponseEntity getAirdropsByTag(@PathVariable("tag") String tag) {
        return ResponseEntity.ok(airdropScheduleService.getAirdropsByTag(tag));
    }

    @GetMapping("/ids")
    public ResponseEntity getAllAirdropIds() {
        return ResponseEntity.ok(airdropScheduleService.getAllAirdropIds());
    }



}
