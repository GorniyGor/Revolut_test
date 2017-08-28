package com.example.gor.revolut_test;

import android.app.AlarmManager;
import android.app.PendingIntent;
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
import android.widget.TextView;

import com.example.gor.revolut_test.Currency_selection.ModelListCalling;
import com.example.gor.revolut_test.Currency_selection.SelectionFragment;
import com.example.gor.revolut_test.Internet.LoadService;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public final static String BR_ACTION = "com.example.gor.revolut_test";
    public final static String UPDATE_ACTION = "update data into recycler";

    private LoadService service;
    private ServiceConnection serviceConnection;
    private RecyclerBroadcastReceiver broadcastReceiver = new RecyclerBroadcastReceiver();

    private TextView textViewDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerReceiver(broadcastReceiver, new IntentFilter(BR_ACTION));

 /*     // может на будущее
        Toolbar mToolbar = (Toolbar) findViewById(R.id.id_toolbar_fx);
        setSupportActionBar(mToolbar);*/

        textViewDate = (TextView) findViewById(R.id.id_text_update_time);

        //--Service initialization----------------------------------
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder binder) {
                LoadService.MyBinder b = (LoadService.MyBinder) binder;
                service = b.getService();
                sendBroadcast(new Intent().setAction(BR_ACTION));
                /*Log.d(CurrencyList.TAG,"onServiceConnected: getService" );*/
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                service = null;
            }
        };

        Intent intent = new Intent(this, LoadService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        //----------------------------------------------------------------
        //------



    }

    public class RecyclerBroadcastReceiver extends BroadcastReceiver {
        private static final String idRecyclerTop = "TOP";
        private static final String idRecyclerBottom = "BOTTOM";

        RecyclerView mRecyclerViewTop;
        RecyclerView mRecyclerViewBottom;
        private RecyclerAdapter mRecyclerAdapterTop;
        private RecyclerAdapter mRecyclerAdapterBottom;
        private CurrencyList mCurList = CurrencyList.getInstance();
        private AlarmManager mAlarmManager;

        /*private GregorianCalendar gCalendar = new GregorianCalendar();*/


        @Override
        public void onReceive(final Context context, Intent intent) {

                registerReceiver(new DataUpdateBroadcastReceiver(), new IntentFilter(UPDATE_ACTION));



                //--Вызывается когда произошла закачка данных из сети-----
                LoadService.NotifyListener mNotifyListener = new LoadService.NotifyListener() {
                    @Override
                    public void onNotify() {
                        if(mCurList.getCurrentlyExchangeSize() < 2){
                            mCurList.setCurrentlyExchange(mRecyclerViewTop, 0);
                            mCurList.setCurrentlyExchange(mRecyclerViewBottom, 0);

                        }

                        /*Log.d(CurrencyList.TAG,"MainActivity: NotifyListener.onNotify setCurrentlyExchange" );*/

                        sendBroadcast(new Intent().setAction(UPDATE_ACTION).
                                putExtra("adapter", "both"));

                        // Установка времени последнего обновления валют
                        textViewDate.setText("Updated " +
                                new SimpleDateFormat("HH:mm, ").format(new Date()) +
                                new SimpleDateFormat("dd-MM-yyyy").
                                        format(mCurList.getLastUpdateDate()));
                    }
                };
                service.setNotifyListener(mNotifyListener);

            //--------------------------------------------------------------------------------------

                mRecyclerViewTop = (RecyclerView) findViewById(R.id.id_recycler_top);
                mRecyclerViewBottom = (RecyclerView) findViewById(R.id.id_recycler_bottom);

                //--Нужно для понимания каждым ресайклером,
                // какая валюта в данный момент отображена в другом-------

                //--optimization--скорее даже не нужно,
                // т.к. можно в SnapHelper и onNotify просто свои id передавать
                CurrencyList.RV_NAMES.put(idRecyclerTop, mRecyclerViewTop);
                CurrencyList.RV_NAMES.put(idRecyclerBottom, mRecyclerViewBottom);



                mRecyclerAdapterTop =
                        new RecyclerAdapter(getLayoutInflater(), idRecyclerTop );
                mRecyclerAdapterBottom =
                        new RecyclerAdapter(getLayoutInflater(), idRecyclerBottom );


                //---First recycler---------------------------

                mRecyclerViewTop.setAdapter(mRecyclerAdapterTop);
                mRecyclerViewTop.setLayoutManager(
                        new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));

                mRecyclerViewTop.setHasFixedSize(true);

            //--Для реализации свайпа, а не простого скролла вью-----

                //--Problem--Ложные срабатывания - подёргивания, но не полные сдвиги
                SnapHelper mSnapHelperTop = new PagerSnapHelper() {
                    @Override
                    public boolean onFling (int velocityX, int velocityY){

                        /*Log.d(CurrencyList.TAG,"MainActivity: SnapHelper.onFling TOP" +
                                " velocityX- " + velocityX + " ; ScrollDistance- " +
                                calculateScrollDistance(velocityX, velocityY)[0]);*/

                        // Для понимания, какую валюту показывать при свайпе
                        if(velocityX > 0) mCurList.changeCurrentlyExchange(mRecyclerViewTop, 1);
                        else mCurList.changeCurrentlyExchange(mRecyclerViewTop, -1);

                        // Добавляем обнуление cash при свайпе,
                        // если данным ресайклером последнее значение и было установлено
                        if(mCurList.mCash.getChanger().equals(idRecyclerTop)){

                            /*Log.d(CurrencyList.TAG,"MainActivity.onFling: TOP CLEARING");*/

                            mCurList.mCash.set("", "", 0);
                        }
                        sendBroadcast(new Intent().setAction(UPDATE_ACTION).
                                putExtra("adapter", "BOTTOM"));
                        // Себя обновляем чтобы не было сохранено значение эдитора при свайпе,
                        // если мы его тут занулили
                        sendBroadcast(new Intent().setAction(UPDATE_ACTION).
                                putExtra("adapter", "TOP"));


                        return super.onFling(velocityX, velocityY);
                    }
                };
                mSnapHelperTop.attachToRecyclerView(mRecyclerViewTop);

            //--Для обновления ресайклера, когда ввели сумму, которую нужно перевести-----
            mRecyclerAdapterTop.setNotifyCashChanged(new RecyclerAdapter.NotifyCashChanged() {
                @Override
                public void onNotify(double cash) {

                    /*Log.d(CurrencyList.TAG,"MainActivity.setNotifyCashChanged: TOP " +
                    currencyName + " " + cash);*/

                    mCurList.mCash.set( idRecyclerTop,
                            mCurList.getCurrencyFrom(idRecyclerBottom), cash);
                    sendBroadcast(new Intent().setAction(UPDATE_ACTION).
                            putExtra("adapter", "BOTTOM"));
                }
            });

            //--Для работы со списком валют
            final ModelListCalling model = new ModelListCalling();
            final SelectionFragment fragment = new SelectionFragment();
            mRecyclerAdapterTop.setCallbackCurrencyClickListener(
                    new RecyclerAdapter.CallbackCurrencyClickListener() {
                @Override
                public void onClick() {
                    if(model.isSelected()){
                        getSupportFragmentManager().beginTransaction()
                                .remove(fragment).commit();
                    }
                    else getSupportFragmentManager().beginTransaction()
                                .add(R.id.id_frame_layout, fragment).commit();
                    model.setSelected(!model.isSelected());
                }
            });

                //---Second recycler--------------------------------------

            //--Аналогично first recycler

                mRecyclerViewBottom.setAdapter(mRecyclerAdapterBottom);
                mRecyclerViewBottom.setLayoutManager(
                        new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));

                mRecyclerViewBottom.setHasFixedSize(true);

                SnapHelper mSnapHelperBottom = new PagerSnapHelper(){
                    @Override
                    public boolean onFling (int velocityX, int velocityY){

                        /*Log.d(CurrencyList.TAG,"MainActivity: SnapHelper.onFling BOTTOM" +
                                " velocityX- " + velocityX + " ; ScrollDistance- " +
                                calculateScrollDistance(velocityX, velocityY)[0]);*/

                        if(velocityX > 0) mCurList.changeCurrentlyExchange(mRecyclerViewBottom, 1);
                        else mCurList.changeCurrentlyExchange(mRecyclerViewBottom, -1);

                        if(mCurList.mCash.getChanger().equals(idRecyclerBottom)){

                            /*Log.d(CurrencyList.TAG,"MainActivity.onFling: BOTTOM CLEARING");*/

                            mCurList.mCash.set("", "", 0);
                        }

                        sendBroadcast(new Intent().setAction(UPDATE_ACTION).
                                putExtra("adapter", "TOP"));
                        // Себя обновляем чтобы не было сохранено значение эдитора при свайпе,
                        // если мы его тут занулили
                        sendBroadcast(new Intent().setAction(UPDATE_ACTION).
                                putExtra("adapter", "BOTTOM"));

                        return super.onFling(velocityX, velocityY);
                    }
                };
                mSnapHelperBottom.attachToRecyclerView(mRecyclerViewBottom);

            //--Для обновления другого ресайклера, когда ввели сумму, которую нужно перевести---
            mRecyclerAdapterBottom.setNotifyCashChanged(new RecyclerAdapter.NotifyCashChanged() {
                @Override
                public void onNotify(double cash) {

                    /*Log.d(CurrencyList.TAG,"MainActivity.setNotifyCashChanged: BOTTOM " +
                            currencyName + " " + cash);*/

                    mCurList.mCash.set( idRecyclerBottom,
                            mCurList.getCurrencyFrom(idRecyclerTop), cash);
                    sendBroadcast(new Intent().setAction(UPDATE_ACTION).
                            putExtra("adapter", "TOP"));
                }
            });


            //--------------------------------------------------------------------------------------

            //------Периодизация подкачки данных с сайта------
            service.loadData();
            mAlarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

            Intent alarmIntent = new Intent(context, LoadBroadcastReceiver.class);
            PendingIntent alarmPending = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
            //--maybeProblem--Система сдигает время до 60000 ms
            mAlarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), 30000, alarmPending);

            //--------------------------------------------------------------------------------------

        }

        //--Для обновления курсов на экране при смене валют.
        // Бродкаст нужен, чтобы изменения данных успевали происходить в системе,
        // т.е. просто для разнесенных по времени вызовов - плохое решение
        public class DataUpdateBroadcastReceiver extends BroadcastReceiver{

            @Override
            public void onReceive(Context context, Intent intent) {

                switch (intent.getStringExtra("adapter")){
                    case "both": {
                        mRecyclerViewTop.getAdapter().notifyDataSetChanged();
                        mRecyclerViewBottom.getAdapter().notifyDataSetChanged();
                        break;
                    }
                    case idRecyclerTop:
                        mRecyclerViewTop.getAdapter().notifyDataSetChanged();
                        break;
                    case idRecyclerBottom:
                        mRecyclerViewBottom.getAdapter().notifyDataSetChanged();
                        break;
                }

            }
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        AlarmManager alarmManagerCanceled = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarmManagerCanceled.cancel(PendingIntent.getBroadcast(this, 0,
                new Intent(this, LoadBroadcastReceiver.class), 0));
        unbindService(serviceConnection);
        unregisterReceiver(broadcastReceiver);
    }

}
