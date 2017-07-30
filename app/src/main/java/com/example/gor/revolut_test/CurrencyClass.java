package com.example.gor.revolut_test;

import java.util.HashMap;

/**
 * Created by Gor on 25.07.2017.
 */

public class CurrencyClass {

    private String currencyName;
    private HashMap<String, Double> currencyRates = new HashMap<>();

    public CurrencyClass(String currencyName){
        this.currencyName = currencyName;
    }

    public String getName(){
        return currencyName;
    }

    public double getRate(String currency){
        return currencyRates.get(currency);
    }

    public void setCurrencyRate(String currencyTo, double rate){
        currencyRates.put(currencyTo, rate);
    }

}
