package com.p4u.parvarish.user_pannel;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.p4u.parvarish.R;
import com.p4u.parvarish.login.Login_emailActivity;

import static java.util.Objects.requireNonNull;

public class ResetPasswordActivity extends AppCompatActivity {

    private static final String TAG = ResetPasswordActivity.class.getSimpleName();

    private EditText inputEmail;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnReset,btnBack;
    private View v;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        (requireNonNull(getSupportActionBar())).hide();
        setContentView(R.layout.activity_reset_password);

        initViews();



        auth = FirebaseAuth.getInstance();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResetPasswordActivity.this, Login_emailActivity.class));
                finish();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().toLowerCase().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(ResetPasswordActivity.this, "Enter your registered email id", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d(TAG, "start progress bar");
                progressBar.setVisibility(View.VISIBLE);
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ResetPasswordActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                    // startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                                    //  finish();
                                } else {
                                    Toast.makeText(ResetPasswordActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                }

                                progressBar.setVisibility(View.GONE);
                            }
                        });
                Log.d(TAG, "Progress bar visible");
            }
        });



    }
    private void initViews(){
        inputEmail = findViewById(R.id.et_email);
        btnReset = findViewById(R.id.btn_resetpassword);
        btnBack =findViewById(R.id.btn_back);
        progressBar =findViewById(R.id.progressBar_reset);
    }


}
