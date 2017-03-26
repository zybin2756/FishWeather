package com.example.fishweather.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fishweather.R;
import com.example.fishweather.util.DailyForecastModel;
import com.example.fishweather.util.HttpUtil;
import com.example.fishweather.util.ParseUtil;
import com.example.fishweather.util.SuggestionModel;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather,container,false);
        Bundle args = getArguments();
        this.cityCode = args.getString("cityCode");

        HttpUtil.loadWeatherInfo(this.cityCode);
        now_weather = (RelativeLayout) view.findViewById(R.id.now_weather);
        now_weather.getBackground().setAlpha(180);
        now_tmp = (TextView) now_weather.findViewById(R.id.now_temp);
        now_city_and_code = (TextView) now_weather.findViewById(R.id.now_city_and_code);
        now_dir = (TextView) now_weather.findViewById(R.id.now_dir);
        now_spd = (TextView) now_weather.findViewById(R.id.now_spd);
        now_hum = (TextView) now_weather.findViewById(R.id.now_hum);
        now_qlty = (TextView) now_weather.findViewById(R.id.now_qlty);
        SharedPreferences sp = getActivity().getSharedPreferences(cityCode,Context.MODE_PRIVATE);
        now_tmp.setText(sp.getString("tmp","未获取"));
        now_city_and_code.setText(sp.getString("city","未获取"));
        now_dir.setText(sp.getString("dir","未获取"));
        now_spd.setText(sp.getString("spd","未获取"));
        now_hum.setText(sp.getString("hum","未获取"));
        now_qlty.setText(sp.getString("qai","未获取"));
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
//        DailyForecastModel day0 = (DailyForecastModel) ParseUtil.parseModel(sp.getString("day0",null));
//        DailyForecastModel day1 = (DailyForecastModel) ParseUtil.parseModel(sp.getString("day1",null));
//        DailyForecastModel day2 = (DailyForecastModel) ParseUtil.parseModel(sp.getString("day2",null));

        return view;
    }

}
