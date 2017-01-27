package com.jarvislin.isitrainingnow;

import android.content.Context;

import com.jarvislin.isitrainingnow.network.NetworkService;
import com.jarvislin.isitrainingnow.repository.BaseRepository;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import retrofit2.adapter.rxjava.HttpException;
import timber.log.Timber;

public class Presenter {

    private final BaseView view;
    private Reference<BaseView> reference;
    protected BaseRepository repository;
    protected NetworkService networkService;

    public Presenter(BaseView view) {
        this.view = view;
        networkService = new NetworkService();
        repository = new BaseRepository(getContext());
        reference = new WeakReference<>(view);
    }

    protected void errorHandling(Throwable throwable) {

        if (BuildConfig.DEBUG) {
            Timber.d("errorHandling: " + throwable.getLocalizedMessage());
        }

        try {
            if (throwable instanceof IOException) {
                view.showToast("連線發生錯誤，請檢查網路狀況");
            } else if (throwable instanceof HttpException) {
                HttpException httpException = (HttpException) throwable;
                int code = httpException.code();
                if (code >= 400 && code < 600) {
                    view.showToast("開放資料發生異常");
                } else {
                    view.showToast("發生未知錯誤");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void detachView() {
        if (reference != null) {
            reference.clear();
            reference = null;
        }
    }

    protected Context getContext() {
        return view.getContext();
    }
}
