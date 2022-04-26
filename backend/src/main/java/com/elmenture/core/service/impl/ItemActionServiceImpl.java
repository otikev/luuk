package com.elmenture.core.service.impl;

import com.elmenture.core.model.ItemAction;
import com.elmenture.core.payload.ActionDto;
import com.elmenture.core.repository.ItemActionRepository;
import com.elmenture.core.service.ItemActionService;
import com.elmenture.core.utils.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by otikev on 26-Apr-2022
 */

@Service
public class ItemActionServiceImpl implements ItemActionService {

    @Autowired
    ItemActionRepository itemActionRepository;

    @Override
    public Long logAction(Long userId, Long itemId, Action action) {

        ItemAction itemAction = getAction(userId, itemId, action);
        if (itemAction == null) {
            itemAction = new ItemAction();
            itemAction.setItemId(itemId);
            itemAction.setUserId(userId);
            itemAction.setAction(action.value());
            itemAction.setCount(1);
        } else {
            itemAction.setCount(itemAction.getCount() + 1);
        }
        itemAction = itemActionRepository.save(itemAction);
        return itemAction.getId();
    }

    @Override
    public void logActions(Long userId, ActionDto action) {
        Map<Long, Integer> likes = action.getLikes();
        Map<Long, Integer> dislikes = action.getDislikes();

        if (likes != null) {
            for (Map.Entry<Long, Integer> entry : likes.entrySet()) {
                logAction(userId, entry.getKey(), Action.getAction(entry.getValue()));
            }
        }

        if (dislikes != null) {
            for (Map.Entry<Long, Integer> entry : dislikes.entrySet()) {
                logAction(userId, entry.getKey(), Action.getAction(entry.getValue()));
            }
        }
    }

    @Override
    public ItemAction getAction(Long userId, Long itemId, Action action) {
        return itemActionRepository.findByUserIdAndItemIdAndAction(userId, itemId, action.value());
    }

}
