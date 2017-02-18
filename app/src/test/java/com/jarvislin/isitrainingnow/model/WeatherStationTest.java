package com.jarvislin.isitrainingnow.model;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by JarvisLin on 2017/2/18.
 */
public class WeatherStationTest {
    private WeatherStation weatherStation = new WeatherStation();

    @Test
    public void whenPublishTimeIsWrongFormat() {
        String unexpectedText = "1948/7/1";
        weatherStation.setPublishTime(unexpectedText);
        assertEquals(weatherStation.getDateText(), unexpectedText);
        assertNull(weatherStation.getDate());
    }

    @Test
    public void whenPublishTimeIsCorrectFormat() {
        String expectedDateFormat = "2007-12-25 14:28:30";
        String expectedResult = "2007.12.25 14:28";
        weatherStation.setPublishTime(expectedDateFormat);
        assertEquals(weatherStation.getDateText(), expectedResult);
        assertNotNull(weatherStation.getDate());
    }

    @Test
    public void whenPositionHasNotSet() {
        assertEquals(weatherStation.getPosition(), new LatLng(0, 0));
    }

}