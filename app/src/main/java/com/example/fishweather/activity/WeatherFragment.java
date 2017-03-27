package com.example.fishweather.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fishweather.FishApplication;
import com.example.fishweather.R;
import com.example.fishweather.adapter.WeatherDailyViewAdapter;
import com.example.fishweather.db.UserCity;
import com.example.fishweather.db.dbManage;
import com.example.fishweather.itemTouch.DividerItemDecoration;
import com.example.fishweather.util.Constants;
import com.example.fishweather.util.DailyForecastModel;
import com.example.fishweather.util.HttpUtil;
import com.example.fishweather.util.ParseUtil;
import com.example.fishweather.util.SuggestionModel;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/3/23.
 */

public class WeatherFragment extends Fragment{

    public static WeatherFragment newInstance(String cityCode) {
        
        Bundle args = new Bundle();
        args.putString("cityCode",cityCode);
        WeatherFragment fragment = new WeatherFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private String cityCode;
    private RelativeLayout now_weather;
    private TextView now_tmp;
    private TextView now_city_and_code;
    private TextView now_dir;
    private TextView now_spd;
    private TextView now_hum;
    private TextView now_qlty;

    private WeatherDailyViewAdapter adapter;
    private RecyclerView daily_view;
    private NestedScrollView scrollView;
    private SwipeRefreshLayout weatherinfo_swip;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather,container,false);
        Bundle args = getArguments();
        this.cityCode = args.getString("cityCode");

//        HttpUtil.loadWeatherInfo(this.cityCode);
        initView(view);

        loadWeatherInfoFromSp();
//        sp.getString("o3",null);
//        sp.getString("aqi",null);
//        sp.getString("city",null);
//        sp.getString("update",null);
//        SuggestionModel drsg = (SuggestionModel) ParseUtil.parseModel(sp.getString("drsg",null));
//        SuggestionModel comf = (SuggestionModel) ParseUtil.parseModel(sp.getString("comf",null));
//        SuggestionModel uv = (SuggestionModel) ParseUtil.parseModel(sp.getString("uv",null));
//        SuggestionModel flu = (SuggestionModel) ParseUtil.parseModel(sp.getString("flu",null));
//        SuggestionModel trav = (SuggestionModel) ParseUtil.parseModel(sp.getString("trav",null));
//        SuggestionModel sport = (SuggestionModel) ParseUtil.parseModel(sp.getString("sport",null));
//        SuggestionModel cw = (SuggestionModel) ParseUtil.parseModel(sp.getString("cw",null));
//        SuggestionModel air = (SuggestionModel) ParseUtil.parseModel(sp.getString("air",null));


        return view;
    }

    public void initView(View view) {
        now_weather = (RelativeLayout) view.findViewById(R.id.now_weather);
        weatherinfo_swip = (SwipeRefreshLayout) view.findViewById(R.id.weatherinfo_swip);
        now_weather.getBackground().setAlpha(180);
        now_tmp = (TextView) now_weather.findViewById(R.id.now_temp);
        now_city_and_code = (TextView) now_weather.findViewById(R.id.now_city_and_code);
        now_dir = (TextView) now_weather.findViewById(R.id.now_dir);
        now_spd = (TextView) now_weather.findViewById(R.id.now_spd);
        now_hum = (TextView) now_weather.findViewById(R.id.now_hum);
        now_qlty = (TextView) now_weather.findViewById(R.id.now_qlty);

        weatherinfo_swip.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        weatherinfo_swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                HttpUtil.loadWeatherInfo(cityCode, new HttpUtil.HttpCallBack() {
                    @Override
                    public void onError(String msg) {

                    }

                    @Override
                    public void onFinish(String data) {
                        ParseUtil.parseWeather(data);
                        handler.sendEmptyMessage(Constants.WEATHER_REFRESH);
                    }
                });
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        daily_view = (RecyclerView) view.findViewById(R.id.daily_view);
        daily_view.setLayoutManager(layoutManager);
        adapter = new WeatherDailyViewAdapter();
        daily_view.setAdapter(adapter);
    }

    public boolean isDayOrNight(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String hour = sdf.format(new Date());
        int tmp = Integer.parseInt(hour);
        if(tmp > 6 && tmp < 18){
            return true;
        }
        return false;
    }

    public void loadWeatherInfoFromSp(){
        SharedPreferences sp = getActivity().getSharedPreferences(cityCode,Context.MODE_PRIVATE);
        now_tmp.setText(sp.getString("tmp","未获取")+"°");
        now_city_and_code.setText(sp.getString("city","未获取") + "|"+sp.getString("txt","未获取"));
        now_dir.setText(sp.getString("dir","未获取"));
        now_spd.setText(sp.getString("spd","未获取")+"m/s");
        now_hum.setText(sp.getString("hum","未获取")+"%");
        now_qlty.setText(sp.getString("aqi","未获取"));

        DailyForecastModel day0 = (DailyForecastModel) ParseUtil.parseModel(sp.getString("day0",null));
        day0.setDay("今天");
        DailyForecastModel day1 = (DailyForecastModel) ParseUtil.parseModel(sp.getString("day1",null));
        day1.setDay("明天");
        DailyForecastModel day2 = (DailyForecastModel) ParseUtil.parseModel(sp.getString("day2",null));
        day2.setDay("后天");

        List<DailyForecastModel> list = new ArrayList<>();
        list.add(day0);
        list.add(day1);
        list.add(day2);

        adapter.setList(list);
    };

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.WEATHER_REFRESH:
                    loadWeatherInfoFromSp();
                    weatherinfo_swip.setRefreshing(false);
                    break;
            }
        }
    };
}
