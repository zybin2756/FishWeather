package com.example.fishweather;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import org.litepal.LitePalApplication;

/**
 * Created by Administrator on 2017/3/19.
 */

public class FishApplication extends Application {

    public static  RefWatcher getRefWatcher(){
        FishApplication application = (FishApplication) context;
        return application.refWatcher;
    }

    public static Context getContext(){
        return context;
    }

    private static Context context;
    private RefWatcher refWatcher = null;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePalApplication.initialize(context);
        refWatcher = LeakCanary.install(this);
    }
}
