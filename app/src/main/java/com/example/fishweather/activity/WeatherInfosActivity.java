package com.example.fishweather.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.fishweather.R;

/**
 * Created by Administrator on 2017/3/20 0020.
 */

public class WeatherInfosActivity extends AppCompatActivity {
    public static void  actionStart(Context context){
        Intent intent = new Intent(context,WeatherInfosActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Toast.makeText(this, "我准备用来显示城市列表的", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_changeCity:
                CityManageActivity.actionStart(this);
                break;
            case R.id.action_setting:
                break;
            case R.id.action_exit:
                finish();
                break;
        }
        return true;
    }
}
