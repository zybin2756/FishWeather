package com.example.fishweather.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fishweather.FishApplication;
import com.example.fishweather.R;
import com.example.fishweather.util.DailyForecastModel;
import com.example.fishweather.util.SuggestionModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/27 0027.
 */

public class WeatherSuggestionViewAdapter extends RecyclerView.Adapter<WeatherSuggestionViewAdapter.ViewHolder> {

    List<SuggestionModel> list = new ArrayList<>();

    public void setList(List<SuggestionModel> list) {
        this.list.clear();
        if(list != null) {
            this.list.addAll(list);
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather_suggestion,parent,false);
        ViewHolder holder =  new ViewHolder(itemView);
        return  holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SuggestionModel model = list.get(position);
        holder.suggest_brf.setText(model.getTitle());
        holder.suggest_txt.setText(model.getTxt());

        String code = model.getCode();
        int resId = FishApplication.getContext().getResources().getIdentifier(code,"mipmap",FishApplication.getContext().getPackageName());
        holder.suggest_icon.setImageResource(resId);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView suggest_icon;
        private TextView suggest_brf;
        private TextView suggest_txt;

        public ViewHolder(View itemView) {
            super(itemView);
            suggest_icon = (ImageView) itemView.findViewById(R.id.suggest_icon);
            suggest_brf = (TextView) itemView.findViewById(R.id.suggest_brf);
            suggest_txt = (TextView) itemView.findViewById(R.id.suggest_txt);
        }
    }
}
