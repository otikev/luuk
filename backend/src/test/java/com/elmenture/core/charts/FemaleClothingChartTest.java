package com.elmenture.core.charts;

import com.elmenture.core.engine.charts.FemaleClothingChart;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.elmenture.core.engine.charts.MeasurementUnit.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FemaleClothingChartTest {

    @Test
    public void testUS() throws Exception {
        Map<String, String> mapped = FemaleClothingChart.getClothingSizesAndBodyMeasurements(US, "14");
        assertEquals("L", mapped.get(INT.toString()));
        assertEquals("14", mapped.get(US.toString()));
        assertEquals("18", mapped.get(UK.toString()));
        assertEquals("46", mapped.get(EU.toString()));
    }

    @Test
    public void testFemaleUK_SmallestValue() throws Exception {
        Map<String, String> mapped = FemaleClothingChart.getClothingSizesAndBodyMeasurements(UK, "4");
        assertEquals("XS", mapped.get(INT.toString()));
        assertEquals("0", mapped.get(US.toString()));
        assertEquals("4", mapped.get(UK.toString()));
        assertEquals("32", mapped.get(EU.toString()));
    }

    @Test
    public void testFemaleINT_PicksLargestOption() throws Exception {
        Map<String, String> mapped = FemaleClothingChart.getClothingSizesAndBodyMeasurements(INT, "M");
        assertEquals("M", mapped.get(INT.toString()));
        assertEquals("10", mapped.get(US.toString()));
        assertEquals("14", mapped.get(UK.toString()));
        assertEquals("42", mapped.get(EU.toString()));
    }
}