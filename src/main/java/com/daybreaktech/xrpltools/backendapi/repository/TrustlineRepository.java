package com.daybreaktech.xrpltools.backendapi.repository;

import com.daybreaktech.xrpltools.backendapi.domain.Trustline;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface TrustlineRepository extends CrudRepository<Trustline, Long> {

    @Query("select t from Trustline t where t.issuerAddress =:issuerAddress and t.currencyCode =:currencyCode")
    Trustline findTrustlineByAddressAndCode(@Param("issuerAddress") String issuerAddress, @Param("currencyCode") String currencyCode);

}
