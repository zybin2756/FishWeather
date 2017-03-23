package com.example.fishweather.adapter;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fishweather.R;
import com.example.fishweather.db.UserCity;
import com.example.fishweather.itemTouch.OnStartDragListener;
import com.example.fishweather.itemTouch.myItemTouchHelperCallback;

import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017/3/21 0021.
 */

public class CityManageAdapter extends RecyclerView.Adapter<CityManageAdapter.ViewHolder> implements myItemTouchHelperCallback.ItemTouchHeplerAdapter{

    private List<UserCity> cityList;
    private boolean isModify;
    private OnStartDragListener listener;
    public CityManageAdapter(List<UserCity> cityList,OnStartDragListener listener) {
        this.cityList = cityList;
        this.isModify = false;
        this.listener = listener;
    }

    public boolean isModify() {
        return isModify;
    }

    public void setModify(boolean modify) {
        isModify = modify;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_city_manage,parent,false);

        final ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.action_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                cityList.remove(position);
                notifyDataSetChanged();
            }
        });

        viewHolder.action_move.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN){
                    listener.onStartDrag(viewHolder);
                }
                return false;
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String cityName = cityList.get(position).getCity_name();
        if(isModify){
            holder.action_remove.setVisibility(View.VISIBLE);
            holder.action_move.setVisibility(View.VISIBLE);
        }else{
            holder.action_remove.setVisibility(View.GONE);
            holder.action_move.setVisibility(View.GONE);
        }
        holder.item_city_name.setText(cityName);
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    @Override
    public boolean onItemMove(int from, int to) {
        Collections.swap(cityList, from, to);
        notifyItemMoved(from, to);
        return false;
    }

    @Override
    public void onItemRemove(int position) {
        cityList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean isCanMove() {
        return isModify;
    }

    @Override
    public boolean isCanRemove() {
        return isModify;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView item_city_name;
        ImageView action_remove;
        ImageView action_move;
        public ViewHolder(final View itemView) {
            super(itemView);
            item_city_name = (TextView) itemView.findViewById(R.id.item_city_name);
            action_remove = (ImageView) itemView.findViewById(R.id.action_remove);
            action_move = (ImageView) itemView.findViewById(R.id.action_move);
        }
    }
}
