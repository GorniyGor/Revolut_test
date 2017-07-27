package com.example.gor.revolut_test.Internet;

import com.example.gor.revolut_test.CurrencyClass;
import com.example.gor.revolut_test.CurrencyList;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gor on 25.07.2017.
 */

class JSONHandler {

    public static void dataFilling(String currencyFrom, String jsonString){
        JSONObject dataJsonObj = null;
        CurrencyClass newCurrency = new CurrencyClass(currencyFrom);
        CurrencyList mCurList = CurrencyList.getInstance();
        String key;


        try{
            dataJsonObj = new JSONObject(jsonString);
            JSONObject rates = dataJsonObj.getJSONObject("rates");
            for (int i = 0; i < rates.length(); i++) {
                key = rates.keys().next();
                newCurrency.setCurrencyRate(key, rates.getDouble(key));
            }
            mCurList.setCurrency(currencyFrom, newCurrency);

        }
        catch (JSONException ex){ ex.printStackTrace(); }
    }
}
