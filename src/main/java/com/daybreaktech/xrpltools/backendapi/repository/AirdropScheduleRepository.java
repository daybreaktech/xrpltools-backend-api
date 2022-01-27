package com.daybreaktech.xrpltools.backendapi.repository;

import com.daybreaktech.xrpltools.backendapi.domain.AirdropSchedule;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

@EnableCaching
public interface AirdropScheduleRepository extends CrudRepository<AirdropSchedule, Long> {

    @Query("select a from AirdropSchedule a where a.code =:code")
    AirdropSchedule findByCode(@Param("code") String code);

    @Query("select a from AirdropSchedule a where " +
            "lower(a.code) like lower(concat('%', :key,'%')) or " +
            "lower(a.shortDesc) like lower(concat('%', :key,'%'))"
    )
    List<AirdropSchedule> searchByKey(@Param("key") String key);

    @Query("select a from AirdropSchedule a where " +
            "a.trustline.issuerAddress =:key or " +
            "a.trustline.currencyCode =:key or " +
            "lower(a.trustline.name) like lower(concat('%', :key,'%'))"
    )
    List<AirdropSchedule> searchByKeyTrustline(@Param("key") String key);

    @Query("select air from ScheduleCategory sc left join sc.airdropSchedule air where sc is null")
    List<AirdropSchedule> findByUnassignedCategory();

    @Query("select air from ScheduleCategory sc inner join sc.airdropSchedule air order by sc.category ASC, sc.categoryOrder ASC")
    List<AirdropSchedule> findAssignedOrdered();

    @Query("select a from AirdropSchedule a order by a.id desc")
    List<AirdropSchedule> findOrderById();

    @Query("select a from AirdropSchedule a order by a.airdropDate asc nulls last")
    List<AirdropSchedule> findByAirdropDate();

    @Query("select a from AirdropSchedule a where lower(a.tags) like lower(concat('%', :tag,'%'))  order by a.id desc")
    List<AirdropSchedule> findByTag(@Param("tag") String tag);

    @Query("select a.id from AirdropSchedule a")
    List<Long> findByIds();

    @Query("select a from AirdropSchedule a where a.dateAdded >= :pastDate and a.dateAdded is not null order by a.dateAdded asc")
    List<AirdropSchedule> findByDateAddedForPastDays(@Param("pastDate") LocalDateTime pastDate);

}
