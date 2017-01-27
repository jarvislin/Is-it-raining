package com.jarvislin.isitrainingnow.network;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import com.jarvislin.isitrainingnow.BuildConfig;

/**
 * Created by JarvisLin on 2017/1/7.
 */

public class NetworkService {
    private static final String RAINING_OPEN_DATA_API_URL = "http://opendata.epa.gov.tw/ws/Data/RainTenMin/";

    private Retrofit retrofit;

    public NetworkService() {
        init();
    }

    private void init() {
        retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(new ToStringConverterFactory())
                .baseUrl(RAINING_OPEN_DATA_API_URL)
                .client(new OkHttpClient.Builder()
                        .readTimeout(15, TimeUnit.SECONDS)
                        .connectTimeout(15, TimeUnit.SECONDS)
                        .addInterceptor(new HttpLoggingInterceptor().setLevel(BuildConfig.DEBUG
                                ? HttpLoggingInterceptor.Level.BODY
                                : HttpLoggingInterceptor.Level.NONE))
                        .build())
                .build();
    }

    public OpenDataApi getOpenDataApi() {
        return retrofit.create(OpenDataApi.class);
    }
}
