package com.elmenture.core.repository;

import com.elmenture.core.model.ItemProperty;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by otikev on 18-Mar-2022
 */

public interface ItemPropertyRepository extends JpaRepository<ItemProperty, Long> {
}
