package com.example.gor.revolut_test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.gor.revolut_test.Internet.LoadService;


//--В отдельном файле, т.к. бродкаст, которым пользуется AlarmManager, нужно прописывать в манифесе-
public class LoadBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(CurrencyList.TAG,"LoadBroadCastReceiver.onReceive " +
                "LLLLLLLLLLLoad data" );
        LoadService.MyBinder binder =
                (LoadService.MyBinder) peekService(context, new Intent(context, LoadService.class));

        binder.getService().loadData();
    }
}
