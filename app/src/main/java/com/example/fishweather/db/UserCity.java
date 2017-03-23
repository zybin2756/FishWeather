package com.example.fishweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

public class UserCity extends DataSupport{
    private int id;
    private String city_code;
    private String city_name;
    private int city_index;

    public int getCity_index() {
        return city_index;
    }

    public void setCity_index(int city_index) {
        this.city_index = city_index;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity_code() {
        return city_code;
    }

    public void setCity_code(String city_code) {
        this.city_code = city_code;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }
}
