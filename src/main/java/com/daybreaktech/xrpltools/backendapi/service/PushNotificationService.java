package com.daybreaktech.xrpltools.backendapi.service;

import com.daybreaktech.xrpltools.backendapi.domain.PushNotificationSubscription;
import com.daybreaktech.xrpltools.backendapi.dto.NotificationMessage;
import com.daybreaktech.xrpltools.backendapi.exceptions.XrplToolsException;
import com.daybreaktech.xrpltools.backendapi.repository.PushNotificationSubscriptionRepository;
import com.daybreaktech.xrpltools.backendapi.resource.PushNotificationScheduleResource;
import com.daybreaktech.xrpltools.backendapi.resource.SubscriptionResource;
import com.google.gson.Gson;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Subscription;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Security;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class PushNotificationService {

    @Value("${web-push.publicKey}")
    private String publicKey;
    @Value("${web-push.privateKey}")
    private String privateKey;

    private PushService pushService;

    @Autowired
    private PushNotificationSubscriptionRepository repository;

    @PostConstruct
    private void postConstruct() throws GeneralSecurityException {
        Security.addProvider(new BouncyCastleProvider());
        pushService = new PushService(publicKey, privateKey);
    }

    public void pushNotificationFromResource(PushNotificationScheduleResource resource) {
        List<PushNotificationSubscription> subscriptionList = (List<PushNotificationSubscription>) repository.findAll();

        subscriptionList.forEach(sub -> {
            sendNotificationFromSubs(sub, resource);
        });
    }

    private void sendNotification(Subscription subscription, String messageJson) {
        try {
            pushService.send(new Notification(subscription, messageJson));
        } catch (GeneralSecurityException | IOException | JoseException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(SubscriptionResource subscriptionResource) {
        PushNotificationSubscription pushNotificationSubscription = PushNotificationSubscription.builder()
                .endpoint(subscriptionResource.getEndpoint())
                .p256dh(subscriptionResource.getKeys().getP256dh())
                .auth(subscriptionResource.getKeys().getAuth())
                .dateSubscribed(LocalDateTime.now())
                .build();
        repository.save(pushNotificationSubscription);
    }

    public void checkSubscription(SubscriptionResource subscriptionResource) throws XrplToolsException {
        PushNotificationSubscription check = repository.findCountByEndpoint(subscriptionResource.getEndpoint(),
                subscriptionResource.getKeys().getP256dh(),
                subscriptionResource.getKeys().getAuth());

        if (check == null) {
            throw new XrplToolsException(401, "Browser is not subscribed");
        }
    }

    public void removeSubscription(SubscriptionResource subscriptionResource) {
        PushNotificationSubscription subscription = repository.findCountByEndpoint(subscriptionResource.getEndpoint(),
                subscriptionResource.getKeys().getP256dh(),
                subscriptionResource.getKeys().getAuth());
        repository.delete(subscription);
    }

    @Async("pushNotifAsyncExecutor")
    public void sendNotificationFromSubs(PushNotificationSubscription sub, Object notificationMessage) {
        Subscription.Keys keys = new Subscription.Keys(sub.getP256dh(), sub.getAuth());
        Subscription subscription = new Subscription(sub.getEndpoint(), keys);
        Gson gson = new Gson();
        String message = gson.toJson(notificationMessage);

        sendNotification(subscription, message);
    }

}
