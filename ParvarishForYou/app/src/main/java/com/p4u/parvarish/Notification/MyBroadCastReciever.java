package com.p4u.parvarish.Notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyBroadCastReciever extends BroadcastReceiver {
    private static final String TAG = MyBroadCastReciever.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            Log.i("Check","Screen went ON");
            //Toast.makeText(context, "screen ON", Toast.LENGTH_LONG).show();

            // Here you can write the logic of send SMS, Email, Make a call

        }
    }
}