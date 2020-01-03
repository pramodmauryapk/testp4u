package com.p4u.parvarish.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.p4u.parvarish.R;
import com.p4u.parvarish.WelcomeActivity;
import com.p4u.parvarish.user_pannel.ResetPasswordActivity;

import java.util.Objects;

import static java.util.Objects.requireNonNull;


public class Login_emailActivity extends AppCompatActivity {

    private ImageView top_curve;
    private static final String TAG = Login_emailActivity.class.getSimpleName();

    TextInputEditText inputEmail, inputPassword;
    TextInputLayout tf1,tf2;
    private ProgressBar progressBar;
    private static final int RC_SIGN_IN = 234;
    TextView login_title;
    ImageView logo;
    LinearLayout new_user_layout,mobile_login,skip_login;
    CardView login_card;
    GoogleSignInClient mGoogleSignInClient;
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


       /* GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //Then we will get the GoogleSignInClient object from GoogleSignIn class
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Now we will attach a click listener to the sign_in_button
        //and inside onClick() method we are calling the signIn() method that will open
        //google sign in intent
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });*/

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


        String email = Objects.requireNonNull(inputEmail.getText()).toString().toLowerCase().trim();
        final String password = Objects.requireNonNull(inputPassword.getText()).toString().trim();

        if (TextUtils.isEmpty(email))    {
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

                                Toast.makeText(Login_emailActivity.this, Login_emailActivity.this.getString(R.string.auth_failed), Toast.LENGTH_LONG).show();

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
    @Override
    protected void onStart() {
        super.onStart();

        //if the user is already signed in
        //we will close this activity
        //and take the user to profile activity
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(Login_emailActivity.this, WelcomeActivity.class));
            finish();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if the requestCode is the Google Sign In code that we defined at starting
        if (requestCode == RC_SIGN_IN) {

            //Getting the GoogleSignIn Task
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                //authenticating with firebase
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(Login_emailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        //getting the auth credential
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        //Now using firebase we are signing in the user here
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();

                            Toast.makeText(Login_emailActivity.this, "User Signed In", Toast.LENGTH_SHORT).show();
                            Login_emailActivity.this.startActivity(new Intent(Login_emailActivity.this, WelcomeActivity.class));
                            Login_emailActivity.this.finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login_emailActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }
    //this method is called on click
    private void signIn() {
        //getting the google signin intent
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();

        //starting the activity for result
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

}
