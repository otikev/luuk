package com.elmenture.core.service;

import com.elmenture.core.model.ItemAction;
import com.elmenture.core.payload.ActionDto;
import com.elmenture.core.utils.Action;

import java.time.LocalDate;
import java.util.List;

public interface ItemActionService {

    Long logAction(Long userId, Long itemId, Action action);

    void logActions(Long userId, ActionDto action);

    ItemAction getAction(Long userId, Long itemId, Action itemAction);

    List<Long> getAllItemsForUserAction(Action action, Long userId);

    List<Long> getItemIdsForUserWithDateGreaterThanOrEqualTo(long userId, LocalDate date);

    List<Long> getItemsForUserWithDateLessThan(Action action, long userId, LocalDate date,long limit);
}
