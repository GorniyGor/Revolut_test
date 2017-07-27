package com.example.gor.revolut_test.Internet;

import com.example.gor.revolut_test.CurrencyList;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Gor on 25.07.2017.
 */

public class DataLoader {
    private ExecutorService executor;
    public static ArrayList<String> FX_URL = CurrencyList.FX_URL;
    private final DataHasBeenLoadedListener loadedListener;

    public DataLoader(DataHasBeenLoadedListener loadedListener){
        this.loadedListener = loadedListener;
        start();
    }

    private void start() {
        executor = Executors.newFixedThreadPool(1);
    }

    public void toLoadImage(final int position){
        executor.submit(new Runnable() {
            @Override
            public void run() {
                //Мутное место с position, надо скачивать сразу всё, а не при новой позиции по одной
                HttpRequest request = new HttpRequest(FX_URL.get(position));
                int status = request.makeRequest();

                if(status == HttpRequest.REQUEST_OK){
                    String jsonContent = request.getContent();
                    loadedListener.onDataLoaded(FX_URL.get(position), jsonContent);
                }
            }
        });
    }

    public void stop(){executor.shutdown();}

    public interface DataHasBeenLoadedListener{
        void onDataLoaded(String currencyFrom, String jsonContent);
    }
}
