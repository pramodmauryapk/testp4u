package my.app.p4ulibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;



public class SplashActivity extends Activity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 5000;
    private static final String TAG="Splash Activity";
   // private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
      //  MobileAds.initialize(this, "ca-app-pub-3010715647637473/4395690831");
      //  mAdView = findViewById(R.id.adView);
      //  AdRequest adRequest = new AdRequest.Builder().build();
      //  mAdView.loadAd(adRequest);


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
                        Intent ActivityIndent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(ActivityIndent);
                        finish();
                        Log.d(TAG,"going to login activity");
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
      /*  mAdView.setAdListener(new AdListener () {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        });
*/
    }
    @SuppressWarnings("deprecation")
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