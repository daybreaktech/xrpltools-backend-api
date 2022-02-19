package com.daybreaktech.xrpltools.backendapi.service;

import com.daybreaktech.xrpltools.backendapi.domain.Trustline;
import com.daybreaktech.xrpltools.backendapi.exceptions.XrplToolsException;
import com.daybreaktech.xrpltools.backendapi.repository.TrustlineRepository;
import com.daybreaktech.xrpltools.backendapi.resource.TrustlineResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TrustlineService {

    @Autowired
    private TrustlineRepository trustlineRepository;

    public TrustlineResource getTrustlineResource(Long id) {
        Trustline trustline = trustlineRepository.findById(id).get();
        return convertToResource(trustline);
    }

    public void updateTrustlineInfo(TrustlineResource trustlineResource) {
        Trustline trustline = trustlineRepository.findById(trustlineResource.getId()).get();
        trustline.setTwitterUrl(trustlineResource.getTwitterUrl());
        trustline.setImageUrl(trustlineResource.getImageUrl());
        trustline.setIsScam(trustlineResource.getIsScam());
        trustlineRepository.save(trustline);
    }

    public List<TrustlineResource> getTrustlines() {
        List<TrustlineResource> trustlineResources = new ArrayList<>();
        List<Trustline> trustlines = (List<Trustline>) trustlineRepository.findTrustlinesByDateAdded();
        trustlines.stream().map(trustline -> convertToResource(trustline)).forEach(trustlineResources::add);
        return trustlineResources;
    }

    public List<TrustlineResource> searchTrustline(String key) {
        List<TrustlineResource> trustlineResources = new ArrayList<>();
        List<Trustline> trustlines = (List<Trustline>) trustlineRepository.findTrustlinesBySearchKey(key);
        trustlines.stream().map(trustline -> convertToResource(trustline)).forEach(trustlineResources::add);
        return trustlineResources;
    }

    private TrustlineResource convertToResource(Trustline trustline) {
        return TrustlineResource.builder()
                .id(trustline.getId())
                .name(trustline.getName())
                .currencyCode(trustline.getCurrencyCode())
                .issuerAddress(trustline.getIssuerAddress())
                .limit(trustline.getLimit())
                .imageUrl(trustline.getImageUrl())
                .twitterUrl(trustline.getTwitterUrl())
                .website(trustline.getWebsiteUrl())
                .isScam(trustline.getIsScam())
                .dateAdded(trustline.getDateAdded())
                .build();
    }

    public Trustline createTrustline(TrustlineResource trustlineResource) throws Exception {

        if (validateIfTrustlineExist(trustlineResource)) {
            throw new XrplToolsException(409,
                    String.format("Trustline/Token %s already exist!",
                            trustlineResource.getName()));
        }

        Trustline trustline = Trustline.builder()
                .name(trustlineResource.getName())
                .currencyCode(trustlineResource.getCurrencyCode())
                .issuerAddress(trustlineResource.getIssuerAddress())
                .limit(trustlineResource.getLimit())
                .twitterUrl(trustlineResource.getTwitterUrl())
                .imageUrl(trustlineResource.getImageUrl())
                .websiteUrl(trustlineResource.getWebsite())
                .isScam(trustlineResource.getIsScam())
                .dateAdded(LocalDateTime.now())
                .build();

        return trustlineRepository.save(trustline);
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
