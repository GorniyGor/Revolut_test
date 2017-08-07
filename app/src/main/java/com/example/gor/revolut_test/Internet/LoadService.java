package com.example.gor.revolut_test.Internet;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.gor.revolut_test.CurrencyList;

/**
 * Created by Gor on 25.07.2017.
 */

public class LoadService extends Service{

    private final IBinder mBinder = new MyBinder();
    private NotifyListener mNotifyListener;

    private DataLoader mDataSource;
    private DataLoader.DataHasBeenLoadedListener mLoadedListener =
            new DataLoader.DataHasBeenLoadedListener() {
                @Override
                public void onDataLoaded(String currencyFrom, String jsonContent) {
                    Log.d(CurrencyList.TAG,"DataHasBeenLoadedListener: " + currencyFrom);
                    JSONHandler.dataFilling(currencyFrom, jsonContent);

                    mNotifyListener.onNotify();
                }
            };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(CurrencyList.TAG,"LoadService: onBind " );
        mDataSource = new DataLoader(mLoadedListener);
        return mBinder;
    }

    public void loadData(){
        mDataSource.toLoadData();
    }

    public class MyBinder extends Binder {
        public LoadService getService() {
            return LoadService.this;
        }
    }


    //--Addition-------

    public void setNotifyListener(NotifyListener mNotifyListener) {
        this.mNotifyListener = mNotifyListener;
        Log.d(CurrencyList.TAG,"LoadService.setNotifyListener: it is " + mNotifyListener.toString());
    }

    public interface NotifyListener{
        void onNotify();
    }
}
