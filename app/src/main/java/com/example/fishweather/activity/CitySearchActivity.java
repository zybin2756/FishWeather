package com.example.fishweather.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.fishweather.R;

/**
 * Created by Administrator on 2017/3/22 0022.
 */

public class CitySearchActivity extends AppCompatActivity {

    private Toolbar toolbar;

    public static void actionStart(Context context){
        Intent intent = new Intent(context,CitySearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_search);

        toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
    }
}
