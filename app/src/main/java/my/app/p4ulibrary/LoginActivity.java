package my.app.p4ulibrary;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import my.app.p4ulibrary.main_menu.MainActivity;
import my.app.p4ulibrary.user_cornor.CreateuserActivity;
import my.app.p4ulibrary.user_cornor.ResetPasswordActivity;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText inputEmail, inputPassword;
    private Button btnSignin, btnForgotpassword,btnRegister,btnskip;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        Objects.requireNonNull(getSupportActionBar()).hide();
        FirebaseApp.initializeApp(this);
       if(auth.getCurrentUser() != null){
            Log.d(TAG,"User going to view role");
            startActivity(new Intent(LoginActivity.this, ViewRoleActivity.class));
            Log.d(TAG,"User already logged in, going to role activity now");
            finish();
        }

        // set the view now
        setContentView(R.layout.activity_login);

        initialise();
        btnForgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
                Log.d(TAG,"ResetPasswordActivity called successfully");
                finish();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, CreateuserActivity.class));
                Log.d(TAG,"Register Activity called successfully");
                finish();
            }
        });
        btnskip.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ViewRoleActivity.class));
                finish();

            }
        });

           // Sign into app
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmail.getText().toString().toLowerCase().trim();
                final String password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email))
                {
                    Toast.makeText(getApplication(), "Enter email address !", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplication(), "Enter password !", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // Authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                   progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        inputPassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    startActivity(new Intent(LoginActivity.this, ViewRoleActivity.class));
                                    Log.d(TAG,"User authentication done, going to role activity");
                                    finish();
                                }
                            }
                        });
            }
        });

    }


    private void initialise() {
        inputEmail = (EditText) findViewById(R.id.et_email);
        inputPassword = (EditText) findViewById(R.id.et_password);
        btnSignin = (Button) findViewById(R.id.btn_signin);
        btnForgotpassword = (Button) findViewById(R.id.btn_forgotpassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnRegister=(Button)findViewById(R.id.btn_register);
        btnskip=(Button)findViewById (R.id.btn_skip);
    }


}
