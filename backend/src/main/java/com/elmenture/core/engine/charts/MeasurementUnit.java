package com.elmenture.core.engine.charts;

public enum MeasurementUnit {
    INT(0),
    US(1),
    UK(2),
    EU(3),
    BUST_CM(4),
    WAIST_CM(5),
    HIPS_CM(6);

    final int val;

    MeasurementUnit(int i) {
        val = i;
    }

    public static MeasurementUnit valueOfLabel(int label) {
        for (MeasurementUnit e : values()) {
            if (e.val == label) {
                return e;
            }
        }
        return null;
    }
}
