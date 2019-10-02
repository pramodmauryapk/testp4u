package com.p4u.parvarish.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import com.p4u.parvarish.R;

import static java.util.Objects.requireNonNull;

public class Login_mobile extends AppCompatActivity {
    private static final String TAG = "Login_mobile";
    EditText mobile_number;
    TextInputLayout inputLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_mobile);
        (requireNonNull(getSupportActionBar())).hide();
        mobile_number = findViewById(R.id.login_mobile_number);
        inputLayout = findViewById(R.id.mobile_input_layout);
    }
    public void proceed(View view) {

        final String mobile = mobile_number.getText().toString();

        if (TextUtils.isEmpty(mobile)){
            inputLayout.setError("Enter Mobile Number");
            return;
        }
        if (!TextUtils.isDigitsOnly(mobile)){
            inputLayout.setError("Invalid Mobile Number");
            return;
        }
        if (mobile.length()<10 || mobile.contains(" ") || mobile.contains("+")){
            inputLayout.setError("Please Enter 10 digit Mobile Number");
            return;
        }
        mobile_number.setText("");
        Intent intent = new Intent(Login_mobile.this,OTPConfirmation.class);
        intent.putExtra("mobile",mobile);
        startActivity(intent);
    }
}
