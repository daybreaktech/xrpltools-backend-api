package com.daybreaktech.xrpltools.backendapi.service;

import com.daybreaktech.xrpltools.backendapi.domain.AirdropSchedule;
import com.daybreaktech.xrpltools.backendapi.domain.ScheduleCategory;
import com.daybreaktech.xrpltools.backendapi.domain.Status;
import com.daybreaktech.xrpltools.backendapi.domain.Trustline;
import com.daybreaktech.xrpltools.backendapi.exceptions.XrplToolsException;
import com.daybreaktech.xrpltools.backendapi.repository.AirdropScheduleRepository;
import com.daybreaktech.xrpltools.backendapi.repository.ScheduleCategoryRepository;
import com.daybreaktech.xrpltools.backendapi.resource.AirdropScheduleResource;
import com.daybreaktech.xrpltools.backendapi.resource.TrustlineResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
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

    public List<AirdropScheduleResource> getUnassignedCategoryAirdrops() {
        List<AirdropScheduleResource> airdropScheduleResources = new ArrayList<>();
        List<AirdropSchedule> airdropSchedules = (List<AirdropSchedule>) airdropScheduleRepository.findAll();
        airdropSchedules.stream().map(airdropSchedule -> convertToResource(airdropSchedule))
                .filter(airdropScheduleResource -> airdropScheduleResource.getCategory() == null)
                .forEach(airdropScheduleResources::add);
        return airdropScheduleResources;
    }

    public List<AirdropScheduleResource> getAssignedCategoryAirdrops() {
        List<AirdropScheduleResource> airdropScheduleResources = new ArrayList<>();
        List<AirdropSchedule> airdropSchedules = (List<AirdropSchedule>) airdropScheduleRepository.findAssignedOrdered();
        airdropSchedules.stream().map(airdropSchedule -> convertToResource(airdropSchedule))
                .forEach(airdropScheduleResources::add);
        return airdropScheduleResources;
    }

    public List<AirdropScheduleResource> searchAirdropSchedule(String key) {
        List<AirdropScheduleResource> airdropScheduleResources = new ArrayList<>();
        List<AirdropSchedule> schedules = airdropScheduleRepository.searchByKey(key);

        if (schedules == null) {
            schedules = airdropScheduleRepository.searchByKeyTrustline(key);
        }

        schedules.stream().map(airdropSchedule -> convertToResource(airdropSchedule)).forEach(airdropScheduleResources::add);
        return airdropScheduleResources;
    }

    public void removeAirdropSchedule(Long id) {
        AirdropSchedule airdropSchedule = airdropScheduleRepository.findById(id).get();
        airdropScheduleRepository.delete(airdropSchedule);
    }

    private AirdropSchedule updateAirdropSchedule() {
        return null;
    }

    public AirdropSchedule createAirdropSchedule(AirdropScheduleResource airdropScheduleResource) throws XrplToolsException {
        AirdropSchedule airdropSchedule = convertToDomain(airdropScheduleResource);
        validateAirdropCode(airdropSchedule);
        AirdropSchedule newAirdropSchedule = airdropScheduleRepository.save(airdropSchedule);
        return newAirdropSchedule;
    }

    private void validateAirdropCode(AirdropSchedule airdropSchedule) throws XrplToolsException {
        AirdropSchedule fetchedAirdropSchedule = airdropScheduleRepository.findByCode(airdropSchedule.getCode());

        if (airdropSchedule.getId() != null && fetchedAirdropSchedule != null) {
            if (!airdropSchedule.getId().equals(fetchedAirdropSchedule.getId())) {
                throw new XrplToolsException(409, "Airdrop Code already exist");
            }
        } else if (fetchedAirdropSchedule != null) {
            throw new XrplToolsException(409, "Airdrop Code already exist");
        }
    }

    public AirdropScheduleResource getAirdropScheduleById(Long id) {
        AirdropSchedule airdropSchedule = airdropScheduleRepository.findById(id).get();
        return convertToResource(airdropSchedule);
    }

    public List<AirdropScheduleResource> getAirdropSchedules() {
        List<AirdropScheduleResource> airdropScheduleResources = new ArrayList<>();
        List<AirdropSchedule> airdropSchedules = (List<AirdropSchedule>) airdropScheduleRepository.findOrderById();
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

        if (airdropScheduleResource.getTrustline() != null && airdropScheduleResource.getTrustline().getId() != null) {
            trustline = trustlineService.getTrustline(airdropScheduleResource.getTrustline().getId());
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
                .refsUrl(airdropScheduleResource.getRefsUrl())
                .imageUrl(airdropScheduleResource.getImageUrl())
                .useTrustlineImg(airdropScheduleResource.getUseTrustlineImg())
                .build();
    }

    private AirdropScheduleResource convertToResource(AirdropSchedule airdropSchedule) {
        String categoryName = airdropSchedule.getScheduleCategory() != null ?
                airdropSchedule.getScheduleCategory().getCategory().name() : null;

        List<String> tags = null;

        if (airdropSchedule.getTags() != null) {
            tags = Arrays.stream(airdropSchedule.getTags().split(",")).collect(Collectors.toList());
        }

        return AirdropScheduleResource.builder()
                .id(airdropSchedule.getId())
                .code(airdropSchedule.getCode())
                .shortDesc(airdropSchedule.getShortDesc())
                .longDesc(airdropSchedule.getLongDesc())
                .snapshotDate(airdropSchedule.getSnapshotDate())
                .timeZone(airdropSchedule.getTimeZone())
                .airdropDate(airdropSchedule.getAirdropDate())
                .status(airdropSchedule.getStatus().name())
                .category(categoryName)
                .refsUrl(airdropSchedule.getRefsUrl())
                .imageUrl(airdropSchedule.getImageUrl())
                .useTrustlineImg(airdropSchedule.getUseTrustlineImg())
                .tags(tags)
                .trustline(convertToResource(airdropSchedule.getTrustline()))
                .build();
    }

    private TrustlineResource convertToResource(Trustline trustline) {
        if (trustline == null) {
            return null;
        } else {
            return TrustlineResource.builder()
                    .id(trustline.getId())
                    .name(trustline.getName())
                    .build();
        }
    }

}
