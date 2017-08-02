package com.example.gor.revolut_test;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;

import com.example.gor.revolut_test.Internet.LoadService;

public class MainActivity extends AppCompatActivity {

    /*RecyclerView mRecyclerViewTop;
    RecyclerView mRecyclerViewBottom;
    private RecyclerAdapter mRecyclerAdapterTop;
    private RecyclerAdapter mRecyclerAdapterBottom;*/
    public final static String BR_ACTION = "com.example.gor.revolut_test";

    private LoadService service;
    private ServiceConnection serviceConnection;
    private RecyclerBroadcastReceiver broadcastReceiver = new RecyclerBroadcastReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerReceiver(broadcastReceiver, new IntentFilter(BR_ACTION));


 /*       Toolbar mToolbar = (Toolbar) findViewById(R.id.id_toolbar_fx);
        setSupportActionBar(mToolbar);*/
        //---Тулбар менять через интерфейс в адаптере, использую parent из CurrencyList



        //----Service---------------------------------------

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder binder) {
                LoadService.MyBinder b = (LoadService.MyBinder) binder;
                service = b.getService();
                sendBroadcast(new Intent().setAction(BR_ACTION));
                Log.d(CurrencyList.TAG,"onServiceConnected: getService" );
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                service = null;
            }
        };

        Intent intent = new Intent(this, LoadService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);

    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(serviceConnection);
        unregisterReceiver(broadcastReceiver);
    }

    public class RecyclerBroadcastReceiver extends BroadcastReceiver {
        private static final String idRecyclerTop = "TOP";
        private static final String idRecyclerBottom = "BOTTOM";

        RecyclerView mRecyclerViewTop;
        RecyclerView mRecyclerViewBottom;
        //RecyclerView.OnScrollListener scrollListener;
        private RecyclerAdapter mRecyclerAdapterTop;
        private RecyclerAdapter mRecyclerAdapterBottom;
        private CurrencyList mCurList = CurrencyList.getInstance();


        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(BR_ACTION)) {

                //--Вызывается когда произошла закачка данных из сети--
                LoadService.NotifyListener mNotifyListener = new LoadService.NotifyListener() {
                    @Override
                    public void onNotify() {
                        if(mCurList.getCurrentlyExchangeSize() < 2){
                            mCurList.setCurrentlyExchange(mRecyclerViewTop, 0);
                            mCurList.setCurrentlyExchange(mRecyclerViewBottom, 0);

                        }

                        Log.d(CurrencyList.TAG,"MainActivity: NotifyListener.onNotify setCurrentlyExchange" );
                        mRecyclerViewTop.getAdapter().notifyDataSetChanged();
                        mRecyclerViewBottom.getAdapter().notifyDataSetChanged();
                    }
                };
                service.setNotifyListener(mNotifyListener);

                //----------------------------------------------------------------------------------

                mRecyclerViewTop = (RecyclerView) findViewById(R.id.id_recycler_top);
                mRecyclerViewBottom = (RecyclerView) findViewById(R.id.id_recycler_bottom);

                //--Нужно для понимания каждым ресайклером, какая валюта в данный момент отображена в другом--
                CurrencyList.RV_NAMES.put(idRecyclerTop, mRecyclerViewTop);
                CurrencyList.RV_NAMES.put(idRecyclerBottom, mRecyclerViewBottom);

                /*scrollListener = new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        Log.d(CurrencyList.TAG,"MainActivity, onScrollListener: dx-" + dx +
                                " dy-" + dy );

                        *//*if()
                        mCurList.setCurrentlyExchange(recyclerView, to);

                        mRecyclerViewBottom.getAdapter().notifyDataSetChanged();
                        mRecyclerViewTop.getAdapter().notifyDataSetChanged();*//*

                    }
                };*/

                /*scrollListener = new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        Log.d(CurrencyList.TAG,"MainActivity, onScrollListener: new state- " + newState);
                    }
                };*/

                /*ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(
                        new ItemTouchHelper.SimpleCallback(
                                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ) {
                            @Override
                            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                                return false;
                            }

                            @Override
                            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                            }
                        });*/


                mRecyclerAdapterTop =
                        new RecyclerAdapter(getLayoutInflater(), idRecyclerTop );
                mRecyclerAdapterBottom =
                        new RecyclerAdapter(getLayoutInflater(), idRecyclerBottom );


                //---First recycler------

                mRecyclerViewTop.setAdapter(mRecyclerAdapterTop);
                mRecyclerViewTop.setLayoutManager(
                        new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));

                mRecyclerViewTop.setHasFixedSize(true);
                //mRecyclerViewTop.addOnScrollListener(scrollListener);

                SnapHelper mSnapHelperTop = new PagerSnapHelper();
                mSnapHelperTop.attachToRecyclerView(mRecyclerViewTop);

                //---Second recycler------

                mRecyclerViewBottom.setAdapter(mRecyclerAdapterBottom);
                mRecyclerViewBottom.setLayoutManager(
                        new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));

                mRecyclerViewBottom.setHasFixedSize(true);
                //mRecyclerViewBottom.addOnScrollListener(scrollListener);

                SnapHelper mSnapHelperBottom = new PagerSnapHelper();
                mSnapHelperBottom.attachToRecyclerView(mRecyclerViewBottom);

                //----------------------------

            }

        }
    }


}
