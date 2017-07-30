package com.example.gor.revolut_test;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.gor.revolut_test.Internet.LoadService;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Created by Gor on 24.07.2017.
 */

class RecyclerAdapter extends RecyclerView.Adapter<SimpleViewHolder>{

    public static HashMap<String, String> FX_URL = CurrencyList.FX_URL;

    private final WeakReference<LayoutInflater> localInflater;
    private LoadService mService;//----Тоже лучше weakReference
    private CurrencyList mCurList;
    private ViewGroup mParent;

    //-----------------------

    private LoadService service;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            LoadService.MyBinder b = (LoadService.MyBinder) binder;
            service = b.getService();
            Log.d(CurrencyList.TAG,"onServiceConnected: getService" );
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            service = null;
        }
    };

    LoadService.NotifyListener mNotifyListener = new LoadService.NotifyListener() {
        @Override
        public void onNotify() {
            /*for (int i=0;i<3;i++) {
                notifyItemChanged(i);
            }*/
            notifyDataSetChanged();
            Log.d(CurrencyList.TAG,"RecyclerAdapter: there has been DONE NOTIFY " );
        }
    };

    //-----------------------


    public RecyclerAdapter(LayoutInflater layoutInflater){
        localInflater = new WeakReference<LayoutInflater>(layoutInflater);
        //----------------
        Intent intent = new Intent(localInflater.get().getContext(), LoadService.class);
        localInflater.get().getContext().bindService(intent, serviceConnection, BIND_AUTO_CREATE);
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

            if (service != null) {
                Log.d(CurrencyList.TAG,"RecyclerAdapter: there has been set NOTIFYLISTENER " );
                service.setNotifyListener(mNotifyListener);
                if(mCurList.exchangeRate.size() == 0) service.loadData();
            }

            return new SimpleViewHolder(inflater.inflate(R.layout.recycler_view, parent, false));
        }
        else {
            throw new  RuntimeException("Oooops, looks like activity is dead");
        }
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {

        Log.d(CurrencyList.TAG,"RecyclerAdapter: onBindViewHolder" );

        String ownCurrencyName = mCurList.getCurrencyName(position);

        if(service != null && ownCurrencyName != null) {

            Log.d(CurrencyList.TAG,"RecyclerAdapter: service != null " );




            //--Для информированности другой вью, какая мы сейчас валюта.
            //---может можно было использовать интерфейс для этой цели---
            //-Optimization-- А ещё зачем передавать ownCurrencyName туда, откуда мы его взяли? (риторический)
            //-Problem-- Вью не обновляется при смене вьюшкой валюты
            mCurList.setCurrentlyExchange(mParent, ownCurrencyName);

            holder.setCurrancyName(ownCurrencyName);
            //-Error--Ошибка потому что на данном этапе не добавлена вторая валюта в currentExchange.
            //-Solution--Может быть нужно изначально его дефолтом заполнить
            String currencyTo = mCurList.getCurrencyFrom(mParent);
            holder.setCurrancyRate(mCurList.getRate(currencyTo));

        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
