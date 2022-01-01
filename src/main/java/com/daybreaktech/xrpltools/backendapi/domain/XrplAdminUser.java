package com.daybreaktech.xrpltools.backendapi.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "xrpl_users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class XrplAdminUser {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;

    @Enumerated(value = EnumType.STRING)
    private UserStatus status;

    @OneToMany(mappedBy = "xrplAdminUser", cascade = CascadeType.ALL)
    private List<XrplAdminRole> roles;

}
