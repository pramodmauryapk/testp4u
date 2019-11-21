package com.p4u.parvarish.video.youtubeplayer.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Objects;

public class Utils {
    @SuppressLint("DefaultLocale")
    public static String formatTime(float sec) {
        int minutes = (int) (sec / 60);
        int seconds = (int) (sec % 60);
        return String.format("%d:%02d", minutes, seconds);
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = Objects.requireNonNull(connMgr).getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
