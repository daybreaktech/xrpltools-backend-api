package com.daybreaktech.xrpltools.backendapi.controller;

import com.daybreaktech.xrpltools.backendapi.helpers.ResourceResponseUtil;
import com.daybreaktech.xrpltools.backendapi.resource.PushNotificationScheduleResource;
import com.daybreaktech.xrpltools.backendapi.service.PushNotificationScheduleService;
import com.daybreaktech.xrpltools.backendapi.service.PushNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("${v1API}/notifschedule")
@CrossOrigin({"${web-ui}", "${web-ui-main}", "${web-ui-test}"})
public class PushNotificationScheduleController {

    @Autowired
    private PushNotificationScheduleService pushNotificationScheduleService;

    @Autowired
    private PushNotificationService pushNotificationService;

    @GetMapping("/")
    public ResponseEntity getScheduledNotifications() {
        return ResponseEntity.ok(pushNotificationScheduleService.getScheduledNotifications());
    }

    @PostMapping("/now")
    public ResponseEntity sendNotificationNow(@RequestBody PushNotificationScheduleResource resource) {
        pushNotificationService.pushNotificationFromResource(resource);
        return ResponseEntity.ok(ResourceResponseUtil.success());
    }

    @PostMapping("/")
    public ResponseEntity createScheduledNotification(@RequestBody PushNotificationScheduleResource resource) {
        pushNotificationScheduleService.createScheduledNotification(resource);
        return ResponseEntity.ok(ResourceResponseUtil.success());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteScheduledNotification(@PathVariable("id") Long id) {
        pushNotificationScheduleService.deleteScheduleNotification(id);
        return ResponseEntity.ok(ResourceResponseUtil.success());
    }

}
