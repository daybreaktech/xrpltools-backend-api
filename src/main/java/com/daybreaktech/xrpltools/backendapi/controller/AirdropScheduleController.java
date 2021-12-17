package com.daybreaktech.xrpltools.backendapi.controller;

import com.daybreaktech.xrpltools.backendapi.domain.AirdropSchedule;
import com.daybreaktech.xrpltools.backendapi.helpers.ResourceResponseUtil;
import com.daybreaktech.xrpltools.backendapi.resource.AirdropScheduleResource;
import com.daybreaktech.xrpltools.backendapi.service.AirdropScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${v1API}/airdrop/schedules")
public class AirdropScheduleController {

    @Autowired
    private AirdropScheduleService airdropScheduleService;

    @GetMapping("/")
    public ResponseEntity getSchedules() {
        return ResponseEntity.ok(airdropScheduleService.getAirdropSchedules());
    }

    @PostMapping("/")
    public ResponseEntity createSchedule(@RequestBody AirdropScheduleResource airdropScheduleResource) {
        airdropScheduleService.createAirdropSchedule(airdropScheduleResource);
        return ResponseEntity.ok(ResourceResponseUtil.success());
    }

    @PutMapping("/")
    public ResponseEntity updateSchedule(@RequestBody AirdropScheduleResource airdropScheduleResource) {
        airdropScheduleService.createAirdropSchedule(airdropScheduleResource);
        return ResponseEntity.ok(ResourceResponseUtil.success());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteSchedule(@RequestParam("id") Long id) {
        airdropScheduleService.removeAirdropSchedule(id);
        return ResponseEntity.ok(ResourceResponseUtil.success());
    }

}
