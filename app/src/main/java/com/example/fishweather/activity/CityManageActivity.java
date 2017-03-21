package com.example.fishweather.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.fishweather.R;
import com.example.fishweather.adapter.CityManageAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/21 0021.
 */

public class CityManageActivity extends AppCompatActivity implements View.OnClickListener {
    public static void  actionStart(Context context){
        Intent intent = new Intent(context,CityManageActivity.class);
        context.startActivity(intent);
    }

    private ActionBar actionBar;
    private Button action_edit;
    private Button action_cancel;
    private LinearLayout ll_add_city;
    private RecyclerView city_recyclerView;
    private CityManageAdapter cityManageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.city_toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        List<String> cityList = new ArrayList<String>();
        for(int i = 0 ; i< 10; i++){
            cityList.add("广州"+i);
        }
        cityManageAdapter = new CityManageAdapter(cityList);
        city_recyclerView = (RecyclerView) findViewById(R.id.city_recyclerView);
        city_recyclerView.setAdapter(cityManageAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        city_recyclerView.setLayoutManager(layoutManager);

        ll_add_city = (LinearLayout) findViewById(R.id.ll_add_city);
        action_edit = (Button) toolbar.findViewById(R.id.action_edit);
        action_cancel = (Button) toolbar.findViewById(R.id.action_cancel);
        action_edit.setOnClickListener(this);
        action_cancel.setOnClickListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }

        return  true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_edit:
                break;
            case R.id.action_cancel:
                break;
        }
    }
}
