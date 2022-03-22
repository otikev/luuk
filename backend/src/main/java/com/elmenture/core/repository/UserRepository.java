package com.elmenture.core.repository;

import com.elmenture.core.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by otikev on 06-Mar-2022
 */

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsernameAndSocialAccountType(String username, String socialAccountType);

    User findByAuthToken(String authToken);

    Optional<User> findByUsername(String username);// socialId == username
}
