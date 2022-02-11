package com.daybreaktech.xrpltools.backendapi.repository;

import com.daybreaktech.xrpltools.backendapi.domain.AirdropNotificationLog;
import com.daybreaktech.xrpltools.backendapi.domain.AirdropNotificationType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AirdropNotificationLogRepository extends CrudRepository<AirdropNotificationLog, Long> {

    @Query("select count(anl) from AirdropNotificationLog anl where anl.airdropCode = :airdropCode and anl.type = :type")
    Integer findLogByAirdropCodeAndType(@Param("airdropCode") String airdropCode, @Param("type") AirdropNotificationType type);



}
