package com.jarvislin.isitrainingnow.network;

import com.jarvislin.isitrainingnow.model.WeatherStation;

import java.util.ArrayList;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by JarvisLin on 2017/1/7.
 */

public interface OpenDataApi {
    @GET("?$orderby=PublishTime%20desc&$skip=0&$top=1000&format=json&token=ijhBb85vJEKD0t+rPLJ0tw")
    Observable<ArrayList<WeatherStation>> weatherStation();
}
