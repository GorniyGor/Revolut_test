package com.example.gor.revolut_test.Internet;

import android.util.Log;

import com.example.gor.revolut_test.CurrencyList;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Gor on 25.07.2017.
 */

public class DataLoader {
    private ExecutorService executor;
    public static HashMap<String, String> FX_URL = CurrencyList.FX_URL;
    private final DataHasBeenLoadedListener loadedListener;

    public DataLoader(DataHasBeenLoadedListener loadedListener){
        this.loadedListener = loadedListener;
        start();
    }

    private void start() {
        executor = Executors.newFixedThreadPool(1);
        FX_URL.put("GBP", "http://api.fixer.io/latest?base=GBP");
        FX_URL.put("EUR", "http://api.fixer.io/latest?base=EUR");
        FX_URL.put("USD", "http://api.fixer.io/latest?base=USD");
    }

    public void toLoadData(){
        for (final Map.Entry entry: FX_URL.entrySet()) {

            executor.submit(new Runnable() {
                @Override
                public void run() {
                    HttpRequest request = new HttpRequest((String)entry.getValue());
                    int status = request.makeRequest();

                    if (status == HttpRequest.REQUEST_OK) {
                        String jsonContent = request.getContent();
                        loadedListener.onDataLoaded((String)entry.getKey(), jsonContent);
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
