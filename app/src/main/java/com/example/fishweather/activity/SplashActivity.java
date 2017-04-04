package com.example.fishweather.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.accessibility.AccessibilityManagerCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.fishweather.R;
import com.example.fishweather.db.City;
import com.example.fishweather.db.UserCity;
import com.example.fishweather.db.dbManage;
import com.example.fishweather.Constants;
import com.example.fishweather.service.WeatherService;
import com.example.fishweather.util.HttpUtil;
import com.example.fishweather.util.ParseUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/19.
 */

public class SplashActivity extends BaseActivity {

    private RelativeLayout progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = (RelativeLayout) findViewById(R.id.progressBar);
        copyOrCreateDataBase();
    }



    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.LOAD_CITY_INFO_ERROR:
                    Toast.makeText(SplashActivity.this, "加载数据失败!", Toast.LENGTH_SHORT);
                    finish();
                    break;
                case Constants.LOAD_CITY_INFO_FINISH:
                    progressBar.setVisibility(View.GONE);
                    WeatherInfosActivity.actionStart(SplashActivity.this);
                    finish();
            }
        }
    };

    private void copyOrCreateDataBase(){
        progressBar.setVisibility(View.VISIBLE);
        if(!dbManage.existDataBase()){
            if(dbManage.copyDataBase()){
                mHandler.sendEmptyMessageDelayed(Constants.LOAD_CITY_INFO_FINISH, 3000);
            }
        }else{
            loadDbFromUrl();
        }
    }

    public void loadDbFromUrl(){
        City city = DataSupport.findFirst(City.class);
        if (city == null) {
            String path = "http://files.heweather.com/china-city-list.json";
            HttpUtil.sendOkHttpRequest(path, new HttpUtil.HttpCallBack() {
                @Override
                public void onError(String msg) {
                    mHandler.sendEmptyMessage(Constants.LOAD_CITY_INFO_ERROR);
                }

                @Override
                public void onFinish(String data) {
                    if (!ParseUtil.parseCityFromJson(data)) {
                        mHandler.sendEmptyMessage(Constants.LOAD_CITY_INFO_FINISH);
                    }
                }

            });
        }
        mHandler.sendEmptyMessage(Constants.LOAD_CITY_INFO_FINISH);
    }
}
