package com.example.gor.revolut_test;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Gor on 24.07.2017.
 */

class SimpleViewHolder extends RecyclerView.ViewHolder {

    private CashChangedNotify mCashChangedNotify;
    private CurrencyClickListener mCurrencyClickListener;

    private TextView currencyName;
    private TextView currencyRate;
    private EditText cashAmount;

    public SimpleViewHolder(View itemView) {
        super(itemView);

        currencyName = (TextView) itemView.findViewById(R.id.id_text_currency_name);
        currencyRate = (TextView) itemView.findViewById(R.id.id_text_exchanged_rate);
        cashAmount = (EditText) itemView.findViewById(R.id.id_edit_exchange_number);

        cashAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count > 3) cashAmount.setTextSize(30);
                else cashAmount.setTextSize(40);
            }


            @Override
            public void afterTextChanged(Editable s) {

                /*Log.d(CurrencyList.TAG,"TextChangedListener.afterTextChanged: " + s.toString() +
                "touch event: " + cashAmount.isFocused());*/
                if(cashAmount.isFocused() && !s.toString().equals("")) {
                    Double cash = Double.valueOf(s.toString());
                    if ( cash != 0){
                        // Через MainActivity ментается (проброс туда через адаптер)
                        mCashChangedNotify.onNotify(cash);
                    }
                }
            }
        });

        currencyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrencyClickListener.onClick();
            }
        });
    }

    //--Main work method-------

    public void setCurrencyName(String currencyNameString){ currencyName.setText(currencyNameString); }

    public void setCurrencyRate(double currencyRateNumber){
        currencyRate.setText("1 = * " + currencyRateNumber); }

    public void setCashAmount(Double cashAmountNumber){
        if(cashAmountNumber == 0) cashAmount.setText("");
        else cashAmount.setText(cashAmountNumber.toString()); }

    //--Addition----------

    //--Для перевода числа с одной валюты на другую в момент ввода числа---

    public interface CashChangedNotify{
        void onNotify(double cash);
    }

    public void setCashChangedNotify(CashChangedNotify cashChangedNotify){
        mCashChangedNotify = cashChangedNotify;
    }

    //--Для открытие окна со списком валют---

    public interface CurrencyClickListener {
        void onClick();
    }

    public void setCurrencyClickListener(CurrencyClickListener clickListener){
        mCurrencyClickListener = clickListener;
    }
}
