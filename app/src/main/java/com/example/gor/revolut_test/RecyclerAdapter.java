package com.example.gor.revolut_test;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Gor on 24.07.2017.
 */

class RecyclerAdapter extends RecyclerView.Adapter<SimpleViewHolder>{

    public static HashMap<String, String> FX_URL = CurrencyList.FX_URL;

    private final WeakReference<LayoutInflater> localInflater;
    //private LoadService mService;//----Тоже лучше weakReference
    private CurrencyList mCurList;
    private ViewGroup mParent;

    //--Для предотвращения зацикливания из-за нотификации другого ресайклера
    private ArrayList<Double> checkRates = new ArrayList<>();
    private NotifyRecyclerChanged mNotifyRecyclerChanged;

    //-----------------------

    /*private LoadService service;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            LoadService.MyBinder b = (LoadService.MyBinder) binder;
            service = b.getService();
            Log.d(CurrencyList.TAG,"onServiceConnected: getService" );

            notifyDataSetChanged();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            service = null;
        }
    };

    LoadService.NotifyListener mNotifyListener = new LoadService.NotifyListener() {
        @Override
        public void onNotify() {
            *//*for (int i=0;i<3;i++) {
                notifyItemChanged(i);
            }*//*
            notifyDataSetChanged();
            Log.d(CurrencyList.TAG,"RecyclerAdapter: there has been DONE NOTIFY " );
        }
    };*/

    //-----------------------


    public RecyclerAdapter(LayoutInflater layoutInflater, NotifyRecyclerChanged notifyRecyclerChanged ){

        //---Для NotifyRecyclerChanged--
        mNotifyRecyclerChanged = notifyRecyclerChanged;
        for(int i = 0; i < 3; i++) checkRates.add(i, 0.0);
        //----------------
        localInflater = new WeakReference<LayoutInflater>(layoutInflater);
        /*Intent intent = new Intent(localInflater.get().getContext(), LoadService.class);
        localInflater.get().getContext().bindService(intent, serviceConnection, BIND_AUTO_CREATE);*/
        //------------------

/*        mService = service;*/
        mCurList = CurrencyList.getInstance();
    }

    /*LoadService.NotifyListener mNotifyListener = new LoadService.NotifyListener() {
        @Override
        public void onNotify() {
            notifyDataSetChanged();
        }
    };*/

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = localInflater.get();
        if(inflater != null){
            mParent = parent;

            /*if (service != null) {
                Log.d(CurrencyList.TAG,"RecyclerAdapter: there has been set NOTIFYLISTENER " );
                service.setNotifyListener(mNotifyListener);
                service.loadData();
            }*/

            return new SimpleViewHolder(inflater.inflate(R.layout.recycler_view, parent, false));
        }
        else {
            throw new  RuntimeException("Oooops, looks like activity is dead");
        }
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {

        Log.d(CurrencyList.TAG,"RecyclerAdapter: onBindViewHolder" );

        //-Problem-- Не успели скачаться все данные
        String ownCurrencyName = mCurList.getCurrencyName(position);

        if(/*service != null &&*/ ownCurrencyName != null) {

            Log.d(CurrencyList.TAG,"RecyclerAdapter: service != null " );

            String currencyTo = mCurList.getCurrencyFrom(mParent);
            double rate = mCurList.getRate(currencyTo);

            if(checkRates.get(position) != rate) {

                Log.d(CurrencyList.TAG,"RecyclerAdapter: rates changed " );

                //--Для информированности другой вью, какая мы сейчас валюта.
                //---может можно было использовать интерфейс для этой цели---
                //-Optimization-- А ещё зачем передавать ownCurrencyName туда, откуда мы его взяли? (риторический)
                //-Problem-- Вью не обновляется при смене вьюшкой валюты
                mCurList.setCurrentlyExchange(mParent, ownCurrencyName);

                holder.setCurrancyName(ownCurrencyName);
                //-Error--Ошибка потому что на данном этапе не добавлена вторая валюта в currentExchange.
                //-Solution--Может быть нужно изначально его дефолтом заполнить -- Done, но не очень (просто присволи строку)

                holder.setCurrancyRate(rate);

                checkRates.set(position, rate);
                mNotifyRecyclerChanged.onNotify();
            }



        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public interface NotifyRecyclerChanged {
        void onNotify();
    }
}
