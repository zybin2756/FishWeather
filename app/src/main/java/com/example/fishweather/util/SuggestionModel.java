package com.example.fishweather.util;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class SuggestionModel implements Serializable{
    String brf;
    String txt;

    public SuggestionModel( String brf, String txt) {
        super();
        this.setBrf(brf);
        this.setTxt(txt);
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
