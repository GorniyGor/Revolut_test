package com.example.gor.revolut_test.Internet;

import android.util.Log;

import com.example.gor.revolut_test.CurrencyClass;
import com.example.gor.revolut_test.CurrencyList;

import org.json.JSONArray;
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


        Log.d(CurrencyList.TAG,"JSONHandler: data filling");
        try{
            Log.d(CurrencyList.TAG,"JSONHandler: try");
            dataJsonObj = new JSONObject(jsonString);
            JSONObject rates = dataJsonObj.getJSONObject("rates");
            JSONArray rateKeys = rates.names();

            for (int i = 0; i < rates.length(); i++) {
                key = (String) rateKeys.get(i);
                newCurrency.setCurrencyRate(key, rates.getDouble(key));
                Log.d(CurrencyList.TAG,"JSONHandler: " + currencyFrom + " " + key);
            }
            //--Запись в хранилище--
            mCurList.setCurrency(currencyFrom, newCurrency);
            Log.d(CurrencyList.TAG,"JSONHandler: after for");

        }
        catch (JSONException ex){ ex.printStackTrace(); }
    }
}
