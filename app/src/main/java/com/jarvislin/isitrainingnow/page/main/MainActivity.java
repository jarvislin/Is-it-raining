package com.jarvislin.isitrainingnow.page.main;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.github.promeg.pinyinhelper.Pinyin;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.jarvislin.isitrainingnow.BaseActivity;
import com.jarvislin.isitrainingnow.R;
import com.jarvislin.isitrainingnow.model.WeatherStation;
import com.jarvislin.isitrainingnow.ui.dialog.DetailDialog;
import com.jarvislin.isitrainingnow.ui.dialog.SettingsDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.util.List;
import java.util.Locale;

import timber.log.Timber;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity<MainPresenter> implements MainView, OnMapReadyCallback, ClusterManager.OnClusterItemInfoWindowClickListener<WeatherStation>, SettingsDialog.OnSubmitListener {

    private GoogleMap map;
    private static final LatLng TAIWAN = new LatLng(24.122276688433903, 120.64848627895118);
    private ClusterManager<WeatherStation> clusterManager;
    private WeatherStation clickedStation;
    private SettingsDialog settingsDialog;

    @Override
    protected MainPresenter createPresenter() {
        return MainPresenter_.getInstance_(this);
    }

    @AfterViews
    protected void init() {
        // init toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationIcon(R.mipmap.ic_action_arrow_back);
        toolbar.setNavigationOnClickListener(v -> finish());

        //sync map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //others
        settingsDialog = new SettingsDialog(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        initMap();
        initCluster();
        refresh();
    }

    private void initMap() {
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(false);
        CameraPosition position = new CameraPosition(TAIWAN, 7, 0, 0);
        map.moveCamera(CameraUpdateFactory.newCameraPosition(position));
    }

    private void initCluster() {
        clusterManager = new ClusterManager<>(this, map);
        clusterManager.setRenderer(new ColorfulMarkerRenderer(this, map, clusterManager));
        clusterManager.getMarkerCollection().setOnInfoWindowAdapter(new StationInfoWindowAdapter());
        clusterManager.setOnClusterItemInfoWindowClickListener(this);
        clusterManager.setOnClusterItemClickListener(item -> {
            clickedStation = item;
            animateCamera();
            return false;
        });
        clusterManager.setOnClusterClickListener(cluster -> {
            CameraPosition cameraPosition = new CameraPosition(cluster.getPosition(), map.getCameraPosition().zoom + 1, 0, 0);
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            return true;
        });

        map.setOnCameraIdleListener(clusterManager);
        map.setOnMarkerClickListener(clusterManager);
        map.setInfoWindowAdapter(clusterManager.getMarkerManager());
        map.setOnInfoWindowClickListener(clusterManager);
    }

    @UiThread(delay = 50)
    protected void animateCamera() {
        CameraPosition cameraPosition = new CameraPosition(getLatLngWithOffset(clickedStation.getPosition(), map.getProjection()), map.getCameraPosition().zoom, 0, 0);
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 500, null);
    }

    @UiThread
    @Override
    public void updateStation(List<WeatherStation> filteredList) {
        clusterManager.clearItems();
        map.clear();

        Timber.i("size = " + filteredList.size());

        for (WeatherStation weatherStation : filteredList) {
            clusterManager.addItem(weatherStation);
        }
        clusterManager.cluster();
    }

    @Override
    public void showStationAmount(int size) {
        showToast(String.format(getString(R.string.there_are_n_weather_stations), String.valueOf(size)));
    }

    @Override
    public void showRainingAmount(int size) {
        showToast(String.format(getResources().getQuantityString(R.plurals.place_are_raining, size), size));
    }

    @Click(R.id.refresh)
    protected void refresh() {
        presenter.fetchWeatherStation(settingsDialog.getTimeParam(), settingsDialog.isShowAll());
    }

    @Click(R.id.settings)
    protected void settings() {
        settingsDialog.show();
    }

    @Override
    public void onClusterItemInfoWindowClick(WeatherStation weatherStation) {
        new DetailDialog(this, weatherStation).show();
    }

    private LatLng getLatLngWithOffset(LatLng latLng, Projection projection) {
        final int offset = getResources().getDimensionPixelSize(R.dimen.marker_offset);
        final Point point = projection.toScreenLocation(latLng);
        point.offset(0, offset);
        return projection.fromScreenLocation(point);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onSubmit() {
        refresh();
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

            boolean isChinese = Locale.getDefault().getLanguage().equals("zh");

            if (isChinese) {
                name.setText(clickedStation.getName());
            } else {
                name.setText(Pinyin.toPinyin(clickedStation.getName(), " "));
            }

            rainingInfo.setText(String.format(getString(R.string.raining_info), String.valueOf(clickedStation.getRainfall10min()), String.valueOf(clickedStation.getRainfall1hr())));
            publishTime.setText(clickedStation.getDateText());

            return view;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }

    class ColorfulMarkerRenderer extends DefaultClusterRenderer<WeatherStation> {

        ColorfulMarkerRenderer(Context context, GoogleMap map, ClusterManager<WeatherStation> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(WeatherStation item, MarkerOptions markerOptions) {
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_place));
            super.onBeforeClusterItemRendered(item, markerOptions);
        }
    }
}
