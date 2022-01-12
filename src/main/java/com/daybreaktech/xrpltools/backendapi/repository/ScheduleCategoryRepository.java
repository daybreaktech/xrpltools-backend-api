package com.daybreaktech.xrpltools.backendapi.repository;

import com.daybreaktech.xrpltools.backendapi.domain.AirdropCategories;
import com.daybreaktech.xrpltools.backendapi.domain.AirdropSchedule;
import com.daybreaktech.xrpltools.backendapi.domain.ScheduleCategory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@EnableCaching
public interface ScheduleCategoryRepository extends CrudRepository<ScheduleCategory, Long> {

    @Query("select sc from ScheduleCategory sc where sc.category =:category order by sc.categoryOrder asc")
    List<ScheduleCategory> findByCategory(@Param("category") AirdropCategories category);

    @Query("select sc from ScheduleCategory sc where sc.airdropSchedule =:airdropSchedule")
    ScheduleCategory findByAirdropSchedule(@Param("airdropSchedule") AirdropSchedule airdropSchedule);

    @Query("select count(sc) from ScheduleCategory sc where sc.category =:category")
    Integer countByCategory(@Param("category") AirdropCategories category);

}
