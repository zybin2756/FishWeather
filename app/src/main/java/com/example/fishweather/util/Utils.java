package com.example.fishweather.util;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.example.fishweather.Constants;
import com.example.fishweather.FishApplication;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/3/27.
 */

public class Utils {
    public static boolean isDayOrNight(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String hour = sdf.format(new Date());
        int tmp = Integer.parseInt(hour);
        if(tmp > 6 && tmp < 18){
            return true;
        }
        return false;
    }

    public static String[] getDeniedPermisson(){
        //申请权限

        List<String> permissonList = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(FishApplication.getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            permissonList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(FishApplication.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            permissonList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(FishApplication.getContext(), Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED){
            permissonList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(FishApplication.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            permissonList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if(!permissonList.isEmpty()){
            String [] permissions = permissonList.toArray(new String[permissonList.size()]);
            return  permissions;
        }

        return null;
    }
}
