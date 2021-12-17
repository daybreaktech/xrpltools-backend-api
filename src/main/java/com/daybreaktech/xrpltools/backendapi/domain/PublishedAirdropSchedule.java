package com.daybreaktech.xrpltools.backendapi.domain;

import lombok.*;

import javax.persistence.*;
import java.sql.Blob;

@Entity
@Table(name = "published_airdrop_schedule")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublishedAirdropSchedule {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Lob
    private Blob content;


}
