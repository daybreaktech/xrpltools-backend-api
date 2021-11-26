package com.daybreaktech.xrpltools.backendapi.controller;

import com.daybreaktech.xrpltools.backendapi.domain.Sample;
import com.daybreaktech.xrpltools.backendapi.service.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${v1API}/sample")
public class SampleController {

    @Autowired
    private SampleService service;

    @GetMapping("/")
    public ResponseEntity getAllSamples() {
        return ResponseEntity.ok(service.getAllSamples());
    }

    @GetMapping("/{id}")
    public ResponseEntity getAllSamples(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.getSampleById(id));
    }

    @PostMapping("/")
    public ResponseEntity createSample(@RequestBody Sample sample) {
        return ResponseEntity.ok(service.saveSample(sample) != null ? "SUCCESS" : "FAIL");
    }

}
