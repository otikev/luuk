package com.elmenture.core.repository;

import com.elmenture.core.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by otikev on 06-Mar-2022
 */

public interface UserRepository  extends JpaRepository<User, Long> {
    User findBySocialIdAndSocialAccountType(String socialId,String socialAccountType);
}
