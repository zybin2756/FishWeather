package com.example.fishweather.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.fishweather.R;
import com.example.fishweather.db.City;
import com.example.fishweather.db.UserCity;
import com.example.fishweather.db.dbManage;
import com.example.fishweather.Constants;
import com.example.fishweather.util.HttpUtil;
import com.example.fishweather.util.ParseUtil;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Administrator on 2017/3/19.
 */

public class SplashActivity extends Activity {

    private RelativeLayout progressBar;
    private int count = 0;
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
                        mHandler.sendEmptyMessage(Constants.LOAD_CITY_INFO_ERROR);
                    } else {
                        loadWeatherInfo();
                    }
                }

            });
        } else {
            loadWeatherInfo();
        }
    }

    public void loadWeatherInfo(){
        List<UserCity> userCityList = dbManage.loadUserCity();
        String code;
        for(int i = 0; i < userCityList.size(); i++){
            UserCity city = userCityList.get(i);
            long now = System.currentTimeMillis();
            long last = ( city.getUpdateTime() + 1800*1000);
            if(now > last) {
                count++;
                code = city.getCity_code();
                HttpUtil.loadWeatherInfo(code, new HttpUtil.HttpCallBack() {
                    @Override
                    public void onError(String msg) {
                        count--;
                    }

                    @Override
                    public void onFinish(String data) {
                        ParseUtil.parseWeather(data);
                        count--;
                    }
                });
            }
        }
        while(count > 0);
        mHandler.sendEmptyMessageDelayed(Constants.LOAD_CITY_INFO_FINISH, 1000);
    }
}
