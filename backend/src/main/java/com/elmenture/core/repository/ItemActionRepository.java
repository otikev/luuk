package com.elmenture.core.repository;

import com.elmenture.core.model.ItemAction;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by otikev on 26-Apr-2022
 */

public interface ItemActionRepository extends JpaRepository<ItemAction, Long> {
    ItemAction findByUserIdAndItemIdAndAction(Long userId, Long itemId, Integer action);

    List<ItemAction> findAllByUserIdAndAction(Long userId, Integer action, Sort sort);

    @Query(value = "SELECT item_id FROM item_actions WHERE created_at >:date and user_id =:userId ", nativeQuery = true)
    List<Long> getItemsIdsForUserWithDate(long userId, LocalDate date);

    @Query(value = "SELECT item_id FROM item_actions WHERE created_at <:date and action =:action and user_id =:userId LIMIT 10", nativeQuery = true)
    List<Long> getItemsForUserWithDate(int action, long userId, LocalDate date);
}
