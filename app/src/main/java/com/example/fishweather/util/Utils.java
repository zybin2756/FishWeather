package com.example.fishweather.util;

import java.text.SimpleDateFormat;
import java.util.Date;

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
}
