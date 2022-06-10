package com.elmenture.core.repository;

import com.elmenture.core.model.Brand;
import com.elmenture.core.model.Order;
import com.elmenture.core.model.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by otikev on 06-Mar-2022
 */

public interface BrandRepository extends JpaRepository<Brand, Long> {
    @Query(value = "SELECT description FROM brands WHERE awareness =:awareness or perception =:perception", nativeQuery = true)
    List<String> findDescriptionByAwarenessOrPerception(String awareness, String perception);
}
