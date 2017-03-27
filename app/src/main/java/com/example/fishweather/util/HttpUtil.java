package com.example.fishweather.util;

import android.widget.Toast;

import com.example.fishweather.FishApplication;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/3/20 0020.
 */

public class HttpUtil {
    public static void sendOkHttpRequest(String path, final HttpCallBack httpCallback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(path).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                httpCallback.onError(e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    httpCallback.onFinish(response.body().string());
                }else{
                    httpCallback.onError(response.message());
                }
            }
        });
    }

    public interface HttpCallBack{
        public void onError(String msg);
        public void onFinish(String data);
    }

    public static void  loadWeatherInfo(String cityCode,HttpUtil.HttpCallBack callBack){
        String path = "https://free-api.heweather.com/v5/weather?city="+cityCode+"&&key=65c50b6d014c4de3adf356741cbdd7d4";
        HttpUtil.sendOkHttpRequest(path,callBack);
    }
}
