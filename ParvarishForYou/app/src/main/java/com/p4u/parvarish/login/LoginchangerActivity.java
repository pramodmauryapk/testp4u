package com.p4u.parvarish.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.p4u.parvarish.R;
import com.p4u.parvarish.WelcomeActivity;
import com.p4u.parvarish.user_pannel.Teacher;

import static java.util.Objects.requireNonNull;

public class LoginchangerActivity extends AppCompatActivity {

    private static final String TAG = LoginchangerActivity.class.getSimpleName();
    private Button btnemail,btnmobile,btnskip;
    private SignInButton btngoogle;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 234;
    private FirebaseAuth auth;
    private DatabaseReference myref;
    private Boolean ans=false;
    public LoginchangerActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        (requireNonNull(getSupportActionBar())).hide();
        setContentView(R.layout.loginchanger);
        myref = FirebaseDatabase.getInstance().getReference().child("USERS");
        init();
       GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //Then we will get the GoogleSignInClient object from GoogleSignIn class
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Now we will attach a click listener to the sign_in_button
        //and inside onClick() method we are calling the signIn() method that will open
        //google sign in intent
        findViewById(R.id.login_gmail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        btnmobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginchangerActivity.this, Login_mobileActivity.class));
                finish();
            }
        });
        btnskip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginchangerActivity.this, WelcomeActivity.class));
                finish();
            }
        });
        btnemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginchangerActivity.this, Login_emailActivity.class));
                finish();
            }
        });
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
                assert account != null;
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(LoginchangerActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void init() {
        btngoogle=findViewById(R.id.login_gmail);
        btnemail=findViewById(R.id.login_email);
        btnmobile=findViewById(R.id.login_mobile);
        btnskip=findViewById(R.id.skip_login);

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
                            final FirebaseUser user = auth.getCurrentUser();

                            myref.addValueEventListener(new ValueEventListener() {

                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Log.d(TAG, "Accessing database");
                                    try {
                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                            Teacher uInfo=ds.getValue(Teacher.class);
                                            assert user != null;
                                            if(requireNonNull(uInfo).getUserId().equals(user.getUid())) {
                                               ans=true;

                                            }

                                        }

                                    }
                                    catch (Exception e){

                                        Log.d(TAG, "Exception=", e);
                                        ans=false;
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.d(TAG, "failed to read values", databaseError.toException());
                                    ans=false;
                                }
                            });

                            if(ans){

                                LoginchangerActivity.this.startActivity(new Intent(LoginchangerActivity.this, WelcomeActivity.class));
                                LoginchangerActivity.this.finish();
                            }else {

                                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(LoginchangerActivity.this);
                                if (acct != null) {
                                    String personName = acct.getDisplayName();
                                    String personGivenName = acct.getGivenName();
                                    String personFamilyName = acct.getFamilyName();
                                    String personEmail = acct.getEmail();
                                    String personId = acct.getId();
                                    Uri personPhoto = acct.getPhotoUrl();

                                    Intent i;
                                    i = new Intent(LoginchangerActivity.this, UserRegistrationActivity.class);
                                    i.putExtra("user_name", personName);
                                    i.putExtra("user_email", personEmail);
                                    i.putExtra("user_img", personPhoto);

                                    startActivity(i);
                                    finish();
                                }

                            }
                            Toast.makeText(LoginchangerActivity.this, "User Signed In", Toast.LENGTH_SHORT).show();








                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginchangerActivity.this, "Authentication failed.",
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
    @Override
    protected void onStart() {
        super.onStart();

        //if the user is already signed in
        //we will close this activity
        //and take the user to profile activity
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginchangerActivity.this, WelcomeActivity.class));
            finish();
        }
    }
}