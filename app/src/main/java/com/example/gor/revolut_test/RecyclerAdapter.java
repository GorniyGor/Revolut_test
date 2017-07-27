package com.example.gor.revolut_test;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Gor on 24.07.2017.
 */

class RecyclerAdapter extends RecyclerView.Adapter<SimpleViewHolder>{

    public static ArrayList<String> FX_URL = CurrencyList.FX_URL;

    private final WeakReference<LayoutInflater> localInflater;
    private CurrencyList mCurList;
    private ViewGroup mParent;

    public RecyclerAdapter(LayoutInflater layoutInflater){
        //----------------
        FX_URL.add("http://api.fixer.io/latest?base=GBP");
        FX_URL.add("http://api.fixer.io/latest?base=EUR");
        FX_URL.add("http://api.fixer.io/latest?base=USD");
        //------------------

        localInflater = new WeakReference<LayoutInflater>(layoutInflater);
        mCurList = CurrencyList.getInstance();
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = localInflater.get();
        if(inflater != null){
            mParent = parent;
            //В синглтон нужно вставить родителя--- тут----
            return new SimpleViewHolder(inflater.inflate(R.layout.recycler_view, parent, false));
        }
        else {
            throw new  RuntimeException("Oooops, looks like activity is dead");
        }
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        String ownCurrencyName = mCurList.getCurrencyName(position);

        //--Для информированности другой вью, какая мы сейчас валюта.
        //---может можно было использовать интерфейс для этой цели---
        mCurList.setCurrentlyExchange(mParent, ownCurrencyName );

        holder.setCurrancyName(ownCurrencyName);
        String currencyTo = mCurList.getCurrencyFrom(mParent);
        holder.setCurrancyRate(mCurList.getRate(currencyTo));
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
