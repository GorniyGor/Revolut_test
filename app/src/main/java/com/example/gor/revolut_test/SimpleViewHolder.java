package com.example.gor.revolut_test;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Gor on 24.07.2017.
 */

class SimpleViewHolder extends RecyclerView.ViewHolder {

    private CashChangedNotify mCashChangedNotify;

    private TextView currancyName;
    private TextView currancyRate;
    private EditText cashAmount;

    public SimpleViewHolder(View itemView) {
        super(itemView);

        currancyName = (TextView) itemView.findViewById(R.id.id_text_currency_name);
        currancyRate = (TextView) itemView.findViewById(R.id.id_text_exchanged_rate);
        cashAmount = (EditText) itemView.findViewById(R.id.id_edit_exchange_number);

        cashAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}


            @Override
            public void afterTextChanged(Editable s) {

                Log.d(CurrencyList.TAG,"TextChangedListener.afterTextChanged: " + s.toString() +
                "touch event: " + cashAmount.didTouchFocusSelect());
                if(cashAmount.didTouchFocusSelect() && !s.toString().equals("")) {
                    Double cash = Double.valueOf(s.toString());
                    if ( cash != 0){
                        // Через MainActivity ментается (проброс туда через адаптер)
                        mCashChangedNotify.onNotify(cash);
                    }
                }
            }
        });
    }

    //--Main work method-------

    public void setCurrancyName(String currancyNameString){ currancyName.setText(currancyNameString); }

    public void setCurrancyRate(double currancyRateNumber){
        currancyRate.setText("1 = * " + currancyRateNumber); }

    public void setCashAmount(Double cashAmountNumber){
        if(cashAmountNumber == 0) cashAmount.setText("");
        else cashAmount.setText(cashAmountNumber.toString()); }

    //--Addition----------
    //--Для перевода введенного числа из одной валюты в другую---

    public void setCashChangedNotify(CashChangedNotify cashChangedNotify){
        mCashChangedNotify = cashChangedNotify;
    }

    public interface CashChangedNotify{
        void onNotify(double cash);
    }
}
