package com.example.fishweather.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.fishweather.Constants;
import com.example.fishweather.R;
import com.example.fishweather.adapter.DrawerCityListAdapter;
import com.example.fishweather.adapter.WeaterFragmentAdapter;
import com.example.fishweather.db.City;
import com.example.fishweather.db.UserCity;
import com.example.fishweather.db.dbManage;
import com.example.fishweather.itemTouch.DividerItemDecoration;
import com.example.fishweather.util.Utils;
import com.example.fishweather.util.mPageTransformer;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Administrator on 2017/3/20 0020.
 */

public class WeatherInfosActivity extends BaseActivity {

    private boolean isLocating = false;
//    private List<WeatherFragment> fragmentList = new ArrayList<>();
    private WeaterFragmentAdapter weaterFragmentAdapter;
    private ViewPager weatherInfoView;
    private DrawerLayout drawer;
    private ActionBar actionBar;
    private RecyclerView drawer_citylist;
    private DrawerCityListAdapter drawerCityListAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar mToolbar;
    private LocationClient locationClient = null;
    private myLocationListener locationListener;
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

        weatherInfoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this,drawer,mToolbar,R.string.txt_clik_tips,R.string.app_name){
            @Override
            public void onDrawerOpened(View drawerView) {
//                actionBar.setHomeAsUpIndicator(R.mipmap.leftarrow);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
//                actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu);
                invalidateOptionsMenu();
            }
        };
        drawer.setDrawerListener(mDrawerToggle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        drawer_citylist = (RecyclerView) findViewById(R.id.drawer_citylist);
        drawer_citylist.setLayoutManager(layoutManager);
        drawer_citylist.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        drawerCityListAdapter = new DrawerCityListAdapter(weatherInfoView,drawer);
        drawer_citylist.setAdapter(drawerCityListAdapter);

        refreshData();

    }


    @Override
    protected void onStop() {
        if(locationClient != null){
            locationClient.unRegisterLocationListener(locationListener);
            locationClient.stop();
            locationClient = null;
        }
        super.onStop();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void refreshData(){
        List<UserCity> userCityList = dbManage.loadUserCity();
        weaterFragmentAdapter.setData(userCityList);
        drawerCityListAdapter.setUserCityList(userCityList);
    }
    private void initToolbar(){
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu);
            actionBar.setHomeButtonEnabled(true);
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
                }else{
                    drawer.openDrawer(GravityCompat.START);
                }
                break;
            case R.id.action_changeCity:
                Intent intent = new Intent(this,CityManageActivity.class);
                startActivityForResult(intent, Constants.MANAGE_CITY);
                break;
            case R.id.action_location:
                if(!isLocating) {
                    isLocating = true;
                    String[] perssions = Utils.getDeniedPermisson();
                    if (perssions != null) {
                        ActivityCompat.requestPermissions(this, perssions, Constants.REQUEST_PERMISSION);
                    } else {
                        requestLocation();
                    }
                }
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
                    weatherInfoView.setCurrentItem(weaterFragmentAdapter.getCount()-1);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case Constants.REQUEST_PERMISSION:
                if(grantResults.length > 0){
                    for(int result : grantResults){
                        if(result != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this,"拒绝权限将无法使用自动定位！", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        requestLocation();
                    }
                }else{
                    Toast.makeText(this,"发生未知错误", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    class myLocationListener implements BDLocationListener{

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            String cityName = bdLocation.getCity().split("市")[0];

            if(dbManage.isExistUserCity(cityName)){
                UserCity userCity = (UserCity) DataSupport.where("city_name = ?",cityName).find(UserCity.class).get(0);
                Message msg = new Message();
                msg.what = Constants.LOCATION_CITY;
                msg.arg1 = userCity.getCity_index()-1;
                mHandler.sendMessage(msg);
            }else{
                City city = dbManage.searchCity(cityName).get(0);
                List<UserCity> userCityList = dbManage.loadUserCity();
                for(UserCity userCity : userCityList){
                    userCity.setCity_index(userCity.getCity_index()+1);
                    userCity.save();
                }
                dbManage.saveUserCity(city,1);
                mHandler.sendEmptyMessage(Constants.LOCATION_CITY_REFRESH);
            }
            isLocating = false;
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    public void requestLocation(){

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
        option.setIsNeedAddress(true);
        locationListener = new myLocationListener();
        locationClient = new LocationClient(getApplicationContext());
        locationClient.setLocOption(option);
        locationClient.registerLocationListener(locationListener);
        locationClient.start();
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constants.LOCATION_CITY:
                    weatherInfoView.setCurrentItem(msg.arg1);
                    Toast.makeText(WeatherInfosActivity.this,"您所在城市已添加过!",Toast.LENGTH_SHORT).show();
                    break;
                case Constants.LOCATION_CITY_REFRESH:
                    refreshData();
                    weatherInfoView.setCurrentItem(0);
                    break;
            }
        }
    };
}
