package com.jarvislin.isitrainingnow.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.clustering.ClusterItem;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Getter;

/**
 * Created by JarvisLin on 2017/1/27.
 */

@Getter
public class RainingData implements ClusterItem {

    /**
     * SiteName : 阿里山
     * TWD67Lon : 120.6936
     * TWD67Lat : 23.4708
     * Rainfall10min : 0
     * PublishTime : 2017-01-27 14:50:00
     */

    @SerializedName("SiteName")
    private String name;
    @SerializedName("TWD67Lon")
    private double lng;
    @SerializedName("TWD67Lat")
    private double lat;
    @SerializedName("Rainfall10min")
    private float rainfall10min;
    @SerializedName("Rainfall1hr")
    private float rainfall1hr;
    @SerializedName("PublishTime")
    private String publishTime;

    public boolean isNewest() {

        long time = System.currentTimeMillis();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat();
            dateFormat.applyPattern("yyyy-MM-dd HH:mm:ss");
            Date date = dateFormat.parse(publishTime);
            return time - date.getTime() <= 60 * 60 * 1000; // 30 mins
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(lat, lng);
    }
}
