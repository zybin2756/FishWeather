package com.example.fishweather.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.fishweather.Constants;
import com.example.fishweather.R;
import com.example.fishweather.adapter.DrawerCityListAdapter;
import com.example.fishweather.adapter.WeaterFragmentAdapter;
import com.example.fishweather.db.UserCity;
import com.example.fishweather.db.dbManage;
import com.example.fishweather.itemTouch.DividerItemDecoration;
import com.example.fishweather.util.mPageTransformer;

import java.util.List;

/**
 * Created by Administrator on 2017/3/20 0020.
 */

public class WeatherInfosActivity extends AppCompatActivity {

//    private List<WeatherFragment> fragmentList = new ArrayList<>();
    private WeaterFragmentAdapter weaterFragmentAdapter;
    private ViewPager weatherInfoView;
    private DrawerLayout drawer;
    private ActionBar actionBar;
    private RecyclerView drawer_citylist;
    private DrawerCityListAdapter drawerCityListAdapter;

    public static void  actionStart(Context context){
        Intent intent = new Intent(context,WeatherInfosActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        initToolbar();

        weatherInfoView = (ViewPager) findViewById(R.id.weatherInfoView);
        weaterFragmentAdapter = new WeaterFragmentAdapter(getSupportFragmentManager());
        weatherInfoView.setAdapter(weaterFragmentAdapter);
        weatherInfoView.setPageTransformer(true,new mPageTransformer());


        drawer = (DrawerLayout) findViewById(R.id.drawer);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        drawer_citylist = (RecyclerView) findViewById(R.id.drawer_citylist);
        drawer_citylist.setLayoutManager(layoutManager);
        drawer_citylist.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        drawerCityListAdapter = new DrawerCityListAdapter(weatherInfoView,drawer);
        drawer_citylist.setAdapter(drawerCityListAdapter);

        refreshData();
    }

    private void refreshData(){
        List<UserCity> userCityList = dbManage.loadUserCity();

        weaterFragmentAdapter.setData(userCityList);
        drawerCityListAdapter.setUserCityList(userCityList);


    }
    private void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
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
                if(drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawers();
                    actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu);
                }else{
                    drawer.openDrawer(GravityCompat.START);
                    actionBar.setHomeAsUpIndicator(R.mipmap.leftarrow);
                }
                break;
            case R.id.action_changeCity:
                Intent intent = new Intent(this,CityManageActivity.class);
                startActivityForResult(intent, Constants.MANAGE_CITY);
                break;
            case R.id.action_setting:
                break;
            case R.id.action_exit:
                finish();
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.MANAGE_CITY: {
                if (Constants.MANAGE_CITY_REFRESH == resultCode) {
                    refreshData();
                    weaterFragmentAdapter.notifyDataSetChanged();
                }
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawers();
        }
        else{
            super.onBackPressed();
        }
    }
}
