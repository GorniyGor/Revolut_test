package com.example.gor.revolut_test;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Created by Gor on 24.07.2017.
 */

class RecyclerAdapter extends RecyclerView.Adapter<SimpleViewHolder>{

    public static HashMap<String, String> FX_URL = CurrencyList.FX_URL;

    private final WeakReference<LayoutInflater> localInflater;
    private CurrencyList mCurList;
    private RecyclerView name;

    /*//--Для предотвращения зацикливания из-за нотификации другого ресайклера
    private ArrayList<Double> checkRates = new ArrayList<>();
    private NotifyRecyclerChanged mNotifyRecyclerChanged;*/

    //-----------------------

    //-----------------------


    public RecyclerAdapter(LayoutInflater layoutInflater/*, String name*/ /*NotifyRecyclerChanged notifyRecyclerChanged*/ ){

        /*//---Для NotifyRecyclerChanged--
        mNotifyRecyclerChanged = notifyRecyclerChanged;
        for(int i = 0; i < 3; i++) checkRates.add(i, 0.0);*/
        //----------------

        localInflater = new WeakReference<LayoutInflater>(layoutInflater);
        mCurList = CurrencyList.getInstance();
        /*this.name = name;*/
    }

    //--Мне нужен экземпляр ресайклервью для идентифицирования вью, которая работает,
    //--чтобы пользоваться массивом CurrencyList.currentlyExchange
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);

        name = recyclerView;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = localInflater.get();
        if(inflater != null){
            /*mParent = parent;*/

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

            String currencyTo = mCurList.getCurrencyFrom(name);
            double rate = mCurList.getRate(currencyTo);

                Log.d(CurrencyList.TAG,"RecyclerAdapter: rates changed " );

                //--Для информированности другой вью, какая мы сейчас валюта.
                //---может можно было использовать интерфейс для этой цели---
                //-Optimization-- А ещё зачем передавать ownCurrencyName туда, откуда мы его взяли? (риторический)
                //-Problem-- Вью не обновляется при смене вьюшкой валюты
                /*mCurList.setCurrentlyExchange(mParent, ownCurrencyName);*/

                holder.setCurrancyName(ownCurrencyName);
                //-Error--Ошибка потому что на данном этапе не добавлена вторая валюта в currentExchange.
                //-Solution--Может быть нужно изначально его дефолтом заполнить -- Done, но не очень (просто присволи строку)

                holder.setCurrancyRate(rate);

                /*checkRates.set(position, rate);
                mNotifyRecyclerChanged.onNotify();*/



        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    /*public interface NotifyRecyclerChanged {
        void onNotify();
    }*/
}
