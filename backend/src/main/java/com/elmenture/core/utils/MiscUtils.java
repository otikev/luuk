package com.elmenture.core.utils;

import java.util.ArrayList;
import java.util.List;

public class MiscUtils {

    public static <T> ArrayList<T> removeDuplicates(List<T> list) {
        ArrayList<T> newList = new ArrayList<T>();

        for (T element : list) {
            if (!newList.contains(element)) {
                newList.add(element);
            }
        }

        return newList;
    }


}
