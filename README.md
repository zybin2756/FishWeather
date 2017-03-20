# FishWeather



创建 City类继承 DataSupport
public class City extends DataSupport;
在litepal.xml中添加
 <list>
    <mapping class="com.example.fishweather.db.City"></mapping>
 </list>

由于接下来需要访问api接口获取数据,所以先定义一个用来解析url的工具类

使用okHttp来访问网络

首先,在build.gradle dependencies中添加  compile 'com.squareup.okhttp3:okhttp:3.4.1'

创建 类 HttpUtil
创建 静态方法 public static void sendOkHttpRequest
提供处理完成的接口：
public interface HttpCallBack{
        public void onError(String msg);
        public void onFinish(String data);
    }

创建 类 ParseUtil 用于解析数据

首先解析城市信息 由于是json 所以考虑用GSON 或者 JSONObject

parseCityFromJson

创建 WeatherInfosActivity 用来显示天气