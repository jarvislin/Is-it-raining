package com.jarvislin.isitrainingnow.page.main;

import android.content.Context;

import com.jarvislin.isitrainingnow.Presenter;
import com.jarvislin.isitrainingnow.model.WeatherStation;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by JarvisLin on 2017/1/27.
 */
@EBean
public class MainPresenter extends Presenter {
    private final MainView view;

    public MainPresenter(Context context) {
        super((MainView) context);
        this.view = (MainView) context;
    }

    public void fetchWeatherStation() {
        view.showLoading();
        networkService.getOpenDataApi().rainingData()
                .doOnTerminate(view::hideLoading)
                .subscribeOn(Schedulers.newThread())
                .subscribe(this::filter, throwable -> errorHandling(throwable));
    }

    private void filter(ArrayList<WeatherStation> weatherStationList) {
        Observable.from(weatherStationList)
                .filter(rainingData -> rainingData.isNewest() && rainingData.getRainfall10min() > 0 )
                .toList().singleOrDefault(new ArrayList<>())
                .subscribe(view::updateStation);
    }
}
