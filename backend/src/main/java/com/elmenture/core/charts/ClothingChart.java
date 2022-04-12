package com.elmenture.core.charts;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by otikev on 13-Apr-2022
 */
//https://www.blitzresults.com/en/european-sizes/#how-to-convert-european-clothing-sizes
public class ClothingChart {
    static String[][] FEMALE = {
            //"Int","US","UK","EU"
            { "XS","0","4","30"},
            { "XS","2","6","32"},
            { "XS","4","8","34"},
            { "S","6","10","36"},
            { "S","8","12","38"},
            { "M","10","14","40"},
            { "M","12","16","42"},
            { "L","14","18","44"},
            { "L","16","20","46"},
            { "XL","18","22","48"},
            { "XL","20","24","50"},
            { "XXL","22","26","52"},
            { "XXL","24","28","54"}
    };

    static String[][] MALE = {
            //"Int","US","UK","EU"
            { "XS","30","30","40"},
            { "XS","32","32","42"},
            { "S","34","34","44"},
            { "S","36","36","46"},
            { "M","38","38","48"},
            { "M","40","40","50"},
            { "L","42","42","52"},
            { "L","44","44","54"},
            { "XL","46","46","56"},
            { "XL","48","48","58"},
            { "XXL","50","50","60"},
            { "XXL","52","52","62"}
    };

    public static Map<String,String> getClothingSizes(boolean female, Unit unit, String value){
        String[][] chart;
        if(female){
            chart = FEMALE;
        }else{
            chart = MALE;
        }
        Map<String,String> sizes = new HashMap<>();
        for(int i = chart.length-1; i>=0; i--){
           if(chart[i][unit.val].equalsIgnoreCase(value)){
               for(int j = 0; j< chart[i].length; j++){
                    sizes.put(Unit.valueOfLabel(j).toString(), chart[i][j]);
               }
               break;
           }
        }
        return sizes;
    }

    enum Unit {
        INT(0),
        US(1),
        UK(2),
        EU(3);

        final int val;

        Unit(int i) {
            val = i;
        }

        public static Unit valueOfLabel(int label) {
            for (Unit e : values()) {
                if (e.val == label) {
                    return e;
                }
            }
            return null;
        }
    }
}