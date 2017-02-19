package com.jarvislin.isitrainingnow.model;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;
import org.mockito.Mock;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by JarvisLin on 2017/2/18.
 */
public class WeatherStationTest {
    @Mock
    private WeatherStation weatherStation;

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

    @Test
    public void whenPublishTimeIsValid() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        weatherStation.setPublishTime(dateFormat.format(date));
        assertTrue(weatherStation.isValidUpdateTime(5));
    }

    @Test
    public void whenPublishTimeIsInvalid() {
        Date date = new Date(System.currentTimeMillis() - 6 * 60 * 1000); // 6 mins
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        weatherStation.setPublishTime(dateFormat.format(date));
        assertFalse(weatherStation.isValidUpdateTime(5));
    }

}