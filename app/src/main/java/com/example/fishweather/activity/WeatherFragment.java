package com.example.fishweather.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.fishweather.FishApplication;
import com.example.fishweather.R;
import com.example.fishweather.adapter.WeatherDailyViewAdapter;
import com.example.fishweather.adapter.WeatherSuggestionViewAdapter;
import com.example.fishweather.Constants;
import com.example.fishweather.model.DailyForecastModel;
import com.example.fishweather.util.HttpUtil;
import com.example.fishweather.util.ParseUtil;
import com.example.fishweather.model.SuggestionModel;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/23.
 */

public class WeatherFragment extends Fragment{

    public static int count = 0;
    public static boolean dataChange = false;

    public static WeatherFragment newInstance(String cityCode) {
        
        Bundle args = new Bundle();
        args.putString("cityCode",cityCode);
        WeatherFragment fragment = new WeatherFragment();
        fragment.setCityCode(cityCode);
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
    private ImageView now_icon;

    private WeatherSuggestionViewAdapter suggestionViewAdapter;
    private RecyclerView suggestion_view;
    private WeatherDailyViewAdapter dailyViewAdapteradapter;
    private RecyclerView daily_view;
    private SwipeRefreshLayout weatherinfo_swip;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather,container,false);
        initView(view);
        preLoadWeatherInfo();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(dataChange) {
            preLoadWeatherInfo();
            dataChange = false;
        }
    }

    public void initView(View view) {
        now_weather = (RelativeLayout) view.findViewById(R.id.now_weather);
        weatherinfo_swip = (SwipeRefreshLayout) view.findViewById(R.id.weatherinfo_swip);
        now_weather.getBackground().setAlpha(220);
        now_tmp = (TextView) now_weather.findViewById(R.id.now_temp);
        now_city_and_code = (TextView) now_weather.findViewById(R.id.now_city_and_code);
        now_dir = (TextView) now_weather.findViewById(R.id.now_dir);
        now_spd = (TextView) now_weather.findViewById(R.id.now_spd);
        now_hum = (TextView) now_weather.findViewById(R.id.now_hum);
        now_qlty = (TextView) now_weather.findViewById(R.id.now_qlty);
        now_icon = (ImageView) now_weather.findViewById(R.id.now_icon);
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
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        daily_view = (RecyclerView) view.findViewById(R.id.daily_view);
        daily_view.setLayoutManager(layoutManager);
        daily_view.setHasFixedSize(true);
        daily_view.setNestedScrollingEnabled(false);
        dailyViewAdapteradapter = new WeatherDailyViewAdapter();
        daily_view.setAdapter(dailyViewAdapteradapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        gridLayoutManager.setSmoothScrollbarEnabled(true);
        gridLayoutManager.setAutoMeasureEnabled(true);
        suggestion_view = (RecyclerView) view.findViewById(R.id.suggestion_view);
        suggestion_view.setLayoutManager(gridLayoutManager);
        suggestion_view.setHasFixedSize(true);
        suggestion_view.setNestedScrollingEnabled(false);
        suggestionViewAdapter = new WeatherSuggestionViewAdapter();
        suggestion_view.setAdapter(suggestionViewAdapter);

    }

    public void preLoadWeatherInfo(){
        SharedPreferences sp = getActivity().getSharedPreferences(cityCode,Context.MODE_PRIVATE);
        if(!sp.getBoolean("isUpdate",false)){
            HttpUtil.loadWeatherInfo(cityCode, new HttpUtil.HttpCallBack() {
                @Override
                public void onError(String msg) {

                }

                @Override
                public void onFinish(String data) {
                    ParseUtil.parseWeather(data);
                    preLoadWeatherInfo();
                }
            });
            return;
        }
        handler.sendEmptyMessage(Constants.WEATHER_REFRESH);
    }

    private void loadWeatherInfoFromSp(){
        if(cityCode == null) return;
        Log.i("zyb","第"+(++count)+"次进入");
        SharedPreferences sp = getActivity().getSharedPreferences(cityCode,Context.MODE_PRIVATE);
        now_tmp.setText(sp.getString("tmp","未获取")+"°");
        now_city_and_code.setText(sp.getString("city","未获取") + "|"+sp.getString("txt","未获取"));
        now_dir.setText(sp.getString("dir","未获取"));
        now_spd.setText(sp.getString("spd","未获取")+"m/s");
        now_hum.setText(sp.getString("hum","未获取")+"%");
        now_qlty.setText(sp.getString("aqi","未获取"));

        String code = "p"+sp.getString("code","100");
        int resId = getResources().getIdentifier(code,"mipmap",getContext().getPackageName());
        now_icon.setBackgroundResource(resId);

        DailyForecastModel day0 = (DailyForecastModel) ParseUtil.parseModel(sp.getString("day0",null));
        DailyForecastModel day1 = (DailyForecastModel) ParseUtil.parseModel(sp.getString("day1",null));
        DailyForecastModel day2 = (DailyForecastModel) ParseUtil.parseModel(sp.getString("day2",null));

        List<DailyForecastModel> list = new ArrayList<>();
        if(day0 != null) {
            day0.setDay("今天");
            list.add(day0);
        }
        if(day1 != null) {
            day1.setDay("明天");
            list.add(day1);
        }
        if(day2 != null) {
            day2.setDay("后天");
            list.add(day2);
        }

        dailyViewAdapteradapter.setList(list);

        List<SuggestionModel> slist = new ArrayList<>();
        SuggestionModel drsg = (SuggestionModel) ParseUtil.parseModel(sp.getString("drsg",null));
        SuggestionModel comf = (SuggestionModel) ParseUtil.parseModel(sp.getString("comf",null));
        SuggestionModel uv = (SuggestionModel) ParseUtil.parseModel(sp.getString("uv",null));
        SuggestionModel flu = (SuggestionModel) ParseUtil.parseModel(sp.getString("flu",null));
        SuggestionModel trav = (SuggestionModel) ParseUtil.parseModel(sp.getString("trav",null));
        SuggestionModel sport = (SuggestionModel) ParseUtil.parseModel(sp.getString("sport",null));
        SuggestionModel cw = (SuggestionModel) ParseUtil.parseModel(sp.getString("cw",null));
        SuggestionModel air = (SuggestionModel) ParseUtil.parseModel(sp.getString("air",null));
        if(drsg != null) {
            slist.add(drsg);
        }
        if(cw != null) {
            slist.add(cw);
        }
        if(sport != null) {
            slist.add(sport);
        }
        if(trav != null) {
            slist.add(trav);
        }
        if(flu != null) {
            slist.add(flu);
        }
        if(uv != null) {
            slist.add(uv);
        }
        if(comf != null) {
            slist.add(comf);
        }
        if(air != null) {
            slist.add(air);
        }
        suggestionViewAdapter.setList(slist);
    };

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.WEATHER_REFRESH:
                    loadWeatherInfoFromSp();
                    if(weatherinfo_swip.isRefreshing()) {
                        weatherinfo_swip.setRefreshing(false);
                    }
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        FishApplication.fixInputMethodManagerLeak(getContext());
        RefWatcher watcher = FishApplication.getRefWatcher();
        watcher.watch(this);
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
        dataChange = true;
    }
}
