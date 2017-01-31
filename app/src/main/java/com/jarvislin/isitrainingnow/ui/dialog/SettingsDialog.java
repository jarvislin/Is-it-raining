package com.jarvislin.isitrainingnow.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.jarvislin.isitrainingnow.R;

/**
 * Created by JarvisLin on 2017/1/28.
 */

public class SettingsDialog extends Dialog {
    private int timeParam = 60;
    private boolean showAll = false;

    private OnSubmitListener onSubmitListener;
    private RadioButton btnShowAll;
    private RadioButton btnShowRaining;
    private EditText etMins;

    public SettingsDialog(Context context) {
        super(context);
        onSubmitListener = (OnSubmitListener) context;
        init();
    }

    private void init() {
        setOnCancelListener(dialog -> resetParam());

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_settings, null);
        etMins = (EditText) view.findViewById(R.id.etMins);
        btnShowAll = (RadioButton) view.findViewById(R.id.btnShowAll);
        btnShowRaining = (RadioButton) view.findViewById(R.id.btnShowRaining);
        Button btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(v -> {
            showAll = btnShowAll.isChecked();
            timeParam = Integer.valueOf(etMins.getText().toString());
            dismiss();
            onSubmitListener.onSubmit();
        });

        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v -> {
            dismiss();
            resetParam();
        });

        setContentView(view);
    }

    private void resetParam() {
        if (showAll) {
            btnShowAll.setChecked(true);
        } else {
            btnShowRaining.setChecked(true);
        }
        etMins.setText(String.valueOf(timeParam));
    }

    @Override
    public void show() {
        if (!isShowing()) {
            etMins.setSelection(etMins.length());
            super.show();
        }
    }

    @Override
    public void dismiss() {
        if (isShowing()) {
            super.dismiss();
        }
    }

    public int getTimeParam() {
        return timeParam;
    }

    public boolean isShowAll() {
        return showAll;
    }

    public interface OnSubmitListener {
        void onSubmit();
    }
}
