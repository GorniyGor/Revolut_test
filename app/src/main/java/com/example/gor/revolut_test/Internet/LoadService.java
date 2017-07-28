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
    private NotifyListener mNotifyListener;

    private DataLoader mDataSource;
    private DataLoader.DataHasBeenLoadedListener mLoadedListener =
            new DataLoader.DataHasBeenLoadedListener() {
                @Override
                public void onDataLoaded(String currencyFrom, String jsonContent) {
                    JSONHandler.dataFilling(currencyFrom, jsonContent);

                    mNotifyListener.onNotify();
                }
            };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mDataSource = new DataLoader(mLoadedListener);
        mDataSource.toLoadData();
        return mBinder;
    }

    public void setNotifyListener(NotifyListener mNotifyListener) {
        this.mNotifyListener = mNotifyListener;
    }

    public class MyBinder extends Binder {
        public LoadService getService() {
            return LoadService.this;
        }
    }

    public interface NotifyListener{
        void onNotify();
    }
}
