package com.p4u.parvarish;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.p4u.parvarish.login.Login_emailActivity;


public class SplashActivity extends Activity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 5000;
    private static final String TAG="SplashActivity";
   // private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //making activity full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_splash);
        //init views
        ImageView imageView=findViewById(R.id.image1);
        TextView textView1=findViewById(R.id.welcomtext1);
        TextView textView2=findViewById(R.id.welcometext2);
        //setting animations
        Animation top_curve_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.top_down);
        Animation center_reveal_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.center_reveal_anim);
        imageView.startAnimation(top_curve_anim);
        textView1.startAnimation(center_reveal_anim);
        textView2.startAnimation(center_reveal_anim);
        //checking internet is present or not
        if (isNetworkStatusAvialable(getApplicationContext())) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        Thread.sleep(SPLASH_TIME_OUT);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //finally is used so that it is always executed.
                    finally {
                        Intent ActivityIndent = new Intent(SplashActivity.this, Login_emailActivity.class);
                        SplashActivity.this.startActivity(ActivityIndent);
                        SplashActivity.this.finish();
                        Log.d(TAG, "going to login activity");
                    }
                }
            });
            thread.start();
        } else {
            Toast.makeText(getApplicationContext(), "internet is not avialable", Toast.LENGTH_SHORT).show();
            Intent ActivityIndent = new Intent(getApplicationContext(), ExitActivity.class);
            startActivity(ActivityIndent);
            finish();
            Log.d(TAG,"internet not available");
        }

    }
    //method internet check
    public static boolean isNetworkStatusAvialable (Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
        {
            NetworkInfo netInfos = connectivityManager.getActiveNetworkInfo();
            if(netInfos != null)
                return netInfos.isConnected();
        }
        return false;
    }

}