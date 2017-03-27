package com.example.fishweather.db;

import android.util.Log;

import com.example.fishweather.FishApplication;
import com.example.fishweather.R;
import com.example.fishweather.util.Constants;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

public class dbManage {

    public static boolean existDataBase(){
        File file = new File(Constants.DATABASE_FILEPATH);
        if(!file.exists()){
            return false;
        }
        return true;
    }

    public static boolean isExistUserCity(String cityName){
        return DataSupport.isExist(UserCity.class,"city_name=?",cityName);
    }

    public static void saveUserCity(List<UserCity> cityList){

        if(cityList.size() >0) {
            UserCity city;
            StringBuilder limit = new StringBuilder("(");
            for (int i = 0; i < cityList.size(); i++) {
                city = cityList.get(i);
                city.setCity_index(i + 1);
                city.save();
                limit.append("'" + city.getCity_name() + "',");
            }
            limit.deleteCharAt(limit.length() - 1);
            limit.append(")");
            //        Log.i("zyb",limit.toString());
            DataSupport.deleteAll(UserCity.class, "city_name not in " + limit.toString());
        }
        else{
            DataSupport.deleteAll(UserCity.class);
        }
    }
    public static List<UserCity> loadUserCity(){
        return DataSupport.order("city_index asc").find(UserCity.class);
    }

    public static UserCity loadUserCity(String city_code){
        List<UserCity> cityList = DataSupport.where("city_code = ?",city_code).find(UserCity.class);
        if(cityList.size() > 0){
            return  cityList.get(0);
        }
        return null;
    }

    public static boolean copyDataBase() {
        File dir = new File(Constants.DATABASE_PATH);
        if (!dir.exists()) {
            dir.mkdir();
        }
        FileOutputStream os = null;
        InputStream is = null;
        try {
            os = new FileOutputStream(Constants.DATABASE_FILEPATH);
            is = FishApplication.getContext().getResources().openRawResource(R.raw.fishweather);
            byte[] buffer = new byte[2048];
            int count = 0;
            while ((count = is.read(buffer)) > 0) {
                os.write(buffer, 0, count);
                os.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(os != null)
                    os.close();
                if(is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static List<City> searchCity(String cityName){
        List<City> cityList = null;
        if(cityName.length() > 0) {
            cityList = DataSupport.where("city_name like ?", "%" + cityName + "%").find(City.class);
        }
        return cityList;
    }
}
