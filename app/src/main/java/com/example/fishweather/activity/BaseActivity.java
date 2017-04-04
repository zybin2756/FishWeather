package com.example.fishweather.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.fishweather.db.UserCity;
import com.example.fishweather.service.WeatherService;

import java.util.List;

/**
 * Created by Administrator on 2017/3/31 0031.
 */

public class BaseActivity extends AppCompatActivity {

    protected WeatherService weatherService;
    protected boolean isBound;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isBound = false;
    }


    @Override
    protected void onPause() {
        super.onPause();
        unbindWeatherService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindWeatherService();
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            WeatherService.ServiceBinder binder = (WeatherService.ServiceBinder) service;
            weatherService = (WeatherService) binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public void bindWeatherService(){
        if(!isBound) {
            isBound = true;
            Intent intent = new Intent(this, WeatherService.class);
            bindService(intent, connection, Context.BIND_AUTO_CREATE);
        }
    }

    public void unbindWeatherService(){
        if(isBound) {
            isBound = false;
            unbindService(connection);
        }
    }
}
