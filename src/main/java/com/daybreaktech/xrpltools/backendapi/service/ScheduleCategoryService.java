package com.daybreaktech.xrpltools.backendapi.service;

import com.daybreaktech.xrpltools.backendapi.domain.AirdropCategories;
import com.daybreaktech.xrpltools.backendapi.domain.AirdropSchedule;
import com.daybreaktech.xrpltools.backendapi.domain.ScheduleCategory;
import com.daybreaktech.xrpltools.backendapi.repository.AirdropScheduleRepository;
import com.daybreaktech.xrpltools.backendapi.repository.ScheduleCategoryRepository;
import com.daybreaktech.xrpltools.backendapi.resource.ScheduleCategoryResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleCategoryService {

    @Autowired
    private ScheduleCategoryRepository scheduleCategoryRepository;

    @Autowired
    private AirdropScheduleRepository airdropScheduleRepository;

    @Transactional
    public void updateScheduleCategory(List<ScheduleCategoryResource> scheduleCategoryResources) {
        List<ScheduleCategory> categoryList = new ArrayList<>();

        scheduleCategoryRepository.deleteAll();

        scheduleCategoryResources.stream().map(scheduleCategoryResource -> convertToScheduleCategory(scheduleCategoryResource)).forEach(categoryList::add);
        scheduleCategoryRepository.saveAll(categoryList);
    }

    private ScheduleCategory convertToScheduleCategory(ScheduleCategoryResource scheduleCategoryResource) {
        AirdropSchedule airdropSchedule = airdropScheduleRepository.findById(scheduleCategoryResource.getAirdropId()).get();
        ScheduleCategory scheduleCategory = ScheduleCategory.builder()
                .category(AirdropCategories.valueOf(scheduleCategoryResource.getCategory()))
                .airdropSchedule(airdropSchedule)
                .categoryOrder(scheduleCategoryResource.getOrder())
                .build();
        return scheduleCategory;
    }
}
