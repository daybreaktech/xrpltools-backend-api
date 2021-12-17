package com.daybreaktech.xrpltools.backendapi.controller;

import com.daybreaktech.xrpltools.backendapi.helpers.ResourceResponseUtil;
import com.daybreaktech.xrpltools.backendapi.resource.ScheduleCategoryResource;
import com.daybreaktech.xrpltools.backendapi.service.ScheduleCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${v1API}/airdrop/category")
public class ScheduleCategoryController {

    @Autowired
    private ScheduleCategoryService scheduleCategoryService;

    public ResponseEntity updateScheduleCategory(@RequestBody List<ScheduleCategoryResource> scheduleCategoryResources) {
        scheduleCategoryService.updateScheduleCategory(scheduleCategoryResources);
        return ResponseEntity.ok(ResourceResponseUtil.success());
    }

}
