package com.elmenture.core.payload;

import lombok.Data;

import java.util.List;

/**
 * Created by otikev on 26-Apr-2022
 */

@Data
public class ActionDto {

    private List<Long> likes;

    private List<Long> dislikes;
}