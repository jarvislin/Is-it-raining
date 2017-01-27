package com.jarvislin.isitrainingnow;

import timber.log.Timber;

/**
 * Created by JarvisLin on 2017/1/7.
 */

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
