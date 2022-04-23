package com.elmenture.core.engine.charts;

/**
 * Created by otikev on 22-Apr-2022
 */

public abstract class ChartUtility {

    protected static boolean unitIsBodyMeasurement(MeasurementUnit unit) {
        if (unit == MeasurementUnit.BUST_CM || unit == MeasurementUnit.WAIST_CM || unit == MeasurementUnit.HIPS_CM) {
            return true;
        } else {
            return false;
        }
    }

    protected static boolean isTooSmall(int value, MeasurementUnit unit, String[][] chart) {
        String maxRange = chart[0][unit.val];
        String[] split = maxRange.split("-");
        if (value < Integer.parseInt(split[0])) {
            return true;
        }
        return false;
    }

    protected static boolean isTooLarge(int value, MeasurementUnit unit, String[][] chart) {
        String maxRange = chart[chart.length - 1][unit.val];
        String[] split = maxRange.split("-");
        if (value > Integer.parseInt(split[1])) {
            return true;
        }
        return false;
    }

    protected static boolean valueIsWithinRange(int value, String range) {
        String[] split = range.split("-");
        if (value >= Integer.parseInt(split[0]) && value <= Integer.parseInt(split[1])) {
            return true;
        }
        return false;
    }
}
