package com.p4u.parvarish.Notification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.p4u.parvarish.R;

import java.util.Objects;

public class NotificationActivity extends AppCompatActivity {
    private EditText txttitle,txtmessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Button btnnotify=findViewById(R.id.btnnotifiaction);
        txttitle=findViewById(R.id.txttitle);
        txtmessage=findViewById(R.id.txtmessage);

        btnnotify.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                // Create an explicit intent for an Activity in your app
               notificationDialog();

            }
        });


    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void notificationDialog() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "ParvarishForYou";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);
            // Configure the notification channel.
            notificationChannel.setDescription("Message is sent by Parvarish4U android App ");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            Objects.requireNonNull(notificationManager).createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.pkclogo)
                .setTicker("ParvarishForYou")
                .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle(txttitle.getText().toString())
                .setContentText(txtmessage.getText().toString())
                .setContentInfo("Message is sent by Parvarish4U android App");
        Objects.requireNonNull(notificationManager).notify(1, notificationBuilder.build());
    }
}
