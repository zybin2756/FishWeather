package com.example.fishweather.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import com.example.fishweather.activity.WeatherFragment;
import com.example.fishweather.db.UserCity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class WeaterFragmentAdapter extends FragmentPagerAdapter {


    List<WeatherFragment> fragmentList = new ArrayList<>();
    boolean isChange[] = new boolean[50];
    FragmentManager fm ;
    FragmentTransaction ft;
    public WeaterFragmentAdapter(FragmentManager fm){
        super(fm);
        this.fm = fm;
        for(int i =0; i < isChange.length ; i++){
            isChange[i] = false;
        }
    }

    public void setData(List<UserCity> userCityList){

        if( userCityList == null ) return;

        int userCityListLen = userCityList.size();
        int fragmentListLen = fragmentList.size();
        WeatherFragment fragment = null;
        String code = null;
        for(int i = 0; i < userCityListLen; i++){
            code = userCityList.get(i).getCity_code();
            if(i < fragmentListLen){
                fragment = fragmentList.get(i);
            }else{
                fragment = WeatherFragment.newInstance(code);
                fragmentList.add(fragment);
            }
            if(fragment != null) {
                if (!fragment.getCityCode().equals(code)) {
                    fragment.setCityCode(code);
//                    fragment.loadWeatherInfoFromSp();
                    isChange[i] = true;
                }
            }
        }

        fragmentListLen = fragmentList.size();
        if(userCityListLen != fragmentListLen) {
            ft = fm.beginTransaction();
            while (userCityListLen != fragmentListLen) {
                fragment = fragmentList.get(--fragmentListLen);
                ft.remove(fragment);
                fragmentList.remove(fragmentListLen);
                isChange[fragmentListLen] = true;
            }
            ft.commitAllowingStateLoss();
            ft = null;
            fm.executePendingTransactions();
        }

        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return this.fragmentList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        int index = fragmentList.indexOf(object);
        if(index < 0) return POSITION_NONE;
        if(isChange[index]){
            isChange[index] = false;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }
}
