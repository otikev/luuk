package com.elmenture.core.charts;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.elmenture.core.charts.ClothingChart.Unit.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ClothingChartTest {

    @Test
    public void testFemaleUS() throws Exception {
        Map<String, String> mapped = ClothingChart.getClothingSizes(true,US, "14");
        assertEquals("L", mapped.get(INT.toString()));
        assertEquals("14", mapped.get(US.toString()));
        assertEquals("18", mapped.get(UK.toString()));
        assertEquals("44", mapped.get(EU.toString()));
    }

    @Test
    public void testFemaleUK_SmallestValue() throws Exception {
        Map<String, String> mapped = ClothingChart.getClothingSizes(true,UK, "4");
        assertEquals("XS", mapped.get(INT.toString()));
        assertEquals("0", mapped.get(US.toString()));
        assertEquals("4", mapped.get(UK.toString()));
        assertEquals("30", mapped.get(EU.toString()));
    }

    @Test
    public void testFemaleINT_PicksLargestOption() throws Exception {
        Map<String, String> mapped = ClothingChart.getClothingSizes(true,INT, "M");
        assertEquals("M", mapped.get(INT.toString()));
        assertEquals("12", mapped.get(US.toString()));
        assertEquals("16", mapped.get(UK.toString()));
        assertEquals("42", mapped.get(EU.toString()));
    }
}