package com.elmenture.core.utils;

public enum Action {
    LIKE(1),
    DISLIKE(2);

    private int value;

    Action(int i) {
        value = i;
    }

    public int value() {
        return value;
    }

    public static Action getAction(int value) {
        for (Action a : Action.values()) {
            if (a.value == value) return a;
        }
        throw new IllegalArgumentException("Action not found");
    }
}
