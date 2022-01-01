package com.daybreaktech.xrpltools.backendapi.domain;

import lombok.*;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "trustlines")
@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trustline {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String issuerAddress;
    private String currencyCode;

    @Column(name = "issuer_limit")
    private String limit;

    private String twitterUrl;
    private String websiteUrl;

    private LocalDateTime dateAdded;

    @Lazy
    @OneToMany(mappedBy="trustline")
    private List<AirdropSchedule> airdropSchedules;


}
