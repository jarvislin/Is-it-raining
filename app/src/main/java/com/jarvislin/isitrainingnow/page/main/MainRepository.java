package com.jarvislin.isitrainingnow.page.main;

import com.jarvislin.isitrainingnow.model.WeatherStation;
import com.jarvislin.isitrainingnow.network.NetworkService;
import com.jarvislin.isitrainingnow.base.BaseRepository;

import java.util.ArrayList;

import rx.Observable;

/**
 * Created by JarvisLin on 2017/3/25.
 */
public class MainRepository extends BaseRepository{

    public MainRepository(NetworkService networkService) {
        super(networkService);
    }

    public Observable<ArrayList<WeatherStation>> fetchWeatherStation() {
        return networkService.getOpenDataApi().weatherStation();
    }
}
