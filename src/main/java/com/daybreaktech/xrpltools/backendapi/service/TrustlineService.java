package com.daybreaktech.xrpltools.backendapi.service;

import com.daybreaktech.xrpltools.backendapi.domain.Trustline;
import com.daybreaktech.xrpltools.backendapi.repository.TrustlineRepository;
import com.daybreaktech.xrpltools.backendapi.resource.TrustlineResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TrustlineService {

    @Autowired
    private TrustlineRepository trustlineRepository;

    public List<TrustlineResource> getTrustlines() {
        List<TrustlineResource> trustlineResources = new ArrayList<>();
        List<Trustline> trustlines = (List<Trustline>) trustlineRepository.findAll();
        trustlines.stream().map(trustline -> convertToResource(trustline)).forEach(trustlineResources::add);
        return trustlineResources;
    }

    private TrustlineResource convertToResource(Trustline trustline) {
        return TrustlineResource.builder()
                .id(trustline.getId())
                .currencyCode(trustline.getCurrencyCode())
                .issuerAddress(trustline.getIssuerAddress())
                .twitterUrl(trustline.getTwitterUrl())
                .website(trustline.getWebsiteUrl())
                .build();
    }

    public Trustline createTrustline(TrustlineResource trustlineResource) throws Exception {
        if (!validateIfTrustlineExist(trustlineResource)) {
            Trustline trustline = Trustline.builder()
                    .currencyCode(trustlineResource.getCurrencyCode())
                    .issuerAddress(trustlineResource.getIssuerAddress())
                    .limit(trustlineResource.getLimit())
                    .twitterUrl(trustlineResource.getTwitterUrl())
                    .websiteUrl(trustlineResource.getWebsite())
                    .build();

            return trustlineRepository.save(trustline);
        } else {
            throw new Exception("Trustline address and currency code already exist");
        }
    }

    public boolean validateIfTrustlineExist(TrustlineResource trustlineResource) {
        Trustline trustline = trustlineRepository.findTrustlineByAddressAndCode
                (trustlineResource.getIssuerAddress(), trustlineResource.getCurrencyCode());

        return trustline != null;
    }

    public Trustline editTrustline(TrustlineResource trustlineResource) {
        Trustline trustline = Trustline.builder()
                .id(trustlineResource.getId())
                .currencyCode(trustlineResource.getCurrencyCode())
                .issuerAddress(trustlineResource.getIssuerAddress())
                .limit(trustlineResource.getLimit())
                .twitterUrl(trustlineResource.getTwitterUrl())
                .websiteUrl(trustlineResource.getWebsite())
                .build();

        return trustlineRepository.save(trustline);
    }

    public Trustline getTrustline(Long id) {
        return trustlineRepository.findById(id).get();
    }


}
