package com.daybreaktech.xrpltools.backendapi.controller;

import com.daybreaktech.xrpltools.backendapi.resource.TrustlineResource;
import com.daybreaktech.xrpltools.backendapi.service.TrustlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${v1API}/trustlines")
public class TrustlineController {

    @Autowired
    private TrustlineService trustlineService;

    @GetMapping("/")
    private ResponseEntity getTrustlines() {
        return ResponseEntity.ok(trustlineService.getTrustlines());
    }

    @PostMapping
    private ResponseEntity saveTrustline(@RequestBody TrustlineResource trustlineResource) throws Exception {
        if (trustlineResource.getId() == null) {
            return ResponseEntity.ok(trustlineService.createTrustline(trustlineResource));
        } else {
            return ResponseEntity.ok(trustlineService.editTrustline(trustlineResource));
        }
    }

}
