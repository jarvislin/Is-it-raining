package com.jarvislin.isitrainingnow.model;

import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.clustering.ClusterItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by JarvisLin on 2017/1/27.
 */

@Getter
@Setter
@Builder
public class WeatherStation implements ClusterItem {

    @SerializedName("SiteId")
    private String id;
    @SerializedName("SiteName")
    private String name;
    @SerializedName("County")
    private String county;
    @SerializedName("Township")
    private String township;
    @SerializedName("Unit")
    private String unit;
    @SerializedName("TWD67Lon")
    private double lng;
    @SerializedName("TWD67Lat")
    private double lat;
    @SerializedName("Rainfall10min")
    private float rainfall10min;
    @SerializedName("Rainfall1hr")
    private float rainfall1hr;
    @SerializedName("Rainfall3hr")
    private float rainfall3hr;
    @SerializedName("Rainfall6hr")
    private float rainfall6hr;
    @SerializedName("Rainfall12hr")
    private float rainfall12hr;
    @SerializedName("Rainfall24hr")
    private float rainfall24hr;
    @SerializedName("Now")
    private float rainfallToday;
    @SerializedName("PublishTime")
    private String publishTime;

    public boolean isValidUpdateTime(int n) {
        Date date = getDate();
        if (date != null) {
            long time = System.currentTimeMillis();
            return time - date.getTime() <= n * 60 * 1000; // in n mins
        } else {
            return true; // return true if parse failed
        }
    }

    @Nullable
    public Date getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            return dateFormat.parse(publishTime);
        } catch (ParseException e) {
            return null;
        }
    }

    public String getDateText() {
        Date date = getDate();
        if (date == null) {
            return publishTime;
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault());
            return dateFormat.format(date);
        }
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(lat, lng);
    }
}
