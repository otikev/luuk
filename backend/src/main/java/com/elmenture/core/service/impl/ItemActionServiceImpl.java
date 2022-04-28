package com.elmenture.core.service.impl;

import com.elmenture.core.model.ItemAction;
import com.elmenture.core.payload.ActionDto;
import com.elmenture.core.repository.ItemActionRepository;
import com.elmenture.core.service.ItemActionService;
import com.elmenture.core.utils.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.elmenture.core.utils.Action.DISLIKE;
import static com.elmenture.core.utils.Action.LIKE;

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
        List<Long> likes = action.getLikes();
        List<Long> dislikes = action.getDislikes();
        System.out.println("Logging actions for user id " + userId);
        if (likes != null) {
            for (Long itemId : likes) {
                logAction(userId, itemId, LIKE);
            }
        }

        if (dislikes != null) {
            for (Long itemId : dislikes) {
                logAction(userId, itemId, DISLIKE);
            }
        }
    }

    @Override
    public ItemAction getAction(Long userId, Long itemId, Action action) {
        return itemActionRepository.findByUserIdAndItemIdAndAction(userId, itemId, action.value());
    }

}
