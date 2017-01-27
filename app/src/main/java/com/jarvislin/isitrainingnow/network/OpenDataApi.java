package com.jarvislin.isitrainingnow.network;

import com.jarvislin.isitrainingnow.model.RainingData;

import java.util.ArrayList;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by JarvisLin on 2017/1/7.
 */

public interface OpenDataApi {
    @GET("?$select=SiteName,TWD67Lon,TWD67Lat,Rainfall10min,PublishTime&$orderby=PublishTime%20DESC&$skip=0&$top=1000&format=json")
    Observable<ArrayList<RainingData>> rainingData();
}
