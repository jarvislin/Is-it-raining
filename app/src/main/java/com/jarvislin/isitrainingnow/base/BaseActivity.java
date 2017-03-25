package com.jarvislin.isitrainingnow.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.widget.Toast;

import com.jarvislin.isitrainingnow.R;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

public abstract class BaseActivity<T extends Presenter> extends RxAppCompatActivity implements BaseView {
    protected T presenter;
    protected ProgressDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = createPresenter();
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
    public void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void showLoading() {
        runOnUiThread(() -> loadingDialog = ProgressDialog.show(this, getString(R.string.loading), getString(R.string.please_wait), true));
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
