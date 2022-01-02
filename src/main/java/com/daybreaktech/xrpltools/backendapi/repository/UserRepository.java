package com.daybreaktech.xrpltools.backendapi.repository;

import com.daybreaktech.xrpltools.backendapi.domain.XrplAdminUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<XrplAdminUser, Long> {

    @Query("select u from XrplAdminUser u where u.username =:username or u.email =:email")
    XrplAdminUser findByUsernameOrEmail(@Param("username") String username, @Param("email") String email);

    @Query("select u from XrplAdminUser u where u.username =:username")
    XrplAdminUser findByUsername(@Param("username") String username);
}
