package com.daybreaktech.xrpltools.backendapi.service;

import com.daybreaktech.xrpltools.backendapi.domain.XrplAdminUser;
import com.daybreaktech.xrpltools.backendapi.dto.XrplUserDetails;
import com.daybreaktech.xrpltools.backendapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class XrplUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        XrplAdminUser xrplAdminUser = userRepository.findByUsername(username);
        return new XrplUserDetails(xrplAdminUser);
    }



}
