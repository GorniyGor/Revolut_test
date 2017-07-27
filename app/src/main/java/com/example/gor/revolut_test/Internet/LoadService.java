package com.example.gor.revolut_test.Internet;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Gor on 25.07.2017.
 */

public class LoadService extends Service{

    private final IBinder mBinder = new MyBinder();
    private DataLoader mDataSource;
    private DataLoader.DataHasBeenLoadedListener mLoadedListener =
            new DataLoader.DataHasBeenLoadedListener() {
                @Override
                public void onDataLoaded(String currencyFrom, String jsonContent) {
                    JSONHandler.dataFilling(currencyFrom, jsonContent);
                    //Но
                }
            };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mDataSource = new DataLoader(mLoadedListener);
        return mBinder;
    }

    public class MyBinder extends Binder {
        public LoadService getService() {
            return LoadService.this;
        }
    }
}
