package com.daybreaktech.xrpltools.backendapi.dto;

import com.daybreaktech.xrpltools.backendapi.domain.UserStatus;
import com.daybreaktech.xrpltools.backendapi.domain.XrplAdminUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class XrplUserDetails implements UserDetails {

    private XrplAdminUser xrplAdminUser;

    public XrplUserDetails(XrplAdminUser xrplAdminUser) {
        this.xrplAdminUser = xrplAdminUser;
    }

    public Long getId() {
        return xrplAdminUser.getId();
    }

    public String getEmail() {
        return xrplAdminUser.getEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> grantedAuthorities = xrplAdminUser.getRoles().stream()
                .map(role ->
                    new SimpleGrantedAuthority(role.getRole().name())
                ).collect(Collectors.toList());

        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return xrplAdminUser.getPassword();
    }

    @Override
    public String getUsername() {
        return xrplAdminUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !xrplAdminUser.getStatus().equals(UserStatus.LOCKED);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return xrplAdminUser.getStatus().equals(UserStatus.ACTIVE);
    }

    public List<String> getRolesAsStringList() {
        List<String> grantedAuthorities = xrplAdminUser.getRoles().stream()
                .map(role -> role.getRole().name()).collect(Collectors.toList());

        return grantedAuthorities;
    }
}
