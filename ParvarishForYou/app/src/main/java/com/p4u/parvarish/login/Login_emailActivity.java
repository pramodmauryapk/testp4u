package com.p4u.parvarish.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import com.p4u.parvarish.R;
import com.p4u.parvarish.WelcomeActivity;
import com.p4u.parvarish.user_pannel.ResetPasswordActivity;


import static java.util.Objects.requireNonNull;


public class Login_emailActivity extends AppCompatActivity {

    ImageView top_curve;
   private static final String TAG = "Login_emailActivity";
    TextInputEditText inputEmail, inputPassword;
    TextInputLayout tf1,tf2;
    private ProgressBar progressBar;
    TextView login_title;
    ImageView logo;
    LinearLayout new_user_layout,mobile_login,skip_login;
    CardView login_card;

    FirebaseAuth auth;
    DatabaseReference myref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        (requireNonNull(getSupportActionBar())).hide();
        setContentView(R.layout.activity_login_email);
        init();
        auth = FirebaseAuth.getInstance();
        myref = FirebaseDatabase.getInstance().getReference().child("USERS");

        Animation top_curve_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.top_down);
        top_curve.startAnimation(top_curve_anim);

        Animation field_name_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.field_name_anim);
        login_title.startAnimation(field_name_anim);

       Animation center_reveal_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.center_reveal_anim);
        login_card.startAnimation(center_reveal_anim);

        Animation new_user_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.down_top);
        mobile_login.startAnimation(new_user_anim);
        skip_login.startAnimation(new_user_anim);
        new_user_layout.startAnimation(new_user_anim);

        if(auth.getCurrentUser()!=null){
           // Toast.makeText(Login_emailActivity.this, auth.getUid(), Toast.LENGTH_LONG).show();

            startActivity(new Intent(Login_emailActivity.this, WelcomeActivity.class));
            finish();
        }


    }


    private void init(){
        top_curve = findViewById(R.id.top_curve);
        inputEmail = findViewById(R.id.et_email);
        inputPassword =findViewById(R.id.et_password);
        tf1=findViewById(R.id.tf1);
        tf2=findViewById(R.id.tf2);
        progressBar =findViewById(R.id.progressBar);
        logo = findViewById(R.id.logo);
        login_title = findViewById(R.id.login_text);
        mobile_login=findViewById(R.id.mobile_login);
        skip_login=findViewById(R.id.skip_login);
        new_user_layout = findViewById(R.id.new_user_text);
        login_card = findViewById(R.id.login_card);
    }

    public void register(View view) {
        startActivity(new Intent(this, UserRegistrationActivity.class));
    }

    public void loginButton(View view) {
        //Toast.makeText(this,"Login_mobileActivity Clicked",Toast.LENGTH_SHORT).show();

        String email = Objects.requireNonNull(inputEmail.getText()).toString().toLowerCase().trim();
        final String password = Objects.requireNonNull(inputPassword.getText()).toString().trim();

        if (TextUtils.isEmpty(email))
        {
            tf1.setError("Enter email address !");


            return;
        }

        if (TextUtils.isEmpty(password)) {
            tf2.setError("Enter password !");

            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // Authenticate user
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(Objects.requireNonNull(this), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {
                            // there was an error
                            if (password.length() < 6) {
                                tf2.setError(Login_emailActivity.this.getString(R.string.minimum_password));
                            } else {
                                Toast.makeText(Login_emailActivity.this, Login_emailActivity.this.getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                            }
                        } else {

                            Login_emailActivity.this.startActivity(new Intent(Login_emailActivity.this, WelcomeActivity.class));
                            Login_emailActivity.this.finish();

                        }
                    }
                });
    }

    public void skiplogin(View view) {
        startActivity(new Intent(Login_emailActivity.this, WelcomeActivity.class));
        finish();
    }

    public void login_with_mobile(View view) {
        startActivity(new Intent(Login_emailActivity.this, Login_mobileActivity.class));
        finish();
    }

    public void forgot(View view) {
        startActivity(new Intent(Login_emailActivity.this, ResetPasswordActivity.class));
        finish();
    }
}
