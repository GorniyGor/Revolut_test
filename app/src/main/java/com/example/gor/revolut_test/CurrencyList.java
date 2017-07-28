package com.example.gor.revolut_test;

import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Gor on 26.07.2017.
 */

//  Singleton / Data store----

public class CurrencyList {

    private static final String TAG = "myLogs" ;

    public static HashMap<String, String> FX_URL = new HashMap<>();

    public static volatile CurrencyList sSelf;
    private HashMap<ViewGroup, String> currentlyExchange = new HashMap<>();
    private String otherCurrency; //---Нужно для метода getCurrencyFrom

    TreeMap<String, CurrencyClass> exchangeRate = new TreeMap<>();
    ArrayList<String> positionOfCurrency = new ArrayList<>();
    String currentCurrencyFrom;

    public static CurrencyList getInstance(){
        if(sSelf == null){
            synchronized (CurrencyClass.class){
                if(sSelf == null){
                    sSelf = new CurrencyList();
                }
            }
        }
        return sSelf;
    }
    //------------------------------------------------------

    //--Main work methods-----------------------------------

    public void setCurrency(String nameFrom, CurrencyClass mCurrency){
        exchangeRate.put(nameFrom,mCurrency);
        positionOfCurrency.add(exchangeRate.lastKey());
    }

    public double getRate(String currencyTo){
        return exchangeRate.get(currentCurrencyFrom).getRate(currencyTo);
    }

    public String getCurrencyName(int position){
        currentCurrencyFrom = positionOfCurrency.get(position);
        return currentCurrencyFrom;
    }


    //----Для взаимодействия между вью (валютами)---------------------------------------------------

    //---Необходимо для понимания, какие именно валюты в ланный момент нужно обменивать
    //---А для этого нужно понимать, какая валюта стоит в другой вьюшке
    public void setCurrentlyExchange(ViewGroup parent, String currency){
        currentlyExchange.put(parent, currency);
    }

    //---В структуре currentlyExchange должно быть только 2 элемента,
    //---соответственно для одной вьюшки и для другой.
    //---Нам нужно взять название валюты у другой (не собственной) вьюшки
    public String getCurrencyFrom(ViewGroup ownParent){

        if(currentlyExchange.size() > 2) Log.d(TAG, "getCurrencyFrom: ViewGroup parents too many!");

        for(Map.Entry entry : currentlyExchange.entrySet()){
            if(!ownParent.equals(entry.getKey())){
                otherCurrency = (String) entry.getValue();
            }
        }
        return otherCurrency;
    }
}
