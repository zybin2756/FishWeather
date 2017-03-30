package com.example.fishweather.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.fishweather.Constants;
import com.example.fishweather.R;
import com.example.fishweather.adapter.CitySearchAdapter;
import com.example.fishweather.db.City;
import com.example.fishweather.db.dbManage;
import com.example.fishweather.itemTouch.DividerItemDecoration;

import java.util.List;

/**
 * Created by Administrator on 2017/3/22 0022.
 */

public class CitySearchActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText edt_search;
    private RecyclerView search_recyclerView;
    private CitySearchAdapter adapter;


    public static void actionStart(Context context){
        Intent intent = new Intent(context,CitySearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_search);

        initToolbar();
        initEdtSearch();
        initRecyclerView();
    }

    public void initRecyclerView(){
        search_recyclerView = (RecyclerView) findViewById(R.id.search_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        search_recyclerView.setLayoutManager(layoutManager);
        search_recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        adapter = new CitySearchAdapter(new CitySearchAdapter.closeListener(){
            @Override
            public void onFinish(String code) {
                Intent intent = new Intent();
                intent.putExtra("city_code",code);
                setResult(Constants.SEARCH_CITY_ADD,intent);
                finish();
            }
        });
        search_recyclerView.setAdapter(adapter);
    }



    public void initEdtSearch(){
        edt_search = (EditText) toolbar.findViewById(R.id.edt_search);
        Drawable search = getResources().getDrawable(R.mipmap.search);
        search.setBounds(15,0,50,35);
        edt_search.setCompoundDrawables(search,null,null,null);
        edt_search.addTextChangedListener(watcher);
    }


    public void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            List<City> cityList =  dbManage.searchCity(s.toString());
            adapter.setCityList(cityList);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }

        return  true;
    }
}
