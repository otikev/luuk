package com.elmenture.core.engine.charts;

import java.util.HashMap;
import java.util.Map;

import static com.elmenture.core.engine.charts.MeasurementUnit.*;

/**
 * Created by otikev on 22-Apr-2022
 */

public abstract class ChartUtility {

    protected static boolean unitIsBodyMeasurement(MeasurementUnit unit) {
        if (unit == BUST_CM || unit == WAIST_CM || unit == HIPS_CM) {
            return true;
        } else {
            return false;
        }
    }

    protected static Map<String, String> mapOutOfRangeValues(String value, MeasurementUnit unit, String[][] chart) {
        Map<String, String> sizes = new HashMap<>();
        if (isTooLarge(value, unit, chart)) {
            for (int j = 0; j < chart[chart.length - 1].length; j++) {
                sizes.put(MeasurementUnit.valueOfLabel(j).toString(), chart[chart.length - 1][j]);
            }
            return sizes;
        }

        if (isTooSmall(value, unit, chart)) {
            for (int j = 0; j < chart[0].length; j++) {
                sizes.put(MeasurementUnit.valueOfLabel(j).toString(), chart[0][j]);
            }
            return sizes;
        }

        return new HashMap<>();
    }

    protected static boolean isTooSmall(String value, MeasurementUnit unit, String[][] chart) {
        if (unit == INT) {
            return false;
        }

        if (unitIsBodyMeasurement(unit)) {
            String minRange = chart[0][unit.val];
            String[] split = minRange.split("-");
            if (Integer.parseInt(value) < Integer.parseInt(split[0])) {
                return true;
            }
        } else {
            String min = chart[0][unit.val];
            if (Integer.parseInt(value) < Integer.parseInt(min)) {
                return true;
            }
        }
        return false;
    }

    protected static boolean isTooLarge(String value, MeasurementUnit unit, String[][] chart) {
        if (unit == INT) {
            return false;
        }

        if (unitIsBodyMeasurement(unit)) {
            String maxRange = chart[chart.length - 1][unit.val];
            String[] split = maxRange.split("-");
            if (Integer.parseInt(value) > Integer.parseInt(split[1])) {
                return true;
            }
        } else {
            String max = chart[chart.length - 1][unit.val];
            if (Integer.parseInt(value) > Integer.parseInt(max)) {
                return true;
            }
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
