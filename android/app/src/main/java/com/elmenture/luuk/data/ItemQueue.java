package com.elmenture.luuk.data;

import java.util.ArrayList;
import java.util.List;

import models.Spot;
import utils.LogUtils;

public class ItemQueue {
    static LogUtils logUtils = new LogUtils(ItemQueue.class);
    public static List<Spot> spots = new ArrayList<>();

    public static Spot getTopItem() {
        return spots.get(0);
    }

    public static void removeTopItem() {
        spots.remove(0);
        print();
    }


    public static boolean isEmpty() {
        return spots.isEmpty();
    }

    public static void clear() {
        spots.clear();
        print();
    }

    public static void addItemsAndFilterItemsInCart(List<Spot> _spots,List<Spot> cartItems) {
        List<Spot> filtered = new ArrayList<>();

        for (Spot spot : _spots) {
            if (!cartContains(spot,cartItems)&&!queueContains(spot)) {
                spots.add(spot);
            }
        }
        print();
    }

    public static void addItems(List<Spot> _spots) {
        for (Spot spot : _spots) {
            if (!queueContains(spot)) {
                spots.add(spot);
            }
        }
        print();
    }

    private static boolean cartContains(Spot spot,List<Spot> cartItems){
        boolean foundInCart = false;
        for (Spot cartItem : cartItems) {
            if(spot.getItemId() == cartItem.getItemId()){
                foundInCart = true;
                break;
            }
        }
        return foundInCart;
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
        String queue = " \n*Queue items*\n[";
        for (Spot spot : spots) {
            queue = queue + "\n" + spot.getItemId() + ". " + spot.getDescription();
        }
        queue = queue + "\n] " + spots.size() + " items";
        logUtils.d(queue);
    }
}