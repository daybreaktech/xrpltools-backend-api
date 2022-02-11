package com.daybreaktech.xrpltools.backendapi.controller;

import com.daybreaktech.xrpltools.backendapi.helpers.ResourceResponseUtil;
import com.daybreaktech.xrpltools.backendapi.resource.SubscriptionResource;
import com.daybreaktech.xrpltools.backendapi.service.PushNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${v1API}/notifications")
@CrossOrigin({"${web-ui}", "${web-ui-main}"})
public class PushNotificationController {

    @Autowired
    private PushNotificationService pushNotificationService;

    @PostMapping("/")
    public ResponseEntity subscribe(@RequestBody SubscriptionResource subscriptionResource) {
        pushNotificationService.subscribe(subscriptionResource);
        return ResponseEntity.ok(ResourceResponseUtil.success());
    }

}
