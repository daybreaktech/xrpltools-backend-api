package com.daybreaktech.xrpltools.backendapi.service;

import com.daybreaktech.xrpltools.backendapi.domain.*;
import com.daybreaktech.xrpltools.backendapi.dto.NotificationDisplay;
import com.daybreaktech.xrpltools.backendapi.repository.PushNotificationScheduleRepository;
import com.daybreaktech.xrpltools.backendapi.repository.PushNotificationSubscriptionRepository;
import com.daybreaktech.xrpltools.backendapi.resource.AirdropScheduleResource;
import com.daybreaktech.xrpltools.backendapi.resource.PushNotificationScheduleResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PushNotificationScheduleService {

    Logger logger = LoggerFactory.getLogger(PushNotificationScheduleService.class);

    @Autowired
    private PushNotificationScheduleRepository pushNotificationScheduleRepository;

    @Autowired
    private PushNotificationSubscriptionRepository pushNotificationSubscriptionRepository;

    @Lazy
    @Autowired
    private AirdropScheduleService airdropScheduleService;

    @Value("${web-ui-main}")
    private String webUIUrl;

    @Autowired
    private PushNotificationService pushNotificationService;

    public PushNotificationSchedule createScheduledNotification(PushNotificationScheduleResource resource) {
        PushNotificationSchedule pushNotificationSchedule = resourceToDomain(resource);
        pushNotificationSchedule.setIsSent(false);
        return pushNotificationScheduleRepository.save(pushNotificationSchedule);
    }

    public void deleteScheduleNotification(Long id) {
        pushNotificationScheduleRepository.deleteById(id);
    }

    public List<PushNotificationScheduleResource> getScheduledNotifications() {
        List<PushNotificationScheduleResource> list = new ArrayList<>();
        List<PushNotificationSchedule> pushNotificationSchedules = pushNotificationScheduleRepository.findBySortedTargetDateTime();

        pushNotificationSchedules.stream().map(pns -> domainToResource(pns)).forEach(list::add);
        return list;
    }

    private PushNotificationSchedule resourceToDomain(PushNotificationScheduleResource resource) {
        return PushNotificationSchedule.builder()
                .id(resource.getId())
                .title(resource.getTitle())
                .description(resource.getDescription())
                .iconUrl(resource.getIconUrl())
                .targetDateTime(resource.getTargetDateTime())
                .actionButtonLabel(resource.getActionButtonLabel())
                .actionButtonLink(resource.getActionButtonLink())
                .pushMode(PushMode.valueOf(resource.getPushMode()))
                .build();
    }

    private PushNotificationScheduleResource domainToResource(PushNotificationSchedule pushNotificationSchedule) {
        return PushNotificationScheduleResource.builder()
                .id(pushNotificationSchedule.getId())
                .title(pushNotificationSchedule.getTitle())
                .description(pushNotificationSchedule.getDescription())
                .iconUrl(pushNotificationSchedule.getIconUrl())
                .targetDateTime(pushNotificationSchedule.getTargetDateTime())
                .actionButtonLabel(pushNotificationSchedule.getActionButtonLabel())
                .actionButtonLink(pushNotificationSchedule.getActionButtonLink())
                .pushMode(pushNotificationSchedule.getPushMode().name())
                .isSent(pushNotificationSchedule.getIsSent())
                .build();
    }

    private PushNotificationSchedule processPns(PushNotificationSchedule pns, List<PushNotificationSubscription> subscriptions) {
        PushNotificationSchedule pushNotificationSchedule = pns;

        if (subscriptions != null && !subscriptions.isEmpty()) {
            pns.setSentRecipients(subscriptions.size());

            subscriptions.forEach(sub -> {
                pushNotificationService.sendNotificationFromSubs(sub, pns);
            });
        }
        pns.setIsSent(true);
        return pushNotificationSchedule;
    }

    private void findAndExecuteNotificationSchedules() {
        List<PushNotificationSchedule> pushNotificationSchedules
                = pushNotificationScheduleRepository.findSchedulesBelowThisTime(LocalDateTime.now());

        if (pushNotificationSchedules != null && !pushNotificationSchedules.isEmpty()) {
            List<PushNotificationSubscription> subscriptions
                    = (List<PushNotificationSubscription>) pushNotificationSubscriptionRepository.findAll();
            pushNotificationSchedules.stream().forEach(pns -> {
                PushNotificationSchedule processedPns = processPns(pns, subscriptions);
                pushNotificationScheduleRepository.save(processedPns);
            });
            logger.debug(String.format("Ended scheduleFixedDelayTask: executed %d notifications", subscriptions.size()));
        }
    }

    @Scheduled(fixedDelay = 20000)
    public void processPushNotificationSchedule() {
        try {
            findAndExecuteNotificationSchedules();
        } catch (Exception e) {
            logger.error("Error findAndExecuteNotificationSchedules() " + e);
        }
    }


}
