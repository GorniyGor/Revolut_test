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

    /*double currancyRateNamber;*/

    public SimpleViewHolder(View itemView) {
        super(itemView);

        currancyName = (TextView) itemView.findViewById(R.id.id_text_currency_name);
        currancyRate = (TextView) itemView.findViewById(R.id.id_text_exchanged_rate);
        cashAmount = (EditText) itemView.findViewById(R.id.id_edit_exchange_number);

        cashAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                /*Log.d(CurrencyList.TAG,"TextChangedListener.beforeTextChanged: " + s.toString());*/
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                /*Log.d(CurrencyList.TAG,"TextChangedListener.onTextChanged: " + s.toString());*/
            }

            @Override
            public void afterTextChanged(Editable s) {

                Log.d(CurrencyList.TAG,"TextChangedListener.afterTextChanged: " + s.toString() +
                "touch event: " + cashAmount.didTouchFocusSelect());
                if(cashAmount.didTouchFocusSelect() && !s.toString().equals("")) {
                    Double cash = Double.valueOf(s.toString());
                    if ( cash != 0){
                        //CurrencyList.getInstance().setCashToExchange(cash);
                        // Через MainActivity ментается (проброс туда через адаптер)
                        mCashChangedNotify.onNotify(cash);
                    }
                }
                //--Нужно добавить обновления другого ресайклера, но
                // нужно учесть, что этот метод вызывается ещё и когда программно меняется текст  - учли
                // +
                // Не сразу выставлять значение эдитора, чтобы не нужно было удалять 0
                //-- Слишком длинные числа, сделать максимум 8 цифр - сделали, но
                // +
                // ограничить получаемое при переводе число 9тью цифрами

            }
        });
    }

    //--Main work method----

    public void setCurrancyName(String currancyNameString){ currancyName.setText(currancyNameString); }
    public void setCurrancyRate(double currancyRateNumber){
        /*this.currancyRateNamber = currancyRateNumber;*/
        currancyRate.setText("1 = * " + currancyRateNumber); }
    //--Нужен литенер editText
    public void setCashAmount(Double cashAmountNumber){
        if(cashAmountNumber == 0) cashAmount.setText("");
        else cashAmount.setText(cashAmountNumber.toString()); }

    //--Addition----------
    //--Для перевода числа с одной валюты на другую---

    public void setCashChangedNotify(CashChangedNotify cashChangedNotify){
        mCashChangedNotify = cashChangedNotify;
    }

    public interface CashChangedNotify{
        void onNotify(double cash);
    }
}
