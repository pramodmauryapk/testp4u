package my.app.p4ulibrary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import my.app.p4ulibrary.Progress.AnimatedCircleLoadingView;
import my.app.p4ulibrary.main_menu.MainActivity;

import static java.util.Objects.requireNonNull;

public class WelcomeActivity extends AppCompatActivity {
    private AnimatedCircleLoadingView animatedCircleLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);

        setContentView (R.layout.activity_view_role);
        (requireNonNull (getSupportActionBar ())).hide ();
        animatedCircleLoadingView = (AnimatedCircleLoadingView) findViewById(R.id.circle_loading_view);
        startLoading();
        startPercentMockThread();


    }
    private void startLoading() {
        animatedCircleLoadingView.startDeterminate();
    }

    private void startPercentMockThread() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                    for (int i = 0; i <= 100; i++) {
                        Thread.sleep(65);
                        changePercent(i);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    Intent i;
                    i=new Intent(WelcomeActivity.this, MainActivity.class);
                    i.putExtra("user_role", "USER");
                    i.putExtra("user_name", "USER");
                    i.putExtra("user_email", " ");
                    startActivity(i);
                    finish();
                }
            }
        };
        new Thread(runnable).start();
    }

    private void changePercent(final int percent) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                animatedCircleLoadingView.setPercent(percent);
            }
        });
    }

    public void resetLoading() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                animatedCircleLoadingView.resetLoading();
            }
        });
    }

}
