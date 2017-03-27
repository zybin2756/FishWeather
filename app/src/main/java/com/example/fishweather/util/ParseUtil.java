package com.example.fishweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.example.fishweather.FishApplication;
import com.example.fishweather.db.City;
import com.example.fishweather.db.UserCity;
import com.example.fishweather.db.dbManage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by Administrator on 2017/3/20 0020.
 */

public class ParseUtil {

    public static Object parseModel(String value){
        if(value != null){
            ByteArrayInputStream in = new ByteArrayInputStream(Base64.decode(value,Base64.DEFAULT));
            try {
                ObjectInputStream oin = new ObjectInputStream(in);
                return oin.readObject();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public static boolean parseWeather(String data){
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray heweatherArray = jsonObject.getJSONArray("HeWeather5");
            jsonObject = heweatherArray.getJSONObject(0);

            String status = jsonObject.getString("status");

            if(status.equals("ok")) {
                String code = parseBasic(jsonObject.getJSONObject("basic"));
                 UserCity city = dbManage.loadUserCity(code);
                city.setUpdateTime(System.currentTimeMillis());
                city.save();
                parseNow(jsonObject.getJSONObject("now"), code);
                parseDailyForecast(jsonObject.getJSONArray("daily_forecast"), code);
                parseSuggestion(jsonObject.getJSONObject("suggestion"), code);
                parseAqi(jsonObject.getJSONObject("aqi"), code);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void parseAqi(JSONObject object, String code){
        try {
            SharedPreferences sp = FishApplication.getContext().getSharedPreferences(code,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            JSONObject city = object.getJSONObject("city");
            editor.putString("qlty",city.getString("qlty"));
//            editor.putString("o3",city.getString("o3"));
            editor.putString("aqi",city.getString("aqi"));
            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void parseSuggestion(JSONObject object, String code){
        try {
            SharedPreferences sp = FishApplication.getContext().getSharedPreferences(code,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();

            SuggestionModel model = null;
            //pcloth
            JSONObject jsonObject = object.getJSONObject("drsg");
            model = new SuggestionModel(jsonObject.getString("brf"),jsonObject.getString("txt"),"穿衣指数","pcloth");
            editor.putString("drsg",encodeObject(model));
            //舒适度指数
            jsonObject = object.getJSONObject("comf");
            model = new SuggestionModel(jsonObject.getString("brf"),jsonObject.getString("txt"),"舒适度指数","pcomfortable");
            editor.putString("comf",encodeObject(model));
            //prays
            jsonObject = object.getJSONObject("uv");
            model = new SuggestionModel(jsonObject.getString("brf"),jsonObject.getString("txt"),"紫外线指数","prays");
            editor.putString("uv",encodeObject(model));
            //pcoach
            jsonObject = object.getJSONObject("flu");
            model = new SuggestionModel(jsonObject.getString("brf"),jsonObject.getString("txt"),"感冒指数","pcoach");
            editor.putString("flu",encodeObject(model));
            //ptravel
            jsonObject = object.getJSONObject("trav");
            model = new SuggestionModel(jsonObject.getString("brf"),jsonObject.getString("txt"),"旅游指数","ptravel");
            editor.putString("trav",encodeObject(model));
            //psport
            jsonObject = object.getJSONObject("sport");
            model = new SuggestionModel(jsonObject.getString("brf"),jsonObject.getString("txt"),"运动指数","psport");
            editor.putString("sport",encodeObject(model));
            //pwashCar
            jsonObject = object.getJSONObject("cw");
            model = new SuggestionModel(jsonObject.getString("brf"),jsonObject.getString("txt"),"洗车指数","pwashcar");
            editor.putString("cw",encodeObject(model));
            //pair
            jsonObject = object.getJSONObject("air");
            model = new SuggestionModel(jsonObject.getString("brf"),jsonObject.getString("txt"),"空气指数","pair");
            editor.putString("air",encodeObject(model));
            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void parseDailyForecast(JSONArray jsonArray,String code){
        try {
            SharedPreferences sp = FishApplication.getContext().getSharedPreferences(code,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject object = jsonArray.getJSONObject(i);
                DailyForecastModel dailyForecastModel = new DailyForecastModel();
                JSONObject tmp = object.getJSONObject("tmp");
                dailyForecastModel.setMax(tmp.getString("max"));
                dailyForecastModel.setMin(tmp.getString("min"));

                JSONObject cond = object.getJSONObject("cond");
                dailyForecastModel.setTxt_d(cond.getString("txt_d"));
                dailyForecastModel.setTxt_n(cond.getString("txt_n"));
                dailyForecastModel.setCode_d(cond.getString("code_d"));
                dailyForecastModel.setCode_n(cond.getString("code_n"));
                editor.putString("day"+i,encodeObject(dailyForecastModel));
                editor.commit();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String encodeObject(Object object){
        ByteArrayOutputStream out = null;
        ObjectOutputStream oout = null;
        try {
            out = new ByteArrayOutputStream();
            oout = new ObjectOutputStream(out);
            oout.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(Base64.encode(out.toByteArray(),Base64.DEFAULT));
    }

    public static void parseNow(JSONObject nowObject,String code){
        try {
            SharedPreferences sp = FishApplication.getContext().getSharedPreferences(code,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();

            JSONObject windObject = nowObject.getJSONObject("wind");

            editor.putString("spd",windObject.getString("spd")); //风力等级
            editor.putString("dir",windObject.getString("dir")); //风向
            editor.putString("hum",nowObject.getString("hum")); //相对湿度
            editor.putString("tmp",nowObject.getString("tmp")); //温度

            JSONObject condObject = nowObject.getJSONObject("cond");
            editor.putString("txt",condObject.getString("txt"));
            editor.putString("code",condObject.getString("code"));
            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String parseBasic(JSONObject basicObject){
        String code = "";
        try {
            code = basicObject.getString("id");
            SharedPreferences sp = FishApplication.getContext().getSharedPreferences(code, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("code",code);
            editor.putString("city",basicObject.getString("city"));
            editor.putString("update",basicObject.getJSONObject("update").getString("loc"));
            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return code;
    }

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
