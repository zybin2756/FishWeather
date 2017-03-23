package com.example.fishweather.activity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.fishweather.db.UserCity;
import com.example.fishweather.db.dbManage;
import com.example.fishweather.itemTouch.DividerItemDecoration;
import com.example.fishweather.R;
import com.example.fishweather.adapter.CityManageAdapter;
import com.example.fishweather.itemTouch.myItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;

import com.example.fishweather.itemTouch.OnStartDragListener;
import com.example.fishweather.util.Constants;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

/**
 * Created by Administrator on 2017/3/21 0021.
 */

public class CityManageActivity extends AppCompatActivity implements View.OnClickListener,OnStartDragListener{
    public static void  actionStart(Context context){
        Intent intent = new Intent(context,CityManageActivity.class);
        context.startActivity(intent);
    }

    private ActionBar actionBar;
    private Button action_edit;
    private Button action_cancel;
    private ImageButton btn_add_city;
    private LinearLayout ll_add_city;
    private RecyclerView city_recyclerView;
    private CityManageAdapter cityManageAdapter;
    private ItemTouchHelper itemTouchHelper;

    private List<UserCity> cityList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manage);
        initView();
        initRecyclerView();
    }

    private void initRecyclerView(){
        cityList = new ArrayList<>();
        refreshDta();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        cityManageAdapter = new CityManageAdapter(cityList,this);
        city_recyclerView = (RecyclerView) findViewById(R.id.city_recyclerView);
        city_recyclerView.setAdapter(cityManageAdapter);
        city_recyclerView.setLayoutManager(layoutManager);
        city_recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));

        if(itemTouchHelper != null){
            itemTouchHelper.attachToRecyclerView(null);
        }
        itemTouchHelper = new ItemTouchHelper(new myItemTouchHelperCallback(cityManageAdapter));
        itemTouchHelper.attachToRecyclerView(city_recyclerView);

    }

    private void initView(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.city_toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        action_edit = (Button) toolbar.findViewById(R.id.action_edit);
        action_cancel = (Button) toolbar.findViewById(R.id.action_cancel);
        action_edit.setOnClickListener(this);
        action_cancel.setOnClickListener(this);

        ll_add_city = (LinearLayout) findViewById(R.id.ll_add_city);

        btn_add_city = (ImageButton) ll_add_city.findViewById(R.id.btn_add_city);
        btn_add_city.setOnClickListener(this);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }

        return  true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_edit:
                if(!cityManageAdapter.isModify()) {
                    enterModify(true);
                }else{
                    dbManage.saveUserCity(cityList);
                    enterModify(false);
                }
                break;
            case R.id.action_cancel:
                enterModify(false);
                refreshDta();
                break;
            case R.id.btn_add_city:
                Intent intent = new Intent(this,CitySearchActivity.class);
                startActivityForResult(intent, Constants.SEARCH_CITY);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.SEARCH_CITY: {
                if (Constants.SEARCH_CITY_ADD == resultCode) {
                    setResult(Constants.MANAGE_CITY_REFRESH);
                    finish();
                } else if (Constants.SEARCH_CITY_UNADD == resultCode) {

                }
                break;
            }
        }
    }

    private void refreshDta(){
        cityList.clear();
        cityList.addAll(dbManage.loadUserCity());
    }

    public void enterModify(boolean modify){
        if(modify){
            cityManageAdapter.setModify(true);
            actionBar.setDisplayHomeAsUpEnabled(false);
            action_cancel.setVisibility(View.VISIBLE);
            ll_add_city.setVisibility(View.GONE);
            action_edit.setText("确定");
        }else{
            cityManageAdapter.setModify(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            action_cancel.setVisibility(View.GONE);
            ll_add_city.setVisibility(View.VISIBLE);
            action_edit.setText("编辑");
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }
}
