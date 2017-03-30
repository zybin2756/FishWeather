package com.example.fishweather.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fishweather.R;
import com.example.fishweather.activity.CitySearchActivity;
import com.example.fishweather.db.City;
import com.example.fishweather.db.UserCity;
import com.example.fishweather.db.dbManage;
import com.example.fishweather.Constants;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

public class CitySearchAdapter extends RecyclerView.Adapter<CitySearchAdapter.ViewHolder>{
    List<City> cityList = new ArrayList<>();

    closeListener listener;
    public CitySearchAdapter(closeListener listener) {
        super();
        this.listener = listener;
    }

    public void setCityList(List<City> cityList) {
        this.cityList.clear();
        if(cityList != null){
            this.cityList.addAll(cityList);
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_city_search,parent,false);
        final ViewHolder holder = new ViewHolder(view);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                City city = cityList.get(pos);

                dbManage.saveUserCity(city);

                listener.onFinish(city.getCity_code());

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        City city = cityList.get(position);
        holder.cityName.setText(city.getCity_name() + "-" + city.getProvince_name() + "-" + city.getCountry_name());
        if(dbManage.isExistUserCity(city.getCity_name())){
            holder.hint.setVisibility(View.VISIBLE);
        }else{
            holder.hint.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView cityName;
        TextView hint;
        public ViewHolder(View itemView) {
            super(itemView);
            cityName = (TextView) itemView.findViewById(R.id.item_city_name);
            hint = (TextView) itemView.findViewById(R.id.item_select_hint);
        }
    }

    public interface closeListener{
        void onFinish(String code);
    }
}
