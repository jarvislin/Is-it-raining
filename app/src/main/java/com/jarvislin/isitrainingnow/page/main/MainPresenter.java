package com.jarvislin.isitrainingnow.page.main;

import android.content.Context;

import com.jarvislin.isitrainingnow.Presenter;
import com.jarvislin.isitrainingnow.model.RainingData;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
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

    public void fetchRainingData() {
        networkService.getOpenDataApi().rainingData()
                .subscribeOn(Schedulers.newThread())
                .subscribe(this::filter, this::errorHandling);
    }

    private void filter(ArrayList<RainingData> rainingDataList) {
        Observable.from(rainingDataList)
                .filter(rainingData -> rainingData.isNewest() )
                .toList().toBlocking()
                .subscribe(view::updateRainingData);
    }
}
