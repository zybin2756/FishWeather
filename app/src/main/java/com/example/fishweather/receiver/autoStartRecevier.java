package com.example.fishweather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.fishweather.Constants;
import com.example.fishweather.activity.WeatherInfosActivity;

/**
 * Created by Administrator on 2017/3/31 0031.
 */

public class AutoStartRecevier extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sp = context.getSharedPreferences(Constants.CONFIG_FILE_NAME,Context.MODE_PRIVATE);
        if(sp.getBoolean("autoStart",false)){
            Intent i = new Intent(context, WeatherInfosActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}
