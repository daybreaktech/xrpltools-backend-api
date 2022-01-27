package com.daybreaktech.xrpltools.backendapi.service;

import com.daybreaktech.xrpltools.backendapi.domain.*;
import com.daybreaktech.xrpltools.backendapi.dto.AirdropItem;
import com.daybreaktech.xrpltools.backendapi.exceptions.XrplToolsException;
import com.daybreaktech.xrpltools.backendapi.repository.AirdropScheduleRepository;
import com.daybreaktech.xrpltools.backendapi.repository.ScheduleCategoryRepository;
import com.daybreaktech.xrpltools.backendapi.resource.AirdropScheduleResource;
import com.daybreaktech.xrpltools.backendapi.resource.TrustlineResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class AirdropScheduleService {

    @Autowired
    private AirdropScheduleRepository airdropScheduleRepository;

    @Autowired
    private ScheduleCategoryRepository scheduleCategoryRepository;

    @Autowired
    private TrustlineService trustlineService;

    List<AirdropCategories> exculudedCategories = Arrays.asList(AirdropCategories.ARCHIVE, AirdropCategories.TRASH);

    public List<Long> getAllAirdropIds() {
        return airdropScheduleRepository.findByIds(exculudedCategories);
    }


    public AirdropScheduleResource findByCode(String code) throws XrplToolsException {
        AirdropSchedule airdropSchedule = airdropScheduleRepository.findByCode(code);

        if (airdropSchedule == null) {
            throw new XrplToolsException(404, "Airdrop Schedule code does not exist");
        } else {
            return convertToResource(airdropSchedule);
        }
    }

    public List<AirdropScheduleResource> getFeaturedAirdrops() {
        List<AirdropScheduleResource> airdropScheduleResources = new ArrayList<>();
        List<ScheduleCategory> categoryList = scheduleCategoryRepository.findByCategory(AirdropCategories.FEATURED);

        categoryList.stream().map(category -> convertToResource(category.getAirdropSchedule()))
                .forEach(airdropScheduleResources::add);
        return airdropScheduleResources;
    }

    public List<AirdropScheduleResource> getAllAirdropsByAirdropDate() {
        List<AirdropScheduleResource> airdropScheduleResources = new ArrayList<>();

        List<AirdropSchedule> airdropSchedules = airdropScheduleRepository.findByAirdropDate(exculudedCategories);
        airdropSchedules.stream().map(airdropSchedule -> convertToResource(airdropSchedule))
                .forEach(airdropScheduleResources::add);
        return airdropScheduleResources;
    }

    public List<AirdropScheduleResource> getAirdropsFromPastDays(Integer days) {
        List<AirdropScheduleResource> airdropScheduleResources = new ArrayList<>();

        LocalDateTime last7Days = LocalDateTime.now().minusDays(days);
        List<AirdropSchedule> airdropSchedules = airdropScheduleRepository.findByDateAddedForPastDays(last7Days, exculudedCategories);
        airdropSchedules.stream().map(airdropSchedule -> convertToResource(airdropSchedule))
                .forEach(airdropScheduleResources::add);
        return airdropScheduleResources;
    }

    public List<AirdropScheduleResource> getAirdropsByTag(String tag) {
        List<AirdropScheduleResource> airdropScheduleResources = new ArrayList<>();

        List<AirdropSchedule> airdropSchedules = airdropScheduleRepository.findByTag(tag, exculudedCategories);
        airdropSchedules.stream().map(airdropSchedule -> convertToResource(airdropSchedule))
                .forEach(airdropScheduleResources::add);
        return airdropScheduleResources;
    }

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
                .dateAdded(airdropScheduleResource.getDateAdded())
                .refsUrl(airdropScheduleResource.getRefsUrl())
                .formUrl(airdropScheduleResource.getFormUrl())
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
                .dateAdded(airdropSchedule.getDateAdded())
                .category(categoryName)
                .refsUrl(airdropSchedule.getRefsUrl())
                .formUrl(airdropSchedule.getFormUrl())
                .imageUrl(airdropSchedule.getImageUrl())
                .useTrustlineImg(airdropSchedule.getUseTrustlineImg())
                .tags(putAsTagsWithNew(tags, airdropSchedule.getDateAdded()))
                .trustline(convertToResource(airdropSchedule.getTrustline()))
                .useTrustlineImg(airdropSchedule.getUseTrustlineImg())
                .build();
    }

    private String getImageUrl(AirdropSchedule airdropSchedule, Trustline trustline) {
        if (airdropSchedule.getUseTrustlineImg() != null && airdropSchedule.getUseTrustlineImg() == true) {
            if (trustline != null && trustline.getImageUrl() != null && trustline.getImageUrl() != "") {
                return trustline.getImageUrl();
            }
        }

        return airdropSchedule.getImageUrl();
    }

    private List<String> putAsTagsWithNew(List<String> tags, LocalDateTime dateAdded) {
        List<String> newTags = new ArrayList<>();
        List<String> strippedOffTags = tags != null && !tags.isEmpty() ?
                tags.stream().filter(tag -> !"new".equals(tag.toLowerCase(Locale.ROOT))).collect(Collectors.toList()) : null;

        if (dateAdded == null) {
            return strippedOffTags;
        } else {
            if (validateAirdropIfPastSevenDays(dateAdded)) {
                newTags.add("NEW");

                if (strippedOffTags != null && !strippedOffTags.isEmpty()) {
                    newTags.addAll(strippedOffTags);
                }
                return newTags;
            }
            else {
                return strippedOffTags;
            }
        }
    }

    private boolean validateAirdropIfPastSevenDays(LocalDateTime dateAdded) {
        long daysBetween = DAYS.between(dateAdded, LocalDateTime.now());
        return daysBetween <= 7;
    }

    private TrustlineResource convertToResource(Trustline trustline) {
        if (trustline == null) {
            return null;
        } else {
            return TrustlineResource.builder()
                    .id(trustline.getId())
                    .name(trustline.getName())
                    .issuerAddress(trustline.getIssuerAddress())
                    .currencyCode(trustline.getCurrencyCode())
                    .limit(trustline.getLimit())
                    .twitterUrl(trustline.getTwitterUrl())
                    .imageUrl(trustline.getImageUrl())
                    .build();
        }
    }

}
