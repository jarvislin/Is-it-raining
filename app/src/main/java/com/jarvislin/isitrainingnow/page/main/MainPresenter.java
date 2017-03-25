package com.jarvislin.isitrainingnow.page.main;

import com.jarvislin.isitrainingnow.base.Presenter;
import com.jarvislin.isitrainingnow.model.WeatherStation;

import java.util.ArrayList;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by JarvisLin on 2017/1/27.
 */

public class MainPresenter extends Presenter {
    private final MainView view;
    private final MainRepository repository;

    public MainPresenter(MainView view, MainRepository repository) {
        super(view);
        this.view = view;
        this.repository = repository;
    }

    public void fetchWeatherStation(int mins) {
        view.showLoading();
        repository.fetchWeatherStation()
                .doOnTerminate(view::hideLoading)
                .subscribeOn(Schedulers.newThread())
                .subscribe(weatherStations -> filter(weatherStations, mins), throwable -> errorHandling(throwable));
    }

    private void filter(ArrayList<WeatherStation> weatherStationList, int mins) {
        boolean showAll = view.isShowAll();
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
