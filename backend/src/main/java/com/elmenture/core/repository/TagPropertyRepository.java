package com.elmenture.core.repository;

import com.elmenture.core.model.TagProperty;
import com.elmenture.core.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by otikev on 06-Mar-2022
 */

public interface TagPropertyRepository extends JpaRepository<TagProperty, Long> {

}
