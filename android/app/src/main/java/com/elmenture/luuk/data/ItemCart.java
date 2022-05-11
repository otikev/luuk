package com.elmenture.luuk.data;

import java.util.ArrayList;
import java.util.List;

import models.Spot;
import utils.LogUtils;

public class ItemCart {
    static LogUtils logUtils = new LogUtils(ItemQueue.class);
    public static List<Spot> spots = new ArrayList<>();

    public static Spot getTopItem() {
        return spots.get(0);
    }

    public static void removeTopItem() {
        spots.remove(0);
        print();
    }

    public static void clear() {
        spots.clear();
        print();
    }

    public static void addItem(Spot spot) {
        spots.add(spot);
        print();
    }

    private static boolean queueContains(Spot _spot) {
        for (Spot spot : spots) {
            if (spot.getItemId() == _spot.getItemId()) {
                return true;
            }
        }
        return false;
    }

    public static void print() {
        String queue = " \n*Cart items*\n[";
        for (Spot spot : spots) {
            queue = queue + "\n" + spot.getItemId() + ". " + spot.getDescription();
        }
        queue = queue + "\n] " + spots.size() + " items";
        logUtils.d(queue);
    }
}
