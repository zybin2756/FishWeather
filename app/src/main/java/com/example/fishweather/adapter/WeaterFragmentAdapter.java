package com.example.fishweather.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.fishweather.activity.WeatherFragment;

import java.util.List;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class WeaterFragmentAdapter extends FragmentPagerAdapter {


    List<WeatherFragment> fragmentList;
    public WeaterFragmentAdapter(FragmentManager fm, List<WeatherFragment> fragmentList){
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return this.fragmentList.size();
    }
}
