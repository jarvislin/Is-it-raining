package com.jarvislin.isitrainingnow.page.main;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.jarvislin.isitrainingnow.BaseActivity;
import com.jarvislin.isitrainingnow.R;
import com.jarvislin.isitrainingnow.model.RainingData;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import java.util.List;

import timber.log.Timber;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity<MainPresenter> implements MainView, OnMapReadyCallback {

    private GoogleMap map;
    private static final LatLng TAIWAN = new LatLng(24.7724596,121.2679728);
    private ClusterManager<RainingData> clusterManager;

    @AfterViews
    protected void init() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected MainPresenter createPresenter() {
        return MainPresenter_.getInstance_(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        CameraPosition position = new CameraPosition(TAIWAN, 9, 0, 0);
        map.moveCamera(CameraUpdateFactory.newCameraPosition(position));

        clusterManager = new ClusterManager<>(this, map);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        map.setOnCameraIdleListener(clusterManager);
        map.setOnMarkerClickListener(clusterManager);
        map.setInfoWindowAdapter(clusterManager.getMarkerManager());

        presenter.fetchRainingData();
    }

    @UiThread
    @Override
    public void updateRainingData(List<RainingData> filteredList) {
        Timber.e("size="+filteredList.size());
        for(RainingData rainingData : filteredList) {
//            map.addMarker(new MarkerOptions().position(rainingData.getLatLng()).title(rainingData.getName()));
            clusterManager.addItem(rainingData);
        }
        clusterManager.cluster();

        showToast(String.format("共監測到%s處降雨", filteredList.size()));
    }
}
