package com.elmenture.core.payload;

import lombok.Data;

import java.util.Map;

/**
 * Created by otikev on 26-Apr-2022
 */

@Data
public class ActionDto {

    private Map<Long, Integer> likes; //Item,ItemAction

    private Map<Long, Integer> dislikes;//Item,ItemAction
}