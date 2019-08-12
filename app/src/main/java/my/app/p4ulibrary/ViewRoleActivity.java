package my.app.p4ulibrary;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

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

import my.app.p4ulibrary.Progress.AnimatedCircleLoadingView;
import my.app.p4ulibrary.classes.User;
import my.app.p4ulibrary.main_menu.MainActivity;
import my.app.p4ulibrary.user_cornor.CreateuserActivity;

import static java.util.Objects.requireNonNull;

public class ViewRoleActivity extends AppCompatActivity {
    private static final String TAG = "ViewRoleActivity";
    private AnimatedCircleLoadingView animatedCircleLoadingView;
    public static int lp = 0;
    private String userID;
    private Intent intent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_role);
        //Hiding actionbar for this activity
        (requireNonNull(getSupportActionBar())).hide();
   //     FirebaseApp.initializeApp(this);
        animatedCircleLoadingView = (AnimatedCircleLoadingView) findViewById(R.id.circle_loading_view);
        startLoading();
        startPercentMockThread();
            //Access real time database
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        //Declare database reference
        DatabaseReference myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null){

            userID = (requireNonNull(user)).getUid();
            myRef.addValueEventListener(new ValueEventListener() {

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

        }
        else
        {
            startActivity(new Intent(ViewRoleActivity.this, WelcomeActivity.class));
            finish();

        }



    }


    //Showing data from firebase database


    private void getting_role(@NonNull DataSnapshot dataSnapshot) {
        try {
            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                User uInfo = new User();

                uInfo.setUserRole(((requireNonNull(ds.child(userID).getValue(User.class)))).getUserRole()); //set the role
                uInfo.setUserName(((requireNonNull(ds.child(userID).getValue(User.class)))).getUserName()); //set the role
                uInfo.setUserEmail(((requireNonNull(ds.child(userID).getValue(User.class)))).getUserEmail()); //set the role
                Intent i;
                i = new Intent (ViewRoleActivity.this, MainActivity.class);
                i.putExtra("user_role", uInfo.getUserRole());
                i.putExtra("user_name", uInfo.getUserName());
                i.putExtra("user_email", uInfo.getUserEmail());
                startActivity(i);
                Log.d(TAG, "Accessed role from user database going to Super User activity");


            }
        }
        catch (Exception e){
            Log.d(TAG, "EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE"+e);


            }finally {
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
                    Thread.sleep(1500);
                    for (int i = 0; i <= 100; i++) {
                        Thread.sleep(65);
                        changePercent(i);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
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
