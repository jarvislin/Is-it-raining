package com.jarvislin.isitrainingnow.page.main;

import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;
import com.jarvislin.isitrainingnow.BaseActivity;
import com.jarvislin.isitrainingnow.R;
import com.jarvislin.isitrainingnow.model.WeatherStation;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.util.List;

import timber.log.Timber;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity<MainPresenter> implements MainView, OnMapReadyCallback, ClusterManager.OnClusterItemInfoWindowClickListener<WeatherStation> {

    private GoogleMap map;
    private static final LatLng TAIWAN = new LatLng(24.122276688433903, 120.64848627895118);
    private ClusterManager<WeatherStation> clusterManager;
    private WeatherStation clickedStation;

    @Override
    protected MainPresenter createPresenter() {
        return MainPresenter_.getInstance_(this);
    }

    @AfterViews
    protected void init() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        CameraPosition position = new CameraPosition(TAIWAN, 7, 0, 0);
        map.moveCamera(CameraUpdateFactory.newCameraPosition(position));

        clusterManager = new ClusterManager<>(this, map);
        clusterManager.getMarkerCollection().setOnInfoWindowAdapter(new StationInfoWindowAdapter());
        clusterManager.setOnClusterItemInfoWindowClickListener(this);
        clusterManager.setOnClusterItemClickListener(item -> {
            clickedStation = item;
            return false;
        });

        map.setOnCameraIdleListener(clusterManager);
        map.setOnMarkerClickListener(clusterManager);
        map.setInfoWindowAdapter(clusterManager.getMarkerManager());
        map.setOnInfoWindowClickListener(clusterManager);

        presenter.fetchWeatherStation();
    }

    @UiThread
    @Override
    public void updateStation(List<WeatherStation> filteredList) {
        Timber.e("size=" + filteredList.size());
        for (WeatherStation weatherStation : filteredList) {
            clusterManager.addItem(weatherStation);
        }
        clusterManager.cluster();

        showToast(String.format("共監測到%s處降雨", filteredList.size()));
    }

    @Override
    public void onClusterItemInfoWindowClick(WeatherStation weatherStation) {

    }

    class StationInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        private final View view;

        StationInfoWindowAdapter() {
            view = getLayoutInflater().inflate(R.layout.info_window, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {

            TextView name = ((TextView) view.findViewById(R.id.stationName));
            TextView rainingInfo = ((TextView) view.findViewById(R.id.rainingInfo));
            TextView publishTime = ((TextView) view.findViewById(R.id.publishTime));

            name.setText(clickedStation.getName());
            rainingInfo.setText(String.format(getString(R.string.raining_info), String.valueOf(clickedStation.getRainfall10min()), String.valueOf(clickedStation.getRainfall1hr())));
            publishTime.setText(clickedStation.getPublishTime());

            return view;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }
}
