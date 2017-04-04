package com.example.fishweather.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.example.fishweather.FishApplication;
import com.example.fishweather.R;
import com.example.fishweather.activity.SplashActivity;
import com.example.fishweather.activity.WeatherInfosActivity;
import com.example.fishweather.db.UserCity;
import com.example.fishweather.db.dbManage;
import com.example.fishweather.util.HttpUtil;
import com.example.fishweather.util.ParseUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/31 0031.
 */

public class WeatherService extends Service{

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ServiceBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        Intent i = new Intent(this, SplashActivity.class);
//        PendingIntent pi = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);
//        RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.notification_layout);
//        Notification notification = new NotificationCompat.Builder(this.getApplicationContext())
//                .setContent(remoteViews)
//                .setWhen(System.currentTimeMillis())
//                .setContentIntent(pi)
//                .build();
//        notification.defaults = Notification.DEFAULT_SOUND;
//        startForeground(101,notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateAllWeather();

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int hour = 60 * 60 * 1000;
        long triggerAtTime = (SystemClock.elapsedRealtime() + hour);
        Intent i2 = new Intent(this,WeatherService.class);
        PendingIntent pi2 = PendingIntent.getService(this,0,i2,0);
        manager.cancel(pi2);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi2);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class ServiceBinder extends Binder{
        public Service getService(){
            return WeatherService.this;
        }
    }

    public void updateAllWeather(){
        List<UserCity> userCityList = dbManage.loadUserCity();

        for(int i = 0; i < userCityList.size(); i++){
            updateWeather(userCityList.get(i));
        }
    }

    public void updateWeather(UserCity city){
        String code = city.getCity_code();
        HttpUtil.loadWeatherInfo(code, new HttpUtil.HttpCallBack() {
            @Override
            public void onError(String msg) {

            }

            @Override
            public void onFinish(String data) {
                ParseUtil.parseWeather(data);
            }
        });
    }
}
