package com.daybreaktech.xrpltools.backendapi.service;

import com.daybreaktech.xrpltools.backendapi.domain.*;
import com.daybreaktech.xrpltools.backendapi.dto.AirdropItem;
import com.daybreaktech.xrpltools.backendapi.dto.NotificationDisplay;
import com.daybreaktech.xrpltools.backendapi.exceptions.XrplToolsException;
import com.daybreaktech.xrpltools.backendapi.repository.AirdropNotificationLogRepository;
import com.daybreaktech.xrpltools.backendapi.repository.AirdropScheduleRepository;
import com.daybreaktech.xrpltools.backendapi.repository.PushNotificationSubscriptionRepository;
import com.daybreaktech.xrpltools.backendapi.repository.ScheduleCategoryRepository;
import com.daybreaktech.xrpltools.backendapi.resource.AirdropScheduleResource;
import com.daybreaktech.xrpltools.backendapi.resource.TrustlineResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class AirdropScheduleService {

    Logger logger = LoggerFactory.getLogger(AirdropScheduleService.class);

    @Autowired
    private AirdropScheduleRepository airdropScheduleRepository;

    @Autowired
    private ScheduleCategoryRepository scheduleCategoryRepository;

    @Autowired
    private AirdropNotificationLogRepository airdropNotificationLogRepository;

    @Autowired
    private PushNotificationSubscriptionRepository pushNotificationSubscriptionRepository;

    @Autowired
    private TrustlineService trustlineService;

    @Autowired
    private PushNotificationService pushNotificationService;

    @Value("${web-ui-main}")
    private String webUIUrl;

    List<AirdropCategories> excludedCategories = Arrays.asList(AirdropCategories.TRASH);
    List<AirdropCategories> excludedCategoriesForAirdrops = Arrays.asList(AirdropCategories.TRASH, AirdropCategories.HOLDERS);


    public void logAirdropNotification(String airdropCode, AirdropNotificationType type) {
        AirdropNotificationLog notificationLog = AirdropNotificationLog.builder()
                .airdropCode(airdropCode)
                .type(type)
                .dateTimeSent(LocalDateTime.now())
                .build();

        airdropNotificationLogRepository.save(notificationLog);
    }

    public List<Long> getAllAirdropIds() {
        return airdropScheduleRepository.findByIds(excludedCategories);
    }

    public Boolean isAirdropNotificationSentAlready(String airdropCode, AirdropNotificationType notificationType) {
        Integer count = airdropNotificationLogRepository.findLogByAirdropCodeAndType(airdropCode, notificationType);
        return count != null && count > 0;
    }

    public List<AirdropScheduleResource> getAirdropNotification(AirdropNotificationType notificationType) {
        List<AirdropScheduleResource> airdropScheduleResources = new ArrayList<>();
        LocalDateTime start = LocalDateTime.now().minusMinutes(2);
        LocalDateTime end = start.plusDays(2);
        List<AirdropSchedule> airdropSchedules = null;

        if (AirdropNotificationType.AIRDROP_TWO_DAYS_LEFT.equals(notificationType)) {
            airdropSchedules = airdropScheduleRepository.findAirdropDatesBetweenDates(start, end, excludedCategories);
        } else {
            airdropSchedules = airdropScheduleRepository.findSnapshotDatesBetweenDates(start, end, excludedCategories);
        }

        airdropSchedules.stream().map(airdropSchedule -> convertToResource(airdropSchedule))
                .forEach(airdropScheduleResources::add);
        return airdropScheduleResources;
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
        return getCategorizedAirdrops(AirdropCategories.FEATURED);
    }

    public List<AirdropScheduleResource> getHoldersAirdrops() {
        return getCategorizedAirdrops(AirdropCategories.HOLDERS);
    }

    public List<AirdropScheduleResource> getFaucetsAirdrops() {
        return getCategorizedAirdrops(AirdropCategories.FAUCETS);
    }

    public List<AirdropScheduleResource> getCategorizedAirdrops(AirdropCategories airdropCategories) {
        List<AirdropScheduleResource> airdropScheduleResources = new ArrayList<>();
        List<ScheduleCategory> categoryList = scheduleCategoryRepository.findByCategory(airdropCategories);

        categoryList.stream().map(category -> convertToResource(category.getAirdropSchedule()))
                .forEach(airdropScheduleResources::add);
        return airdropScheduleResources;
    }

    public List<AirdropScheduleResource> getAllAirdropsByAirdropDate() {
        List<AirdropScheduleResource> airdropScheduleResources = new ArrayList<>();
        LocalDateTime maxTime = LocalDateTime.now().minusDays(1);

        List<AirdropSchedule> airdropSchedules = airdropScheduleRepository.findByAirdropDate(excludedCategoriesForAirdrops, maxTime);
        airdropSchedules.stream().map(airdropSchedule -> convertToResource(airdropSchedule))
                .forEach(airdropScheduleResources::add);
        return airdropScheduleResources;
    }

    public List<AirdropScheduleResource> getAllExpiredAirdrops() {
        List<AirdropScheduleResource> airdropScheduleResources = new ArrayList<>();

        List<AirdropSchedule> airdropSchedules = airdropScheduleRepository.findByExpiredAirdropDate(excludedCategories, LocalDateTime.now());
        airdropSchedules.stream().map(airdropSchedule -> convertToResource(airdropSchedule))
                .forEach(airdropScheduleResources::add);
        return airdropScheduleResources;
    }

    public List<AirdropScheduleResource> getAirdropsFromPastDays(Integer days) {
        List<AirdropScheduleResource> airdropScheduleResources = new ArrayList<>();

        LocalDateTime last7Days = LocalDateTime.now().minusDays(days).withHour(0).withMinute(0).withSecond(0);;
        List<AirdropSchedule> airdropSchedules = airdropScheduleRepository.findByDateAddedForPastDays(last7Days, excludedCategories);
        airdropSchedules.stream().map(airdropSchedule -> convertToResource(airdropSchedule))
                .forEach(airdropScheduleResources::add);
        return airdropScheduleResources;
    }

    public List<AirdropScheduleResource> getAirdropsByTag(String tag) {
        List<AirdropScheduleResource> airdropScheduleResources = new ArrayList<>();

        List<AirdropSchedule> airdropSchedules = airdropScheduleRepository.findByTag(tag, excludedCategories);
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

        sendNotificationForNewAirdrop(airdropScheduleResource);

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
                .isAirdropExpired(validateExpiredAirdrop(airdropSchedule.getAirdropDate()))
                .isSnapshotExpired(validateExpiredAirdrop(airdropSchedule.getSnapshotDate()))
                .build();
    }

    private Boolean validateExpiredAirdrop(LocalDateTime date) {
        if (date != null) {
            return LocalDateTime.now().isAfter(date);
        }
        return false;
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
                    .isScam(trustline.getIsScam())
                    .build();
        }
    }

    /*Notifications*/
    @Async("asyncExecutor")
    private void sendNotificationForNewAirdrop(AirdropScheduleResource airdropScheduleResource) {
        if (airdropScheduleResource.getId() == null) {
            List<PushNotificationSubscription> subscriptions
                    = (List<PushNotificationSubscription>) pushNotificationSubscriptionRepository.findAll();
            decorateAndSendNotification(airdropScheduleResource, AirdropNotificationType.NEW_AIRDROP, subscriptions);
            logAirdropNotification(airdropScheduleResource.getCode(), AirdropNotificationType.NEW_AIRDROP);
        }
    }

    private void findExecuteAirdropSchedule(AirdropNotificationType type) {
        List<AirdropScheduleResource> airdropScheduleResources = getAirdropNotification(type);

        if (airdropScheduleResources != null && !airdropScheduleResources.isEmpty()) {
            List<PushNotificationSubscription> subscriptions
                    = (List<PushNotificationSubscription>) pushNotificationSubscriptionRepository.findAll();

            List<AirdropScheduleResource> valids = airdropScheduleResources.stream().filter(resource -> {
                return !isAirdropNotificationSentAlready(resource.getCode(), type);
            }).collect(Collectors.toList());

            processResourceForNotificationSend(valids, type, subscriptions);
        }
    }

    private void processResourceForNotificationSend(List<AirdropScheduleResource> airdropScheduleResources,
                                                    AirdropNotificationType type, List<PushNotificationSubscription> subscriptions) {
        if (airdropScheduleResources != null && !airdropScheduleResources.isEmpty()) {
            airdropScheduleResources.stream().forEach(resource -> {
                try {
                    decorateAndSendNotification(resource, type, subscriptions);
                    logAirdropNotification(resource.getCode(), type);
                } catch (Exception e) {
                    logger.error(e.toString());
                }
            });
        }
    }

    private void decorateAndSendNotification(AirdropScheduleResource resource, AirdropNotificationType type,
                                             List<PushNotificationSubscription> subscriptions) {
        NotificationDisplay notificationDisplay = null;

        if (AirdropNotificationType.AIRDROP_TWO_DAYS_LEFT.equals(type)) {
            notificationDisplay = decorateForAirdropDate(resource);
        } else if (AirdropNotificationType.SNAPSHOT_TWO_DAYS_LEFT.equals(type)) {
            notificationDisplay = decorateForSnapshotDate(resource);
        } else {
            notificationDisplay = decorateForNewAirdrop(resource);
        }

        processSendNotification(notificationDisplay, subscriptions);
    }

    private NotificationDisplay decorateForAirdropDate(AirdropScheduleResource resource) {
        NotificationDisplay notificationDisplay = NotificationDisplay.builder()
                .title(generateTitle(resource, "Airdrop Alert!"))
                .description(generateBody(resource.getTrustline(), "Airdrop", resource.getSnapshotDate()))
                .iconUrl("images/xrp-airdrop-icon.png")
                .actionButtonLabel("More Details")
                .actionButtonLink(generateAirdropDetailsUrl(resource.getCode()))
                .build();
        return notificationDisplay;
    }

    private NotificationDisplay decorateForSnapshotDate(AirdropScheduleResource resource) {
        NotificationDisplay notificationDisplay = NotificationDisplay.builder()
                .title(generateTitle(resource, "Snapshot update!"))
                .description(generateBody(resource.getTrustline(), "Snapshot", resource.getSnapshotDate()))
                .iconUrl("images/xrp-airdrop-icon.png")
                .actionButtonLabel("More Details")
                .actionButtonLink(generateAirdropDetailsUrl(resource.getCode()))
                .build();
        return notificationDisplay;
    }

    private NotificationDisplay decorateForNewAirdrop(AirdropScheduleResource resource) {
        NotificationDisplay notificationDisplay = NotificationDisplay.builder()
                .title("New Airdrop Alert!")
                .description(generateBody(resource.getTrustline(), "Airdrop", resource.getSnapshotDate()))
                .iconUrl("images/xrp-airdrop-icon.png")
                .actionButtonLabel("More Details")
                .actionButtonLink(generateAirdropDetailsUrl(resource.getCode()))
                .build();
        return notificationDisplay;
    }

    private String generateAirdropDetailsUrl(String airdropCode) {
        return String.format("%s#/schedule?code=%s&route=airdrops", webUIUrl, airdropCode);
    }

    private String generateBody(TrustlineResource trustlineResource, String mode, LocalDateTime date) {
        return String.format("%s %s scheduled to take place in %s",
                generateTokenName(trustlineResource),
                        mode, generateDate(date));
    }

    @Deprecated
    private String getImageUrl(AirdropScheduleResource resource) {
        if (resource.getUseTrustlineImg() != null && resource.getUseTrustlineImg()) {

            if (resource.getTrustline() != null && resource.getTrustline().getImageUrl()
                    != null && resource.getTrustline().getImageUrl() != "") {
                return resource.getTrustline().getImageUrl();
            }
        }

        return resource.getImageUrl();
    }

    private String generateTitle(AirdropScheduleResource resource, String titlePrefix) {
        return String.format("%s %s", generateTokenName(resource.getTrustline()), titlePrefix);
    }

    private String generateTokenName(TrustlineResource trustlineResource) {
        if (trustlineResource != null) {
            return trustlineResource.getName();
        } else {
            return "[No-Token]";
        }
    }

    private String generateDate(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        return date.format(formatter);
    }

    private void processSendNotification(Object obj, List<PushNotificationSubscription> subscriptions) {
        if (subscriptions != null && !subscriptions.isEmpty()) {

            subscriptions.forEach(sub -> {
                pushNotificationService.sendNotificationFromSubs(sub, obj);
            });
        }
    }

    @Scheduled(fixedDelay = 600000)
    public void processAutoSendNotificationAirdropDate() {
        try {
            findExecuteAirdropSchedule(AirdropNotificationType.AIRDROP_TWO_DAYS_LEFT);
        } catch (Exception e) {
            logger.error("Error processAutoSendNotificationAirdropDate() " + e);
        }
    }

    @Scheduled(fixedDelay = 600000)
    public void processAutoSendNotificationSnapshotDateDate() {
        try {
            findExecuteAirdropSchedule(AirdropNotificationType.SNAPSHOT_TWO_DAYS_LEFT);
        } catch (Exception e) {
            logger.error("Error processAutoSendNotificationSnapshotDateDate() " + e);
        }
    }

}
