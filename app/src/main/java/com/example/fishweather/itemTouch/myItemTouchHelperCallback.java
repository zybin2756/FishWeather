package com.example.fishweather.itemTouch;

import android.graphics.Canvas;
import android.graphics.Color;
import android.media.browse.MediaBrowser;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.fishweather.adapter.CityManageAdapter;

/**
 * Created by Administrator on 2017/3/22 0022.
 */

public class myItemTouchHelperCallback extends ItemTouchHelper.Callback {

    public static final float ALPHA_FULL = 1.0f;

    private ItemTouchHeplerAdapter itemTouchAdapter;
    public myItemTouchHelperCallback( ItemTouchHeplerAdapter adapter) {
        super();
        this.itemTouchAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return itemTouchAdapter.isCanMove();
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return itemTouchAdapter.isCanRemove();
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if(layoutManager instanceof GridLayoutManager){
            final int dragFlag = ItemTouchHelper.UP | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN;
            final int swipFlag = 0;

            return makeMovementFlags(dragFlag, swipFlag);
        }else {
            final int dragFlag = ItemTouchHelper.UP | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN;
            final int swipFlag = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlag, swipFlag);
        }
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder1) {
        if(viewHolder.getItemViewType() != viewHolder1.getItemViewType()){
            return false;
        }
        itemTouchAdapter.onItemMove(viewHolder.getAdapterPosition(),viewHolder1.getAdapterPosition());
        return true;
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setAlpha(ALPHA_FULL);
        viewHolder.itemView.setBackgroundColor(0);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);
        if(actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            final float alpha = ALPHA_FULL - Math.abs(dX)/(float)viewHolder.itemView.getWidth();
            viewHolder.itemView.setAlpha(alpha);
            viewHolder.itemView.setTranslationX(dX);
        }else{
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
        itemTouchAdapter.onItemRemove(viewHolder.getAdapterPosition());
    }

    public interface ItemTouchHeplerAdapter{
        boolean onItemMove(int from, int to);
        void onItemRemove(int position);
        boolean isCanMove();
        boolean isCanRemove();
    }
}
