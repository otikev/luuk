package com.elmenture.core.engine.charts;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by otikev on 13-Apr-2022
 */
//https://www.omnicalculator.com/everyday-life/dress-size
public class FemaleClothingChart {
    static String[][] DRESS = {
            //"Int","US","UK","EU","BUST(CM)","WAIST(CM)","HIPS(CM)"
            {"XS", "0", "4", "32", "74-77", "58-61", "80-84"},
            {"XS", "2", "6", "34", "78-81", "62-64", "85-89"},
            {"S", "4", "8", "36", "82-85", "65-68", "90-94"},
            {"S", "6", "10", "38", "86-89", "69-72", "95-97"},
            {"M", "8", "12", "40", "90-93", "73-77", "98-101"},
            {"M", "10", "14", "42", "94-97", "78-81", "102-104"},
            {"L", "12", "16", "44", "98-102", "82-85", "105-108"},
            {"L", "14", "18", "46", "103-107", "86-90", "109-112"},
            {"XL", "16", "20", "48", "108-113", "91-95", "113-116"},
            {"XL", "18", "22", "50", "114-119", "96-102", "117-121"},
            {"XXL", "20", "24", "52", "120-125", "103-108", "122-128"},
            {"XXL", "22", "26", "54", "126-131", "109-114", "129-134"}
    };

    public static Map<String, String> getClothingSizesAndBodyMeasurements(MeasurementUnit unit, String value) {
        if (unitIsBodyMeasurement(unit)) {
            return mapBodyMeasurementToSizes(unit, Integer.parseInt(value));
        } else {
            return mapSizes(unit, value);
        }
    }

    private static Map<String, String> mapSizes(MeasurementUnit unit, String value) {
        String[][] chart = DRESS;
        Map<String, String> sizes = new HashMap<>();
        for (int i = chart.length - 1; i >= 0; i--) {
            if (chart[i][unit.val].equalsIgnoreCase(value)) {
                for (int j = 0; j < chart[i].length; j++) {
                    sizes.put(MeasurementUnit.valueOfLabel(j).toString(), chart[i][j]);
                }
                break;
            }
        }
        return sizes;
    }

    private static Map<String, String> mapBodyMeasurementToSizes(MeasurementUnit unit, int cm) {
        String[][] chart = DRESS;
        Map<String, String> sizes = new HashMap<>();

        if(isTooLarge(cm,unit,chart)){
            for (int j = 0; j < chart[chart.length-1].length; j++) {
                sizes.put(MeasurementUnit.valueOfLabel(j).toString(), chart[chart.length-1][j]);
            }
            return sizes;
        }

        if(isTooSmall(cm,unit,chart)){
            for (int j = 0; j < chart[0].length; j++) {
                sizes.put(MeasurementUnit.valueOfLabel(j).toString(), chart[0][j]);
            }
            return sizes;
        }

        for (int i = chart.length - 1; i >= 0; i--) {
            if (valueIsWithinRange(cm, chart[i][unit.val])) {
                for (int j = 0; j < chart[i].length; j++) {
                    sizes.put(MeasurementUnit.valueOfLabel(j).toString(), chart[i][j]);
                }
                break;
            }
        }

        return sizes;
    }

    private static boolean unitIsBodyMeasurement(MeasurementUnit unit) {
        if (unit == MeasurementUnit.BUST_CM || unit == MeasurementUnit.WAIST_CM || unit == MeasurementUnit.HIPS_CM) {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isTooSmall(int value, MeasurementUnit unit, String[][] chart) {
        String maxRange = chart[0][unit.val];
        String[] split = maxRange.split("-");
        if (value < Integer.parseInt(split[0])) {
            return true;
        }
        return false;
    }

    private static boolean isTooLarge(int value, MeasurementUnit unit, String[][] chart) {
        String maxRange = chart[chart.length-1][unit.val];
        String[] split = maxRange.split("-");
        if (value > Integer.parseInt(split[1])) {
            return true;
        }
        return false;
    }

    private static boolean valueIsWithinRange(int value, String range) {
        String[] split = range.split("-");
        if (value >= Integer.parseInt(split[0]) && value <= Integer.parseInt(split[1])) {
            return true;
        }
        return false;
    }
}