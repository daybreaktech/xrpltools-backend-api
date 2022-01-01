package com.daybreaktech.xrpltools.backendapi.service;

import com.daybreaktech.xrpltools.backendapi.domain.Roles;
import com.daybreaktech.xrpltools.backendapi.domain.UserStatus;
import com.daybreaktech.xrpltools.backendapi.domain.XrplAdminRole;
import com.daybreaktech.xrpltools.backendapi.domain.XrplAdminUser;
import com.daybreaktech.xrpltools.backendapi.exceptions.XrplToolsException;
import com.daybreaktech.xrpltools.backendapi.repository.UserRepository;
import com.daybreaktech.xrpltools.backendapi.resource.UserResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public void registerUser(UserResource userResource) throws XrplToolsException {
        validateUsernameAndEmail(userResource.getUsername(), userResource.getEmail());
        XrplAdminUser xrplAdminUser = resourceToDomain(userResource);
        userRepository.save(xrplAdminUser);
    }

    private XrplAdminUser resourceToDomain(UserResource userResource) {

        XrplAdminUser user = XrplAdminUser.builder()
                .username(userResource.getUsername())
                .email(userResource.getEmail())
                .password(passwordEncoder.encode(userResource.getPassword()))
                .status(UserStatus.ACTIVE)
                .build();

        List<XrplAdminRole> xrplAdminRoleList = new ArrayList<>();

        userResource.getRoles().stream().map(role -> {
            return XrplAdminRole.builder()
                    .role(Roles.valueOf(role))
                    .xrplAdminUser(user)
                    .build();
        }).forEach(xrplAdminRoleList::add);

        user.setRoles(xrplAdminRoleList);

        return user;
    }

    private void validateUsernameAndEmail(String username, String email) throws XrplToolsException {
       XrplAdminUser user = userRepository.findByUsernameOrEmail(username, email);

       if (user != null) {
           throw new XrplToolsException(409, "Username or Email already exist");
       }
    }

}
