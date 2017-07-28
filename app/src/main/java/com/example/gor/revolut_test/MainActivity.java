package com.example.gor.revolut_test;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;

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

        //----Service---------------------------------------
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder binder) {
                LoadService.MyBinder b = (LoadService.MyBinder) binder;
                service = b.getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                service = null;
            }
        };
        Intent intent = new Intent(getBaseContext(), LoadService.class);
        bindService(intent, serviceConnection,BIND_AUTO_CREATE);
        //---------------------------------------------------


        //----RecyclerAdapter----
        mRecyclerAdapter = new RecyclerAdapter(getLayoutInflater(), service);
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

        //---Нам нужено обновление данных, когда какой-то из ресайклеров поменялся
        LoadService.NotifyListener mNotifyListener = new LoadService.NotifyListener() {
            @Override
            public void onNotify() {
                mRecyclerViewTop.getAdapter().notifyDataSetChanged();
                mRecyclerViewBottom.getAdapter().notifyDataSetChanged();
            }
        };

        if (service != null) {
            service.setNotifyListener(mNotifyListener);
        }


    }

    @Override
    protected void onStop() {
        unbindService(serviceConnection);
        super.onStop();
    }
}
