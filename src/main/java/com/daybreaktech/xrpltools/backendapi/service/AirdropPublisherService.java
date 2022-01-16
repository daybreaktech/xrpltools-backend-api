package com.daybreaktech.xrpltools.backendapi.service;

import com.daybreaktech.xrpltools.backendapi.domain.AirdropCategories;
import com.daybreaktech.xrpltools.backendapi.domain.AirdropSchedule;
import com.daybreaktech.xrpltools.backendapi.domain.ScheduleCategory;
import com.daybreaktech.xrpltools.backendapi.domain.Trustline;
import com.daybreaktech.xrpltools.backendapi.dto.AirdropBody;
import com.daybreaktech.xrpltools.backendapi.dto.AirdropItem;
import com.daybreaktech.xrpltools.backendapi.exceptions.XrplToolsException;
import com.daybreaktech.xrpltools.backendapi.repository.AirdropScheduleRepository;
import com.daybreaktech.xrpltools.backendapi.repository.ScheduleCategoryRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AirdropPublisherService {

    @Autowired
    private ScheduleCategoryRepository categoryRepository;

    @Autowired
    private AirdropScheduleRepository airdropScheduleRepository;

    public void generateAndSave() throws JsonProcessingException {
        Map<String, List<AirdropItem>> stringListMap = new HashMap<>();
        for (AirdropCategories categories: AirdropCategories.values()) {
            List<AirdropItem> airdropItems = new ArrayList<>();
            List<ScheduleCategory> categoryList = categoryRepository.findByCategory(categories);
            categoryList.stream().map(scheduleCategory -> airdropItem(scheduleCategory))
                    .forEach(airdropItems::add);
            stringListMap.put(categories.name(), airdropItems);
        }

        AirdropBody airdropBody = AirdropBody.builder()
                .items(stringListMap)
                .build();
    }

    public AirdropItem findByCode(String code) throws XrplToolsException {
        AirdropSchedule airdropSchedule = airdropScheduleRepository.findByCode(code);

        if (airdropSchedule == null) {
            throw new XrplToolsException(404, "Airdrop Schedule code does not exist");
        } else {
            return airdropItemSolo(airdropSchedule);
        }
    }

    public AirdropBody getOrderedSchedules() {
        Map<String, List<AirdropItem>> stringListMap = new HashMap<>();
        for (AirdropCategories categories: AirdropCategories.values()) {
            if (!categories.isHidden()) {
                List<AirdropItem> airdropItems = new ArrayList<>();
                List<ScheduleCategory> categoryList = categoryRepository.findByCategory(categories);
                categoryList.stream().map(scheduleCategory -> airdropItem(scheduleCategory))
                        .forEach(airdropItems::add);
                stringListMap.put(categories.name(), airdropItems);
            }
        }

        AirdropBody airdropBody = AirdropBody.builder()
                .items(stringListMap)
                .build();

        return airdropBody;
    }

    private AirdropItem airdropItemSolo(AirdropSchedule airdropSchedule) {
        Trustline trustline = airdropSchedule.getTrustline();

        AirdropItem airdropItem = AirdropItem.builder()
                .id(airdropSchedule.getId())
                .tokenName(trustline != null ? trustline.getName() : null)
                .issuerAddress(trustline != null ? trustline.getIssuerAddress() : null)
                .currencyCode(trustline != null ? trustline.getCurrencyCode() : null)
                .limit(trustline != null ? trustline.getLimit() : null)
                .twitterUrl(trustline != null ? trustline.getTwitterUrl() : null)
                .websiteUrl(trustline != null ? trustline.getWebsiteUrl() : null)
                .refsUrl(airdropSchedule.getRefsUrl())
                .imageUrl(getImageUrl(airdropSchedule, trustline))
                .code(airdropSchedule.getCode())
                .timezone("CST")
                .snapshotDate(airdropSchedule.getSnapshotDate())
                .airdropDate(airdropSchedule.getAirdropDate())
                .shortDesc(airdropSchedule.getShortDesc())
                .longDesc(airdropSchedule.getLongDesc())
                .tags(tagSplitter(airdropSchedule.getTags()))
                .build();

        return airdropItem;
    }

    private AirdropItem airdropItem(ScheduleCategory scheduleCategory) {
        AirdropSchedule airdropSchedule = scheduleCategory.getAirdropSchedule();
        Trustline trustline = airdropSchedule.getTrustline();

        AirdropItem airdropItem = AirdropItem.builder()
                .id(airdropSchedule.getId())
                .tokenName(trustline != null ? trustline.getName() : null)
                .issuerAddress(trustline != null ? trustline.getIssuerAddress() : null)
                .currencyCode(trustline != null ? trustline.getCurrencyCode() : null)
                .limit(trustline != null ? trustline.getLimit() : null)
                .twitterUrl(trustline != null ? trustline.getTwitterUrl() : null)
                .websiteUrl(trustline != null ? trustline.getWebsiteUrl() : null)
                .refsUrl(airdropSchedule.getRefsUrl())
                .imageUrl(getImageUrl(airdropSchedule, trustline))
                .code(airdropSchedule.getCode())
                .timezone("CST")
                .snapshotDate(airdropSchedule.getSnapshotDate())
                .airdropDate(airdropSchedule.getAirdropDate())
                .shortDesc(airdropSchedule.getShortDesc())
                .longDesc(airdropSchedule.getLongDesc())
                .order(scheduleCategory.getCategoryOrder())
                .tags(tagSplitter(airdropSchedule.getTags()))
                .build();

        return airdropItem;
    }

    private String getImageUrl(AirdropSchedule airdropSchedule, Trustline trustline) {
        if (airdropSchedule.getUseTrustlineImg() != null && airdropSchedule.getUseTrustlineImg() == true) {
            if (trustline != null && trustline.getImageUrl() != null && trustline.getImageUrl() != "") {
                return trustline.getImageUrl();
            }
        }

        return airdropSchedule.getImageUrl();
    }

    private List<String> tagSplitter(String tags) {
        if (tags != null && !tags.isEmpty()) {
            String[] splitTags = tags.split(",");
            return Arrays.asList(splitTags);
        }
        return null;
    }



}
