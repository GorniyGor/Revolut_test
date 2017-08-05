package com.example.gor.revolut_test;

import java.util.HashMap;

/**
 * Created by Gor on 25.07.2017.
 */

public class CurrencyClass {

    private String currencyName;
    private HashMap<String, Double> currencyRates = new HashMap<>();
    /*private double cashToExchange = 0;*/

    public CurrencyClass(String currencyName){
        this.currencyName = currencyName;
    }

    public String getName(){
        return currencyName;
    }

    public double getRate(String currency){
        return currencyRates.get(currency);
    }

    /*public double getCashToExchange() { return cashToExchange; }*/

    public void setCurrencyRate(String currencyTo, double rate){
        currencyRates.put(currencyTo, rate);
    }

    /*public void setCashToExchange(double cashToExchange){
        this.cashToExchange = cashToExchange;
    }*/

}
