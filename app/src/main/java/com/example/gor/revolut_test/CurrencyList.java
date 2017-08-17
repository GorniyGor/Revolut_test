package com.example.gor.revolut_test;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Gor on 26.07.2017.
 */

//  Singleton / Data store----

public class CurrencyList {

    public static final String TAG = "myLogs" ;

    //--Выставляется в DataLoader.class
    public static HashMap<String, String> FX_URL = new HashMap<>();
    //--Выставляется в MainActivity.class
    public static final HashMap<String, RecyclerView> RV_NAMES = new HashMap<>();
    public static volatile CurrencyList sSelf;

    private Date lastUpdateDate = null;

    private HashMap<RecyclerView, Integer>  currentlyExchange = new HashMap<>();
    private String otherCurrency = null;

    private HashMap<String, CurrencyClass> exchangeRate = new HashMap<>();
    //--optimization--вместо него можно FX_URL ArrayList
    private ArrayList<String> positionOfCurrency = new ArrayList<>();
    private String currentCurrencyFrom = null;

    public Cash mCash = new Cash();//--Количество денег, которое хотят перевести в другую валюту


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
        positionOfCurrency.add(nameFrom);
    }

    public void setDate(Date date){
        lastUpdateDate = date;
    }

    public double getRate(String currencyFrom, String currencyTo){
        if(!Objects.equals(currencyTo, currencyFrom)) {
            return exchangeRate.get(currencyFrom).getRate(currencyTo);
        }
        else return 1;
    }

    public String getCurrencyName(int position){

        if(positionOfCurrency.size() > position) {
            currentCurrencyFrom = positionOfCurrency.get(position);
            return currentCurrencyFrom;
        }
        return null;
    }

    public Date getLastUpdateDate(){
        return lastUpdateDate;
    }


    //----Для взаимодействия между вью (валютами)---------------------------------------------------

    //--Необходимо для понимания, какие именно валюты в данный момент нужно обменивать---
    // А для этого нужно понимать, какая валюта стоит в другой вьюшке
    public void changeCurrentlyExchange(RecyclerView name, Integer mod){
        int currency = currentlyExchange.get(name);
        if(mod < 0 && currency == 0) return;
        if(mod > 0 && currency == 2) return;
        currentlyExchange.put(name, currency + mod);
    }

    public void setCurrentlyExchange(RecyclerView name, Integer currency){
        currentlyExchange.put(name, currency);
    }

    public int getCurrentlyExchangeSize(){return currentlyExchange.size();}

    // В структуре currentlyExchange должно быть только 2 элемента,
    // соответственно для одной вьюшки и для другой.
    // Нам нужно взять название валюты у другой (несобственной) вьюшки
    public String getCurrencyFrom(String  name){

        ////--optimization--можно просто по name поиск сделать, переопределив currentlyExchange
        RecyclerView currency = RV_NAMES.get(name);

        if(currentlyExchange.size() > 2) Log.d(TAG, "getCurrencyFrom: ViewGroup parents too many!");

        for(Map.Entry entry : currentlyExchange.entrySet()){
            if(!currency.equals(entry.getKey())){
                otherCurrency = positionOfCurrency.get((Integer) entry.getValue());
            }
        }
        return otherCurrency;
    }
    //----------------------------------------------------------------------------------------------

    //--Addition-------


    public class Cash{
        private double cashToExchange = 0; //--Sum that had been entered in EditView
        private String changerId = ""; //--RecyclerView which had been done it
        private String versionChangerId = ""; //--Currency which had been done it

        public void set(String id, String vId, double sum){
            changerId = id;
            versionChangerId = vId;
            cashToExchange = sum;

            Log.d(CurrencyList.TAG,"CurrencyList: changerId: " + id +
                    " versionChangerId: " + vId + " cashToExchange: " + sum );
        }

        public String getChanger(){ return changerId; }
        public String getVersionOfChanger() { return versionChangerId; }
        public double getCash(){ return cashToExchange; }
    }
}
