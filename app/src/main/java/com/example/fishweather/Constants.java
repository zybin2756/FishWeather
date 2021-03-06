package com.example.fishweather;

/**
 * Created by Administrator on 2017/3/20 0020.
 */

public class Constants {
    public static final int LOAD_CITY_INFO_ERROR = 1;
    public static final int LOAD_CITY_INFO_FINISH = 2;

    public static final int SEARCH_CITY = 3;
    public static final int SEARCH_CITY_ADD = 4;
    public static final int SEARCH_CITY_UNADD = 5;

    public static final int MANAGE_CITY = 6;
    public static final int MANAGE_CITY_REFRESH = 7;

    public static final int WEATHER_REFRESH = 8;
    public static final int WEATHER_REFRESHED = 9;

    public static final int REQUEST_PERMISSION = 9;

    public static final int LOCATION_CITY = 10;
    public static final int LOCATION_CITY_REFRESH = 11;
    public static final String DATABASE_PATH = "/data/data/com.example.fishweather/databases";
    public static final String DATABASE_NAME = "FishWeather.db";
    public static final String DATABASE_FILEPATH = DATABASE_PATH+"/"+DATABASE_NAME;

    public static final String CONFIG_FILE_NAME = "config";
}
