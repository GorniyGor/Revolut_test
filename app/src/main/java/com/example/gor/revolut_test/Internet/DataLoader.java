package com.example.gor.revolut_test.Internet;

import android.util.Log;

import com.example.gor.revolut_test.CurrencyList;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Gor on 25.07.2017.
 */

public class DataLoader {
    private ExecutorService executor;
    private HashMap<String, String> OPT_FX_URL = CurrencyList.OPT_FX_URL;
    private String url = "http://api.fixer.io/latest?base=";
    private String[] curAbbreviation = {
            "GBP","EUR","USD","AUD","CAD","CHF","CYP","CZK","DKK","EEK","HKD","HUF","ISK","JPY",
            "KRW","LTL","LVL","MTL","NOK","NZD","PLN","ROL","SEK","SGD","SIT","SKK","TRL","ZAR"};
    private final DataHasBeenLoadedListener loadedListener;

    public DataLoader(DataHasBeenLoadedListener loadedListener){
        this.loadedListener = loadedListener;
        start();
    }

    private void start() {
        executor = Executors.newFixedThreadPool(1);
        for(int i=0; i<curAbbreviation.length; i++){
            OPT_FX_URL.put(curAbbreviation[i], url + curAbbreviation[i] );
        }
        CurrencyList.FX_URL = new String[]{"GBP", "EUR", "USD"};
    }

    public void toLoadData(){
        for (final String entry: CurrencyList.FX_URL) {
            //Скачивать нужно только 3 валюты

            executor.submit(new Runnable() {
                @Override
                public void run() {
                    HttpRequest request = new HttpRequest(OPT_FX_URL.get(entry));
                    int status = request.makeRequest();

                    if (status == HttpRequest.REQUEST_OK) {
                        String jsonContent = request.getContent();
                        loadedListener.onDataLoaded(entry, jsonContent);
                    }
                    else Log.d(CurrencyList.TAG,"DataLoader: HttpRequest notOk " );
                }
            });
        }
    }

    public void stop(){executor.shutdown();}

    public interface DataHasBeenLoadedListener{
        void onDataLoaded(String currencyFrom, String jsonContent);
    }
}
