package com.p4u.parvarish.Notification;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.p4u.parvarish.R;

public class PushActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push);/*
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(
                    SyncStateContract.Constants.CHANNEL_ID, SyncStateContract.Constants.CHANNEL_NAME, importance);
            mChannel.setDescription(SyncStateContract.Constants.CHANNEL_DESCRIPTION);
            mChannel.enableLights(true); mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true); mChannel.setVibrationPattern
                    (new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
        }*/
    }
}
