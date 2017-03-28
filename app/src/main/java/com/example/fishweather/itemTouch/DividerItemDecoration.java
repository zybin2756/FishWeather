package com.example.fishweather.itemTouch;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2017/3/21.
 */

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    private Drawable mDivider;

    private int mOrientation;


    public DividerItemDecoration(Context context, int orientation) {
        setmOrientation(orientation);
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    public void setmOrientation(int mOrientation) {
        if (mOrientation != HORIZONTAL_LIST && mOrientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        this.mOrientation = mOrientation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if(this.mOrientation == VERTICAL_LIST){
            drawVertical(c,parent);
        }else{
            drawHorizontal(c,parent);
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent){
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int count = parent.getChildCount();
        for(int i = 0; i < count; i++){
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left,top,right,bottom);
            mDivider.draw(c);
        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent){
        final int top = parent.getTop();
        final int bottom = parent.getHeight() - parent.getBottom();

        final int count = parent.getChildCount();
        for(int i = 0; i < count; i++){
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getPaddingRight() - params.rightMargin;
            final int right = left + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left,top,right,bottom);
            mDivider.draw(c);
        }
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == VERTICAL_LIST) {
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        }
    }
}