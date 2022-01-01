package com.daybreaktech.xrpltools.backendapi.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "xrpl_users_role")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class XrplAdminRole {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private Roles role;

    @ManyToOne
    @JoinColumn(name="user_id")
    private XrplAdminUser xrplAdminUser;

}
