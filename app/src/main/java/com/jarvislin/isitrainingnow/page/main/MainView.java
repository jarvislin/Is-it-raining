package com.jarvislin.isitrainingnow.page.main;

import com.jarvislin.isitrainingnow.BaseView;
import com.jarvislin.isitrainingnow.model.RainingData;

import java.util.List;

/**
 * Created by JarvisLin on 2017/1/27.
 */
public interface MainView extends BaseView{

    void updateRainingData(List<RainingData> filteredList);
}
