package com.daybreaktech.xrpltools.backendapi.controller;

import com.daybreaktech.xrpltools.backendapi.helpers.ResourceResponseUtil;
import com.daybreaktech.xrpltools.backendapi.resource.TrustlineResource;
import com.daybreaktech.xrpltools.backendapi.service.TrustlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${v1API}/trustlines")
@CrossOrigin("${web-ui}")
public class TrustlineController {

    @Autowired
    private TrustlineService trustlineService;

    @GetMapping("/")
    private ResponseEntity getTrustlines() {
        return ResponseEntity.ok(trustlineService.getTrustlines());
    }

    @PutMapping("/")
    private ResponseEntity updateTrustline(@RequestBody TrustlineResource trustlineResource) {
        trustlineService.updateTrustlineInfo(trustlineResource);
        return ResponseEntity.ok(ResourceResponseUtil.success());
    }

    @GetMapping("/{id}")
    private ResponseEntity getTrustline(@PathVariable("id") Long id) {
        return ResponseEntity.ok(trustlineService.getTrustlineResource(id));
    }

    @GetMapping("/search")
    private ResponseEntity searchTrustline(@RequestParam("key") String key) {
        return ResponseEntity.ok(trustlineService.searchTrustline(key));
    }

    @PostMapping("/")
    private ResponseEntity saveTrustline(@RequestBody TrustlineResource trustlineResource) throws Exception {
        trustlineService.createTrustline(trustlineResource);
        return ResponseEntity.ok(ResourceResponseUtil.success());
    }

}
