package com.example.fishweather.util;

import com.example.fishweather.db.City;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/3/20 0020.
 */

public class ParseUtil {
    public static boolean parseCityFromJson(String data){
        try {
            JSONArray jsonArray = new JSONArray(data);
            JSONObject jsonObject = null;
            City city = null;
            for(int i = 0; i < jsonArray.length(); i++){
                jsonObject = jsonArray.getJSONObject(i);
                String city_code = jsonObject.getString("id");
                String city_name = jsonObject.getString("cityZh");
                String province_name = jsonObject.getString("provinceZh");
                String country_name = jsonObject.getString("countryZh");
                String lat = jsonObject.getString("lat");
                String lon = jsonObject.getString("lon");

                city = new City();
                city.setCity_code(city_code);
                city.setCity_name(city_name);
                city.setCountry_name(country_name);
                city.setProvince_name(province_name);
                city.setLat(lat);
                city.setLon(lon);
                city.save();

                city = null;
            }
            jsonArray = null;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
