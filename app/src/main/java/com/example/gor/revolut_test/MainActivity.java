package com.example.gor.revolut_test;

import android.content.ServiceConnection;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;

import com.example.gor.revolut_test.Internet.LoadService;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerViewTop;
    RecyclerView mRecyclerViewBottom;
    private RecyclerAdapter mRecyclerAdapter;
    private LoadService service;
    private ServiceConnection serviceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

 /*       Toolbar mToolbar = (Toolbar) findViewById(R.id.id_toolbar_fx);
        setSupportActionBar(mToolbar);*/
        //---Тулбар менять через интерфейс в адаптере, использую parent из CurrencyList



        //----Service---------------------------------------

        //---------------------------------------------------


        //----RecyclerAdapter----
        mRecyclerAdapter = new RecyclerAdapter(getLayoutInflater());
        //---First recycler------
        mRecyclerViewTop = (RecyclerView)findViewById(R.id.id_recycler_top);
        mRecyclerViewTop.setAdapter(mRecyclerAdapter);
        mRecyclerViewTop.setLayoutManager(
                new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        mRecyclerViewTop.setHasFixedSize(true);

        SnapHelper mSnapHelperTop = new PagerSnapHelper();
        mSnapHelperTop.attachToRecyclerView(mRecyclerViewTop);
        //---Second recycler------
        mRecyclerViewBottom = (RecyclerView)findViewById(R.id.id_recycler_bottom);
        mRecyclerViewBottom.setAdapter(mRecyclerAdapter);
        mRecyclerViewBottom.setLayoutManager(
                new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        mRecyclerViewBottom.setHasFixedSize(true);
        SnapHelper mSnapHelperBottom = new PagerSnapHelper();
        mSnapHelperBottom.attachToRecyclerView(mRecyclerViewBottom);

        //---initialData----

        //---Нам нужно обновление данных, когда какой-то из ресайклеров поменялся
        /*LoadService.NotifyListener mNotifyListener = new LoadService.NotifyListener() {
            @Override
            public void onNotify() {
                mRecyclerViewTop.getAdapter().notifyDataSetChanged();
                mRecyclerViewBottom.getAdapter().notifyDataSetChanged();
            }
        };*/

        Log.d(CurrencyList.TAG,"MainActivity: Does service null?" );
        /*if (service != null) {
            Log.d(CurrencyList.TAG,"MainActivity: service != null " );
            service.setNotifyListener(mNotifyListener);
        }*/


    }

    @Override
    protected void onStop() {
        unbindService(serviceConnection);
        super.onStop();
    }
}
