package com.daybreaktech.xrpltools.backendapi.repository;

import com.daybreaktech.xrpltools.backendapi.domain.Trustline;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

@EnableCaching
public interface TrustlineRepository extends CrudRepository<Trustline, Long> {

    @Query("select t from Trustline t where t.issuerAddress =:issuerAddress and t.currencyCode =:currencyCode")
    Trustline findTrustlineByAddressAndCode(@Param("issuerAddress") String issuerAddress, @Param("currencyCode") String currencyCode);

    @Query("select t from Trustline t order by t.dateAdded desc")
    List<Trustline> findTrustlinesByDateAdded();

    @Query("select t from Trustline t where t.issuerAddress =:key or t.currencyCode =:key or lower(t.name) like lower(concat('%', :key,'%'))")
    List<Trustline> findTrustlinesBySearchKey(@Param("key") String key);

}
