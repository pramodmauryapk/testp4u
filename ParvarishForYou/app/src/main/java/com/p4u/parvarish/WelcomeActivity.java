package com.p4u.parvarish;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.p4u.parvarish.splash_activity.AnimatedCircleLoadingView;
import com.p4u.parvarish.menu_items.MainActivity;
import com.p4u.parvarish.user_pannel.Teacher;

import static java.util.Objects.requireNonNull;

public class WelcomeActivity extends AppCompatActivity {
    private static final String TAG = "WelcomeActivity";
    private AnimatedCircleLoadingView animatedCircleLoadingView;
    FirebaseUser user;
    String user_name,user_email,user_roll,user_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView (R.layout.activity_welcome);
        (requireNonNull (getSupportActionBar ())).hide ();
        animatedCircleLoadingView = findViewById(R.id.circle_loading_view);
        user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference myref = FirebaseDatabase.getInstance().getReference().child("USERS");

        startLoading();
        startPercentMockThread();
        if(user!=null){

                myref.addValueEventListener(new ValueEventListener() {

                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d(TAG, "Accessing database");
                        getting_role(dataSnapshot);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(TAG, "failed to read values", databaseError.toException());
                    }
                });
            }else{
            user_name="USER";
            user_email="EMAIL";
            user_roll="USER";
            user_img=null;
            Intent i;
            i=new Intent(this, MainActivity.class);
            i.putExtra("user_role", user_roll);
            i.putExtra("user_name", user_name);
            i.putExtra("user_email", user_email);
            i.putExtra("user_img",user_img);
            startActivity(i);
            finish();
        }


    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void getting_role(DataSnapshot dataSnapshot) {
        try {
            for (DataSnapshot ds : dataSnapshot.getChildren()) {
               Teacher uInfo=ds.getValue(Teacher.class);
               if(requireNonNull(uInfo).getUserId().equals(user.getUid())) {
                   user_name = requireNonNull(uInfo).getUserName();
                   user_email = uInfo.getUserEmail();
                   user_roll = uInfo.getUserRole();
                   user_img = uInfo.getImageURL();


               }

            }

        }
        catch (Exception e){

            Log.d(TAG, "Exception=", e);
            user_name="USER";
            user_email="EMAIL";
            user_roll="USER";
            user_img=null;


        }
        finally {
            Intent i;
            i=new Intent(WelcomeActivity.this, MainActivity.class);
            i.putExtra("user_role", user_roll);
            i.putExtra("user_name", user_name);
            i.putExtra("user_email", user_email);
            i.putExtra("user_img",user_img);
            startActivity(i);
            finish();
        }

    }
    private void startLoading() {
        animatedCircleLoadingView.startDeterminate();
    }

    private void startPercentMockThread() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                    for (int i = 0; i <= 100; i++) {
                        Thread.sleep(50);
                        WelcomeActivity.this.changePercent(i);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    WelcomeActivity.this.resetLoading();
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
