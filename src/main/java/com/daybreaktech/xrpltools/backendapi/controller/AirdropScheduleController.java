package com.daybreaktech.xrpltools.backendapi.controller;

import com.daybreaktech.xrpltools.backendapi.exceptions.XrplToolsException;
import com.daybreaktech.xrpltools.backendapi.helpers.ResourceResponseUtil;
import com.daybreaktech.xrpltools.backendapi.resource.AirdropScheduleResource;
import com.daybreaktech.xrpltools.backendapi.service.AirdropScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("${v1API}/airdrop/schedules")
@CrossOrigin("${web-ui}")
public class AirdropScheduleController {

    @Autowired
    private AirdropScheduleService airdropScheduleService;

    @GetMapping("/")
    public ResponseEntity getSchedules() {
        return ResponseEntity.ok(airdropScheduleService.getAirdropSchedules());
    }

    @GetMapping("/{id}")
    public ResponseEntity getSchedulesById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(airdropScheduleService.getAirdropScheduleById(id));
    }

    @PostMapping("/")
    public ResponseEntity createSchedule(@RequestBody AirdropScheduleResource airdropScheduleResource)
            throws XrplToolsException {
        airdropScheduleService.createAirdropSchedule(airdropScheduleResource);
        return ResponseEntity.ok(ResourceResponseUtil.success());
    }

    @GetMapping("/category/unassigned")
    private ResponseEntity searchUnAssignedAirdropSchedule() {
        return ResponseEntity.ok(airdropScheduleService.getUnassignedCategoryAirdrops());
    }

    @GetMapping("/category/assigned")
    private ResponseEntity searchAssignedAirdropSchedule() {
        return ResponseEntity.ok(airdropScheduleService.getAssignedCategoryAirdrops());
    }

    @GetMapping("/search")
    private ResponseEntity searchAirdropSchedule(@RequestParam("key") String key) {
        return ResponseEntity.ok(airdropScheduleService.searchAirdropSchedule(key));
    }

    @PutMapping("/")
    public ResponseEntity updateSchedule(@RequestBody AirdropScheduleResource airdropScheduleResource)
            throws XrplToolsException {
        airdropScheduleService.createAirdropSchedule(airdropScheduleResource);

        return ResponseEntity.ok(ResourceResponseUtil.success());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteSchedule(@PathVariable("id") Long id) {
        airdropScheduleService.removeAirdropSchedule(id);
        return ResponseEntity.ok(ResourceResponseUtil.success());
    }

    @GetMapping("/published/ids")
    public ResponseEntity getAllAirdropIds() {
        return ResponseEntity.ok(airdropScheduleService.getAllAirdropIds());
    }

    @GetMapping("/published/calendar/airdrop")
    public ResponseEntity getAirdropsByAirdropDate() {
        return ResponseEntity.ok(airdropScheduleService.getAllAirdropsByAirdropDate());
    }

    @GetMapping("/published/tag/{tag}")
    public ResponseEntity getAirdropsByTag(@PathVariable("tag") String tag) {
        return ResponseEntity.ok(airdropScheduleService.getAirdropsByTag(tag));
    }

    @GetMapping("/published/calendar/added")
    public ResponseEntity getRecentlyAdded(@RequestParam(name = "days", required = true) Integer days) {
        return ResponseEntity.ok(airdropScheduleService.getAirdropsFromPastDays(days));
    }

}
