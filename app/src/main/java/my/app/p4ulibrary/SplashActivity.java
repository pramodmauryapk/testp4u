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
    private static int SPLASH_TIME_OUT = 3000;
    private static final String TAG="Splash Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

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