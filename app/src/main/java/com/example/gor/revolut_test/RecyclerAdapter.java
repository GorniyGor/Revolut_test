package com.example.gor.revolut_test;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gor on 24.07.2017.
 */

class RecyclerAdapter extends RecyclerView.Adapter<SimpleViewHolder>{

    private final WeakReference<LayoutInflater> localInflater;
    private List<CurrencyClass> mCurrency;

    public RecyclerAdapter(LayoutInflater layoutInflater){
        //----------------
        mCurrency = new ArrayList<>();
        mCurrency.add(new CurrencyClass("GBR", 0.1845));
        mCurrency.add(new CurrencyClass("EUR", 1.3045));
        mCurrency.add(new CurrencyClass("USD", 1.1956));
        //------------------

        localInflater = new WeakReference<LayoutInflater>(layoutInflater);


    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = localInflater.get();
        if(inflater != null){
            return new SimpleViewHolder(inflater.inflate(R.layout.recycler_view, parent, false));
        }
        else {
            throw new  RuntimeException("Oooops, looks like activity is dead");
        }
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        holder.setCurrancyName(mCurrency.get(position).currencyName);
        holder.setCurrancyRate(mCurrency.get(position).currencyRate);
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
