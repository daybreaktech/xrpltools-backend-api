package com.daybreaktech.xrpltools.backendapi.repository;

import com.daybreaktech.xrpltools.backendapi.domain.PushNotificationSchedule;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PushNotificationScheduleRepository extends CrudRepository<PushNotificationSchedule, Long> {

    @Query("select pns from PushNotificationSchedule pns order by pns.targetDateTime desc")
    List<PushNotificationSchedule> findBySortedTargetDateTime();

    @Query("select pns from PushNotificationSchedule pns where pns.targetDateTime < :dateTimeNow")
    List<PushNotificationSchedule> findSchedulesBelowThisTime(@Param("dateTimeNow") LocalDateTime dateTimeNow);

}
