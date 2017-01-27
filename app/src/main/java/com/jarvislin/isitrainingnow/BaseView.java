package com.jarvislin.isitrainingnow;

import android.content.Context;
import android.support.annotation.StringRes;

public interface BaseView {
    Context getContext();

    void showToast(@StringRes int stringRes);

    void showToast(String title);

    void showLoading();

    void hideLoading();
}
