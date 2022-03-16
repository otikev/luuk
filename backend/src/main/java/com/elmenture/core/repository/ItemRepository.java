package com.elmenture.core.repository;

import com.elmenture.core.model.Item;
import com.elmenture.core.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by otikev on 06-Mar-2022
 */

public interface ItemRepository extends JpaRepository<Item, Long> {
}
