package com.alexredchets.sunshineweather.mvp.currentWeather;

import android.util.Log;

import com.alexredchets.sunshineweather.WeatherApi;
import com.alexredchets.sunshineweather.WeatherMapper;
import com.alexredchets.sunshineweather.WeatherModel.Weather;
import com.alexredchets.sunshineweather.mvp.main.WeatherInterface;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class CurrentWeatherPresenter implements WeatherInterface.WeatherPresenterInterface{

    private static final String TAG = CurrentWeatherPresenter.class.getSimpleName();
    private Retrofit mRetrofit;
    private WeatherInterface.CurrentWeatherFragmentInterface view;

    @Inject
    public CurrentWeatherPresenter(Retrofit mRetrofit,
                                   WeatherInterface.CurrentWeatherFragmentInterface view) {
        this.mRetrofit = mRetrofit;
        this.view = view;
    }

    @Inject
    protected WeatherMapper mWeatherMapper;

    @Override
    public void fetchData() {

        Log.i(TAG, "fetchData started");

        mRetrofit.create(WeatherApi.class).getCurrentWeather("52.051503",
                "113.471191",
                "metric",
                "d73975775ce9c90c9b05799d119ef5e9")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                            Log.e(TAG, "Successfully got data");

                            Weather mWeather = mWeatherMapper.mapCurrentWeather(response);
                            view.onComplete(mWeather);
                        },
                        throwable -> {
                            Log.e("Error", throwable.getMessage());

                            view.onError(throwable.getMessage());
                        });

    }

    @Override
    public void fetchDataDB() {

    }
}