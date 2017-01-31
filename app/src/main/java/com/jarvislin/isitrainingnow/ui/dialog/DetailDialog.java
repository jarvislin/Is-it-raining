package com.jarvislin.isitrainingnow.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.promeg.pinyinhelper.Pinyin;
import com.jarvislin.isitrainingnow.R;
import com.jarvislin.isitrainingnow.model.WeatherStation;

import java.util.Locale;

/**
 * Created by JarvisLin on 2017/1/28.
 */

public class DetailDialog extends Dialog {

    public DetailDialog(Context context, WeatherStation weatherStation) {
        super(context);
        init(weatherStation);
    }

    private void init(WeatherStation weatherStation) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_detail, null);

        TextView tvId = (TextView) view.findViewById(R.id.tvId);
        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        TextView tvUpdateTime = (TextView) view.findViewById(R.id.tvUpdateTime);
        TextView tv10min = (TextView) view.findViewById(R.id.tv10min);
        TextView tv1hr = (TextView) view.findViewById(R.id.tv1hr);
        TextView tv3hr = (TextView) view.findViewById(R.id.tv3hr);
        TextView tv6hr = (TextView) view.findViewById(R.id.tv6hr);
        TextView tv12hr = (TextView) view.findViewById(R.id.tv12hr);
        TextView tv24hr = (TextView) view.findViewById(R.id.tv24hr);
        TextView tvToday = (TextView) view.findViewById(R.id.tvToday);
        Button btnDismiss = (Button) view.findViewById(R.id.btnDismiss);
        btnDismiss.setOnClickListener(v -> dismiss());

        boolean isChinese = Locale.getDefault().getLanguage().equals("zh");

        if (isChinese) {
            tvName.setText(String.format(getContext().getString(R.string.name), weatherStation.getName()));
        } else {
            tvName.setText(String.format(getContext().getString(R.string.name), Pinyin.toPinyin(weatherStation.getName(), " ")));
        }

        tvId.setText(String.format(getContext().getString(R.string.id), weatherStation.getId()));
        tvUpdateTime.setText(String.format(getContext().getString(R.string.published_at), weatherStation.getDateText()));
        tv10min.setText(String.format(getContext().getString(R.string.rain_10mins), weatherStation.getRainfall10min()));
        tv1hr.setText(String.format(getContext().getString(R.string.rain_1hr), weatherStation.getRainfall1hr()));
        tv3hr.setText(String.format(getContext().getString(R.string.rain_3hr), weatherStation.getRainfall3hr()));
        tv6hr.setText(String.format(getContext().getString(R.string.rain_6hr), weatherStation.getRainfall6hr()));
        tv12hr.setText(String.format(getContext().getString(R.string.rain_12hr), weatherStation.getRainfall12hr()));
        tv24hr.setText(String.format(getContext().getString(R.string.rain_24hr), weatherStation.getRainfall24hr()));
        tvToday.setText(String.format(getContext().getString(R.string.rain_today), weatherStation.getRainfallToday()));

        setContentView(view);
    }

    @Override
    public void show() {
        if (!isShowing()) {
            super.show();
        }
    }

    @Override
    public void dismiss() {
        if (isShowing()) {
            super.dismiss();
        }
    }
}
