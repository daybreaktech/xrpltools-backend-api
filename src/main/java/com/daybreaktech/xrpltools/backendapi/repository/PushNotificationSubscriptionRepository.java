package com.daybreaktech.xrpltools.backendapi.repository;

import com.daybreaktech.xrpltools.backendapi.domain.PushNotificationSubscription;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PushNotificationSubscriptionRepository extends CrudRepository<PushNotificationSubscription, Long> {

    @Query("select pns from PushNotificationSubscription pns " +
            "where pns.endpoint = :endpoint and pns.p256dh = :p256dh and pns.auth = :auth")
    PushNotificationSubscription findCountByEndpoint(@Param("endpoint") String endpoint,
                                @Param("p256dh") String p256dh,
                                @Param("auth") String auth);

}
