package com.example.fishweather.util;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class SuggestionModel implements Serializable{
    String brf;
    String txt;
    String title;
    String code;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public SuggestionModel(String brf, String txt, String title,String code) {
        super();
        this.setBrf(brf);
        this.setTxt(txt);
        this.setCode(code);
        this.setTitle(title);
    }

    public String getBrf() {
        return brf;
    }

    public void setBrf(String brf) {
        this.brf = brf;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }
}
