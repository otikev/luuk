package com.elmenture.core.repository;

import com.elmenture.core.model.ItemAction;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by otikev on 26-Apr-2022
 */

public interface ItemActionRepository extends JpaRepository<ItemAction, Long> {
    ItemAction findByUserIdAndItemIdAndAction(Long userId, Long itemId, Integer action);
}
