package com.jarvislin.isitrainingnow.page.main;

import com.jarvislin.isitrainingnow.base.BaseView;
import com.jarvislin.isitrainingnow.model.WeatherStation;

import java.util.List;

/**
 * Created by JarvisLin on 2017/1/27.
 */
interface MainView extends BaseView {

    void updateStation(List<WeatherStation> filteredList);

    void showStationAmount(int size);

    void showRainingAmount(int size);

    boolean isShowAll();
}
