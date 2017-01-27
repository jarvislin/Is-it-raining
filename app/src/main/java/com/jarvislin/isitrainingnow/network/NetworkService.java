package com.jarvislin.isitrainingnow.network;

import android.content.Context;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by JarvisLin on 2017/1/7.
 */

public class NetworkService {
    private static final String RAINING_OPEN_DATA_API_URL = "http://opendata.epa.gov.tw/ws/Data/RainTenMin/";

    private Retrofit retrofit;

    public NetworkService(Context context) {
        init();
    }

    private void init() {
        retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(new ToStringConverterFactory())
                .baseUrl(RAINING_OPEN_DATA_API_URL)
                .client(new OkHttpClient.Builder().build())
                .build();
    }

    public OpenDataApi getOpenDataApi() {
        return retrofit.create(OpenDataApi.class);
    }
}
