package com.jarvislin.isitrainingnow.page.main;

import android.content.Context;

import com.jarvislin.isitrainingnow.Presenter;
import com.jarvislin.isitrainingnow.model.WeatherStation;
import com.jarvislin.isitrainingnow.network.NetworkService;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by JarvisLin on 2017/1/27.
 */

public class MainPresenter extends Presenter {
    private final MainView view;

    public MainPresenter(MainView view, NetworkService service) {
        super(view);
        this.view = view;
        networkService = service;
    }

    public void fetchWeatherStation(int mins, boolean showAll) {
        view.showLoading();
        networkService.fetchWeatherStation()
                .doOnTerminate(view::hideLoading)
                .subscribeOn(Schedulers.newThread())
                .subscribe(weatherStations -> filter(weatherStations, mins, showAll), throwable -> errorHandling(throwable));
    }

    private void filter(ArrayList<WeatherStation> weatherStationList, int mins, boolean showAll) {
        Observable.from(weatherStationList)
                .filter(rainingData -> {
                    if (showAll) {
                        return rainingData.isValidUpdateTime(mins);
                    } else {
                        return rainingData.isValidUpdateTime(mins) && rainingData.getRainfall10min() > 0;
                    }
                })
                .toList().singleOrDefault(new ArrayList<>())
                .subscribe(result -> {
                    view.updateStation(result);
                    if (showAll) {
                        view.showStationAmount(result.size());
                    } else {
                        view.showRainingAmount(result.size());
                    }
                });
    }
}
