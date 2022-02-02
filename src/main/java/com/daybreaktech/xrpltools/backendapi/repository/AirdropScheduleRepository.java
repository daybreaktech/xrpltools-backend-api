package com.daybreaktech.xrpltools.backendapi.repository;

import com.daybreaktech.xrpltools.backendapi.domain.AirdropCategories;
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

    //Publics

    @Query("select a from AirdropSchedule a " +
            "left join a.scheduleCategory s " +
            "where (s is null or s.category not in (:categories)) " +
            "and a.airdropDate is not null " +
            "and a.airdropDate >= current_date " +
            "order by a.airdropDate asc")
    List<AirdropSchedule> findByAirdropDate(@Param("categories") List<AirdropCategories> categories);

    @Query("select a from AirdropSchedule a " +
            "left join a.scheduleCategory s " +
            "where lower(a.tags) like lower(concat('%', :tag,'%')) " +
            "and (s is null or s.category not in (:categories)) " +
            "order by a.id desc")
    List<AirdropSchedule> findByTag(@Param("tag") String tag,
                                    @Param("categories") List<AirdropCategories> categories);

    @Query("select a.id from AirdropSchedule a " +
            "left join a.scheduleCategory s " +
            "where s is null or s.category not in (:categories)")
    List<Long> findByIds(@Param("categories") List<AirdropCategories> categories);

    @Query("select a from AirdropSchedule a " +
            "left join a.scheduleCategory s " +
            "where a.dateAdded >= :pastDate " +
            "and (s is null or s.category not in (:categories)) " +
            "and a.dateAdded is not null " +
            "order by a.dateAdded desc")
    List<AirdropSchedule> findByDateAddedForPastDays(@Param("pastDate") LocalDateTime pastDate,
                                                     @Param("categories") List<AirdropCategories> categories);

}
