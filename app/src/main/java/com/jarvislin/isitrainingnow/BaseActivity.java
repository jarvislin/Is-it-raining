package com.jarvislin.isitrainingnow;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

public abstract class BaseActivity<T extends Presenter> extends RxAppCompatActivity implements BaseView {
    protected T presenter;
    protected ProgressDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = createPresenter();

        // hide keyboard
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    protected abstract T createPresenter();

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showToast(@StringRes int stringRes) {
        showToast(getString(stringRes));
    }

    @Override
    public void showToast(String title) {
        runOnUiThread(() -> {
            Toast.makeText(this, title, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void showLoading() {
        runOnUiThread(() -> loadingDialog = ProgressDialog.show(this, "讀取中", "請稍候...", true));
    }

    @Override
    public void hideLoading() {
        runOnUiThread(() -> {
            if (loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }
        });
    }

}
