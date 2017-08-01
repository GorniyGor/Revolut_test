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

        //---------------------------------------------------


        //----RecyclerAdapter----

        //--------initialization
        /*mRecyclerViewTop = (RecyclerView)findViewById(R.id.id_recycler_top);
        mRecyclerViewBottom = (RecyclerView)findViewById(R.id.id_recycler_bottom);

        RecyclerAdapter.NotifyRecyclerChanged mNotifyRecyclerChangedTop =
                new RecyclerAdapter.NotifyRecyclerChanged() {
                    @Override
                    public void onNotify() {
                        mRecyclerViewBottom.getAdapter().notifyDataSetChanged();
                    }
                };
        RecyclerAdapter.NotifyRecyclerChanged mNotifyRecyclerChangedBottom =
                new RecyclerAdapter.NotifyRecyclerChanged() {
                    @Override
                    public void onNotify() {
                        mRecyclerViewTop.getAdapter().notifyDataSetChanged();
                    }
                };


        mRecyclerAdapterTop = new RecyclerAdapter(getLayoutInflater(), mNotifyRecyclerChangedTop);
        mRecyclerAdapterBottom = new RecyclerAdapter(getLayoutInflater(), mNotifyRecyclerChangedBottom);


        //---First recycler------

        mRecyclerViewTop.setAdapter(mRecyclerAdapterTop);
        mRecyclerViewTop.setLayoutManager(
                new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        mRecyclerViewTop.setHasFixedSize(true);

        SnapHelper mSnapHelperTop = new PagerSnapHelper();
        mSnapHelperTop.attachToRecyclerView(mRecyclerViewTop);
        //---Second recycler------

        mRecyclerViewBottom.setAdapter(mRecyclerAdapterBottom);
        mRecyclerViewBottom.setLayoutManager(
                new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        mRecyclerViewBottom.setHasFixedSize(true);

        SnapHelper mSnapHelperBottom = new PagerSnapHelper();
        mSnapHelperBottom.attachToRecyclerView(mRecyclerViewBottom);*/
        //---------------------------------

        //----Service------------------------------------------------

        //---Нам нужно обновление данных, когда какой-то из ресайклеров поменялся
        /*LoadService.NotifyListener mNotifyListener = new LoadService.NotifyListener() {
            @Override
            public void onNotify() {
                mRecyclerViewTop.getAdapter().notifyDataSetChanged();
                mRecyclerViewBottom.getAdapter().notifyDataSetChanged();
            }
        };

        Log.d(CurrencyList.TAG,"MainActivity: Does service null?" );
        if (service != null) {
            Log.d(CurrencyList.TAG,"MainActivity: service != null " );
            service.setNotifyListener(mNotifyListener);
        }
*/        //--------------------------------------------------------------------------------------

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
        private RecyclerAdapter mRecyclerAdapterTop;
        private RecyclerAdapter mRecyclerAdapterBottom;
        private CurrencyList mCurList = CurrencyList.getInstance();;


        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(BR_ACTION)) {

                mRecyclerViewTop = (RecyclerView) findViewById(R.id.id_recycler_top);
                mRecyclerViewBottom = (RecyclerView) findViewById(R.id.id_recycler_bottom);

               /* RecyclerAdapter.NotifyRecyclerChanged mNotifyRecyclerChangedTop =
                        new RecyclerAdapter.NotifyRecyclerChanged() {
                            @Override
                            public void onNotify() {
                                mRecyclerViewBottom.getAdapter().notifyDataSetChanged();
                            }
                        };
                RecyclerAdapter.NotifyRecyclerChanged mNotifyRecyclerChangedBottom =
                        new RecyclerAdapter.NotifyRecyclerChanged() {
                            @Override
                            public void onNotify() {
                                mRecyclerViewTop.getAdapter().notifyDataSetChanged();
                            }
                        };*/


                mRecyclerAdapterTop =
                        new RecyclerAdapter(getLayoutInflater()/*, idRecyclerTop*/ /* mNotifyRecyclerChangedTop*/);
                mRecyclerAdapterBottom =
                        new RecyclerAdapter(getLayoutInflater()/*, idRecyclerBottom*/ /*mNotifyRecyclerChangedBottom*/);


                //---First recycler------

                mRecyclerViewTop.setAdapter(mRecyclerAdapterTop);
                mRecyclerViewTop.setLayoutManager(
                        new MyLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));

                mRecyclerViewTop.setHasFixedSize(true);

                SnapHelper mSnapHelperTop = new PagerSnapHelper();
                mSnapHelperTop.attachToRecyclerView(mRecyclerViewTop);
                //---Second recycler------

                mRecyclerViewBottom.setAdapter(mRecyclerAdapterBottom);
                mRecyclerViewBottom.setLayoutManager(
                        new MyLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));

                mRecyclerViewBottom.setHasFixedSize(true);

                SnapHelper mSnapHelperBottom = new PagerSnapHelper();
                mSnapHelperBottom.attachToRecyclerView(mRecyclerViewBottom);

                //----------------------------

                //--Вызывается когда произошла закачка данных из сети--
                LoadService.NotifyListener mNotifyListener = new LoadService.NotifyListener() {
                    @Override
                    public void onNotify() {
                        if(mCurList.getCurrentlyExchangeSize() == 0){
                            mCurList.setCurrentlyExchange(mRecyclerViewTop, 0);
                            mCurList.setCurrentlyExchange(mRecyclerViewBottom, 0);
                        }

                        mRecyclerViewTop.getAdapter().notifyDataSetChanged();
                        mRecyclerViewBottom.getAdapter().notifyDataSetChanged();
                    }
                };

                Log.d(CurrencyList.TAG,"MainActivity: Does service null?" );
                if (service != null) {
                    Log.d(CurrencyList.TAG,"MainActivity: service != null " );
                    service.setNotifyListener(mNotifyListener);
                }

            }

        }

        public class MyLayoutManager extends LinearLayoutManager{

            public MyLayoutManager(Context context, int orientation, boolean reverseLayout) {
                super(context, orientation, reverseLayout);
            }

            //--Problem--Не вызывается
            //--Отвечает за обновление данных на вью при смене элемента (при сайпе)--
            @Override
            public void onItemsMoved(RecyclerView recyclerView, int from, int to, int itemCount){
                super.onItemsMoved(recyclerView, from, to, itemCount);

                //--Не понятно что именно такое from и to--
                Log.d(CurrencyList.TAG,"MainActivity, LayoutManager.onItemMoved: from-" + from +
                                " to-" + to );
                mCurList.setCurrentlyExchange(recyclerView, to);

                mRecyclerViewBottom.getAdapter().notifyDataSetChanged();
                mRecyclerViewTop.getAdapter().notifyDataSetChanged();

            }
        }
    }


}
