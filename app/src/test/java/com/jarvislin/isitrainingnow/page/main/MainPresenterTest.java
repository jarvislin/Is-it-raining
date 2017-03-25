package com.jarvislin.isitrainingnow.page.main;

import android.support.annotation.NonNull;

import com.jarvislin.isitrainingnow.ImmediateSchedulersRule;
import com.jarvislin.isitrainingnow.R;
import com.jarvislin.isitrainingnow.model.WeatherStation;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import rx.Observable;

import static org.mockito.Mockito.verify;

/**
 * Created by JarvisLin on 2017/2/18.
 */
public class MainPresenterTest {
    @Rule
    public final ImmediateSchedulersRule schedulers = new ImmediateSchedulersRule();

    private MainView view;
    private MainPresenter presenter;
    private MainRepository repository;

    @Before
    public void setup() {
        view = Mockito.mock(MainView.class);
        repository = Mockito.mock(MainRepository.class);
        presenter = new MainPresenter(view, repository);
    }

    @Test
    public void whenFetchAllStationsSuccess() {
        ArrayList<WeatherStation> list = getWeatherStations();

        Mockito.when(repository.fetchWeatherStation()).thenReturn(Observable.just(list));
        Mockito.when(view.isShowAll()).thenReturn(true);
        presenter.fetchWeatherStation(5);

        verify(view).showLoading();
        verify(repository).fetchWeatherStation();
        verify(view).hideLoading();
        verify(view).updateStation(list);
        verify(view).showStationAmount(list.size());
    }

    @Test
    public void whenFetchRainingStationsSuccess() {
        ArrayList<WeatherStation> list = new ArrayList<>();

        Mockito.when(repository.fetchWeatherStation()).thenReturn(Observable.just(list));
        presenter.fetchWeatherStation(5);

        verify(view).showLoading();
        verify(repository).fetchWeatherStation();
        verify(view).hideLoading();
        verify(view).updateStation(list);
        verify(view).showRainingAmount(list.size());
    }

    @Test
    public void whenFetchRainingStationsTimeout() {
        Mockito.when(repository.fetchWeatherStation()).thenReturn(Observable.error(new SocketTimeoutException()));
        presenter.fetchWeatherStation(5);

        verify(view).showLoading();
        verify(repository).fetchWeatherStation();
        verify(view).hideLoading();
        verify(view).showToast(R.string.timeout_hint);

    }

    @NonNull
    private ArrayList<WeatherStation> getWeatherStations() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        WeatherStation station = WeatherStation.builder()
                .publishTime(dateFormat.format(date))
                .rainfall10min(10.5f)
                .build();

        return new ArrayList<WeatherStation>() {{
            add(station);
        }};
    }

}