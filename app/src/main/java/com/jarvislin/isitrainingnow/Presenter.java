package com.jarvislin.isitrainingnow;

import android.content.Context;

import com.jarvislin.isitrainingnow.network.NetworkService;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.net.SocketTimeoutException;

import retrofit2.adapter.rxjava.HttpException;
import timber.log.Timber;

public class Presenter {

    private final BaseView view;
    private Reference<BaseView> reference;
    protected NetworkService networkService;

    public Presenter(BaseView view) {
        this.view = view;
        reference = new WeakReference<>(view);
    }

    protected void errorHandling(Throwable throwable) {

        if (BuildConfig.DEBUG) {
            Timber.d("errorHandling: " + throwable.getLocalizedMessage());
        }

        try {
            if (throwable instanceof IOException) {
                if (throwable instanceof SocketTimeoutException) {
                    view.showToast(R.string.timeout_hint);
                } else {
                    view.showToast(R.string.network_hint);
                }
            } else if (throwable instanceof HttpException) {
                HttpException httpException = (HttpException) throwable;
                int code = httpException.code();
                if (code >= 400 && code < 600) {
                    view.showToast(R.string.opendata_api_broken_hint);
                } else {
                    view.showToast(R.string.unknown_problem_hint);
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
