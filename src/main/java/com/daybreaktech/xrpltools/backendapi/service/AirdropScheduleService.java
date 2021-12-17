package com.daybreaktech.xrpltools.backendapi.service;

import com.daybreaktech.xrpltools.backendapi.domain.AirdropSchedule;
import com.daybreaktech.xrpltools.backendapi.domain.ScheduleCategory;
import com.daybreaktech.xrpltools.backendapi.domain.Status;
import com.daybreaktech.xrpltools.backendapi.domain.Trustline;
import com.daybreaktech.xrpltools.backendapi.repository.AirdropScheduleRepository;
import com.daybreaktech.xrpltools.backendapi.repository.ScheduleCategoryRepository;
import com.daybreaktech.xrpltools.backendapi.resource.AirdropScheduleResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AirdropScheduleService {

    @Autowired
    private AirdropScheduleRepository airdropScheduleRepository;

    @Autowired
    private ScheduleCategoryRepository scheduleCategoryRepository;

    @Autowired
    private TrustlineService trustlineService;

    public void removeAirdropSchedule(Long id) {
        AirdropSchedule airdropSchedule = airdropScheduleRepository.findById(id).get();
        airdropScheduleRepository.delete(airdropSchedule);
    }

    private AirdropSchedule updateAirdropSchedule() {
        return null;
    }

    public AirdropSchedule createAirdropSchedule(AirdropScheduleResource airdropScheduleResource) {
        AirdropSchedule airdropSchedule = convertToDomain(airdropScheduleResource);
        AirdropSchedule newAirdropSchedule = airdropScheduleRepository.save(airdropSchedule);
        return newAirdropSchedule;
    }

    public List<AirdropScheduleResource> getAirdropSchedules() {
        List<AirdropScheduleResource> airdropScheduleResources = new ArrayList<>();
        List<AirdropSchedule> airdropSchedules = (List<AirdropSchedule>) airdropScheduleRepository.findAll();
        airdropSchedules.stream().map(airdropSchedule -> convertToResource(airdropSchedule)).forEach(airdropScheduleResources::add);
        return airdropScheduleResources;
    }

    private AirdropSchedule convertToDomain(AirdropScheduleResource airdropScheduleResource) {

        String tags = null;
        if (airdropScheduleResource.getTags() != null &&
                !airdropScheduleResource.getTags().isEmpty()) {
            tags = airdropScheduleResource.getTags().stream().collect(Collectors.joining(","));
        }

        Trustline trustline = null;

        if (airdropScheduleResource.getTrustlineId() != null) {
            trustline = trustlineService.getTrustline(airdropScheduleResource.getTrustlineId());
        }

        return AirdropSchedule.builder()
                .id(airdropScheduleResource.getId())
                .code(airdropScheduleResource.getCode())
                .shortDesc(airdropScheduleResource.getShortDesc())
                .longDesc(airdropScheduleResource.getLongDesc())
                .snapshotDate(airdropScheduleResource.getSnapshotDate())
                .timeZone(airdropScheduleResource.getTimeZone())
                .airdropDate(airdropScheduleResource.getAirdropDate())
                .status(Status.valueOf(airdropScheduleResource.getStatus()))
                .trustline(trustline)
                .tags(tags)
                .build();
    }

    private AirdropScheduleResource convertToResource(AirdropSchedule airdropSchedule) {
        ScheduleCategory scheduleCategory = scheduleCategoryRepository.findByAirdropSchedule(airdropSchedule);

        return AirdropScheduleResource.builder()
                .id(airdropSchedule.getId())
                .code(airdropSchedule.getCode())
                .shortDesc(airdropSchedule.getShortDesc())
                .longDesc(airdropSchedule.getLongDesc())
                .snapshotDate(airdropSchedule.getSnapshotDate())
                .timeZone(airdropSchedule.getTimeZone())
                .airdropDate(airdropSchedule.getAirdropDate())
                .status(airdropSchedule.getStatus().name())
                .category(scheduleCategory.getCategory().name())
                .build();
    }

}
