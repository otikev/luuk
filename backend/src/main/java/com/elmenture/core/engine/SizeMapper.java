package com.elmenture.core.engine;

import com.elmenture.core.engine.charts.FemaleClothingChart;
import com.elmenture.core.model.BodyMeasurement;
import com.elmenture.core.model.ClothingSize;
import com.elmenture.core.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.elmenture.core.engine.charts.MeasurementUnit.*;

/**
 * Created by otikev on 13-Apr-2022
 */

public class SizeMapper {

    public static Map<String, String> mappedSizesForUser(User user) {
        List<Map<String, String>> possibilities = new ArrayList<>();

        BodyMeasurement bodyMeasurement = user.getBodyMeasurement();
        if (bodyMeasurement != null) {
            Integer chest = bodyMeasurement.getChest_cm();
            Integer waist = bodyMeasurement.getWaist_cm();
            Integer hips = bodyMeasurement.getHips_cm();

            if (chest != null && chest > 0) {
                Map<String, String> values = FemaleClothingChart.getClothingSizesAndBodyMeasurements(BUST_CM, String.valueOf(chest));
                possibilities.add(values);
            }

            if (waist != null && waist > 0) {
                Map<String, String> values = FemaleClothingChart.getClothingSizesAndBodyMeasurements(WAIST_CM, String.valueOf(waist));
                possibilities.add(values);
            }

            if (hips != null && hips > 0) {
                Map<String, String> values = FemaleClothingChart.getClothingSizesAndBodyMeasurements(HIPS_CM, String.valueOf(hips));
                possibilities.add(values);
            }
        }


        ClothingSize clothingSize = user.getClothingSize();
        if (clothingSize != null) {
            String international = clothingSize.getInternational();
            Integer us = clothingSize.getUs();
            Integer uk = clothingSize.getUk();
            Integer eu = clothingSize.getEu();
            if (international != null) {
                Map<String, String> values = FemaleClothingChart.getClothingSizesAndBodyMeasurements(INT, international);
                possibilities.add(values);
            }

            if (us != null) {
                Map<String, String> values = FemaleClothingChart.getClothingSizesAndBodyMeasurements(US, String.valueOf(us));
                possibilities.add(values);
            }

            if (uk != null) {
                Map<String, String> values = FemaleClothingChart.getClothingSizesAndBodyMeasurements(UK, String.valueOf(uk));
                possibilities.add(values);
            }

            if (eu != null) {
                Map<String, String> values = FemaleClothingChart.getClothingSizesAndBodyMeasurements(EU, String.valueOf(eu));
                possibilities.add(values);
            }
        }

        if (possibilities.size() == 1) {
            return possibilities.get(0);
        }else if(possibilities.size() == 0){
            return FemaleClothingChart.getMidSize();
        }

        //FIND THE LARGEST MAPPING
        Map<String, String> largest = null;
        for (Map<String, String> mapping : possibilities) {
            if (largest == null) {
                largest = mapping;
            } else {
                if (largest.size() > 0) {
                    int _us = Integer.parseInt(largest.get(US.name()));
                    if (Integer.parseInt(mapping.get(US.name())) > _us) {
                        largest = mapping;
                    }
                }
            }
        }

        return largest;
    }
}
