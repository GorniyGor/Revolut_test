package com.example.gor.revolut_test;

import android.support.v7.widget.RecyclerView;
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
    private String name;

    /*//--Для предотвращения зацикливания из-за нотификации другого ресайклера
    private ArrayList<Double> checkRates = new ArrayList<>();
    private NotifyRecyclerChanged mNotifyRecyclerChanged;*/

    //-----------------------

    //-----------------------


    public RecyclerAdapter(LayoutInflater layoutInflater, String name /*NotifyRecyclerChanged notifyRecyclerChanged*/ ){

        /*//---Для NotifyRecyclerChanged--
        mNotifyRecyclerChanged = notifyRecyclerChanged;
        for(int i = 0; i < 3; i++) checkRates.add(i, 0.0);*/
        //----------------

        localInflater = new WeakReference<LayoutInflater>(layoutInflater);
        mCurList = CurrencyList.getInstance();
        this.name = name;
    }

    //--Мне нужен экземпляр ресайклервью для идентифицирования вью, которая работает,
    //--чтобы пользоваться массивом CurrencyList.currentlyExchange
   /* @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);

        name = recyclerView;
    }*/

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = localInflater.get();
        if(inflater != null){
            /*mParent = parent;*/
            /*name = (RecyclerView) parent;*/

            return new SimpleViewHolder(inflater.inflate(R.layout.recycler_view, parent, false));
        }
        else {
            throw new  RuntimeException("Oooops, looks like activity is dead");
        }
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {

        //-Problem-- Не успели скачаться все данные
        String ownCurrencyName = mCurList.getCurrencyName(position);
        double rate = 0.0;

        if(/*service != null &&*/ ownCurrencyName != null) {

            String currencyTo = mCurList.getCurrencyFrom(name);

            //--Из-за race condition адаптера и окончания закачки--
            if(currencyTo!=null) rate = mCurList.getRate(currencyTo);

        }
        else ownCurrencyName = "non";

        holder.setCurrancyName(ownCurrencyName);
        holder.setCurrancyRate(rate);
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    /*public interface NotifyRecyclerChanged {
        void onNotify();
    }*/
}
