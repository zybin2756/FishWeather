package com.example.fishweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.fishweather.FishApplication;
import com.example.fishweather.R;
import com.example.fishweather.db.City;
import com.example.fishweather.util.Constants;
import com.example.fishweather.util.HttpUtil;
import com.example.fishweather.util.ParseUtil;
import com.squareup.leakcanary.RefWatcher;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Administrator on 2017/3/19.
 */

public class SplashActivity extends Activity {

    private RelativeLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBar = (RelativeLayout) findViewById(R.id.progressBar);
        City city = DataSupport.findFirst(City.class);
        if(city == null){
            String path = "http://files.heweather.com/china-city-list.json";
            HttpUtil.sendOkHttpRequest(path,new HttpUtil.HttpCallBack(){
                @Override
                public void onError(String msg) {
                    mHandler.sendEmptyMessage(Constants.LOAD_CITY_INFO_ERROR);
                }

                @Override
                public void onFinish(String data) {
                    if(!ParseUtil.parseCityFromJson(data)){
                        mHandler.sendEmptyMessage(Constants.LOAD_CITY_INFO_ERROR);
                    }
                    else{
                        mHandler.sendEmptyMessage(Constants.LOAD_CITY_INFO_FINISH);
                    }
                }

            });
        }else{
            mHandler.sendEmptyMessageDelayed(Constants.LOAD_CITY_INFO_FINISH,1000);
        }

    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constants.LOAD_CITY_INFO_ERROR:
                    Toast.makeText(SplashActivity.this,"加载数据失败!",Toast.LENGTH_SHORT);
                    break;
                case Constants.LOAD_CITY_INFO_FINISH:
                    progressBar.setVisibility(View.GONE);
                    Intent intent = new Intent(SplashActivity.this,WeatherInfosActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };
}
