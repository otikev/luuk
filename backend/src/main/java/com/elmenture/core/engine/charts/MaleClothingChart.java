package com.elmenture.core.engine.charts;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by otikev on 13-Apr-2022
 */
public class MaleClothingChart {
    static String[][] MALE = {
            //"Int","US","UK","EU","BUST(CM)","WAIST(CM)","HIPS(CM)"
            { "XS","30","30","40","","",""},
            { "XS","32","32","42","","",""},
            { "S","34","34","44","","",""},
            { "S","36","36","46","","",""},
            { "M","38","38","48","","",""},
            { "M","40","40","50","","",""},
            { "L","42","42","52","","",""},
            { "L","44","44","54","","",""},
            { "XL","46","46","56","","",""},
            { "XL","48","48","58","","",""},
            { "XXL","50","50","60","","",""},
            { "XXL","52","52","62","","",""}
    };

    public static Map<String,String> getClothingSizes(MeasurementUnit unit, String value){
        String[][] chart = MALE;
        Map<String,String> sizes = new HashMap<>();
        for(int i = chart.length-1; i>=0; i--){
           if(chart[i][unit.val].equalsIgnoreCase(value)){
               for(int j = 0; j< chart[i].length; j++){
                    sizes.put(MeasurementUnit.valueOfLabel(j).toString(), chart[i][j]);
               }
               break;
           }
        }
        return sizes;
    }

}