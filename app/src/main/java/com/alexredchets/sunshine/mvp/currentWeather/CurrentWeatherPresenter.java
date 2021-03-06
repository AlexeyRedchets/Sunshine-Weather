package com.alexredchets.sunshine.mvp.currentWeather;

import com.alexredchets.sunshine.WeatherApi;
import com.alexredchets.sunshine.WeatherMapper;
import com.alexredchets.sunshine.WeatherModel.Weather;
import com.alexredchets.sunshine.mvp.main.WeatherInterface;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import timber.log.Timber;

public class CurrentWeatherPresenter implements WeatherInterface.WeatherPresenterInterface{

    private Retrofit mRetrofit;
    private WeatherInterface.CurrentWeatherFragmentInterface view;

    @Inject
    CurrentWeatherPresenter(Retrofit mRetrofit,
                            WeatherInterface.CurrentWeatherFragmentInterface view) {
        this.mRetrofit = mRetrofit;
        this.view = view;
    }

    @Inject
    WeatherMapper mWeatherMapper;

    @Override
    public void fetchData(String lat, String lon) {

        mRetrofit.create(WeatherApi.class).getCurrentWeather(lat,
                lon,
                "metric",
                "d73975775ce9c90c9b05799d119ef5e9")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                            Timber.i("Successfully got data");

                            Weather mWeather = mWeatherMapper.mapCurrentWeather(response);
                            view.onComplete(mWeather);
                        },
                        throwable -> {
                            Timber.e(throwable);

                            view.onError(throwable.getMessage());
                        });

    }

    @Override
    public void fetchDataDB() {

    }
}
