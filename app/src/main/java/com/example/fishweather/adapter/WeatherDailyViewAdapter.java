package com.example.fishweather.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fishweather.FishApplication;
import com.example.fishweather.R;
import com.example.fishweather.model.DailyForecastModel;
import com.example.fishweather.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/27 0027.
 */

public class WeatherDailyViewAdapter extends RecyclerView.Adapter<WeatherDailyViewAdapter.ViewHolder> {

    List<DailyForecastModel> list = new ArrayList<>();

    public void setList(List<DailyForecastModel> list) {
        this.list.clear();
        if(list != null) {
            this.list.addAll(list);
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather_daily,parent,false);
        ViewHolder holder =  new ViewHolder(itemView);
        return  holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DailyForecastModel model = list.get(position);
        holder.daily_day.setText(model.getDay());
        String txt_d = model.getTxt_d();
        String txt_n = model.getTxt_n();
        if(txt_d.equals(txt_n)){
            holder.daily_statu.setText(txt_d);
        }else{
            holder.daily_statu.setText(txt_d+"转"+txt_n);
        }

        String max = model.getMax();
        String min = model.getMin();

        holder.daily_temp.setText(max+"°/"+min+"°");

        String code = "p100";
        if(Utils.isDayOrNight()) {
            code = "p" + model.getCode_d();
        }else{
            code = "p" + model.getCode_n();
        }

        int id = FishApplication.getContext().getResources().getIdentifier(code,"mipmap",FishApplication.getContext().getPackageName());
        holder.daily_icon.setImageResource(id);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView daily_icon;
        private TextView daily_day;
        private TextView daily_statu;
        private TextView daily_temp;

        public ViewHolder(View itemView) {
            super(itemView);
            daily_icon = (ImageView) itemView.findViewById(R.id.daily_icon);
            daily_day = (TextView) itemView.findViewById(R.id.daily_day);
            daily_statu = (TextView) itemView.findViewById(R.id.daily_statu);
            daily_temp = (TextView) itemView.findViewById(R.id.daily_temp);
        }
    }
}
