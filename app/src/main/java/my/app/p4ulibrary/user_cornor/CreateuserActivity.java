package my.app.p4ulibrary.user_cornor;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import my.app.p4ulibrary.LoginActivity;
import my.app.p4ulibrary.R;
import my.app.p4ulibrary.classes.User;

import static androidx.constraintlayout.solver.widgets.ConstraintWidget.GONE;

public class CreateuserActivity extends AppCompatActivity {
    private static final String TAG = "CreateuserActivity";
    private EditText etFullname, etPassword,etaPassword, etEmail, etMobile,etAddress,etIdentity;
    private FirebaseAuth auth;
    private Spinner spRole;
    private ProgressBar progressBar;
    private TextView tvrole;
    private DatabaseReference myref;
    private Button btnCreatenewuser;
    private String email,password,aPassword,id,name,role,mobilenumber,address,identity,status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_user);
        initViews();
        tvrole.setVisibility (View.GONE);
        spRole.setVisibility (View.GONE);
        //get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        btnCreatenewuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
        myref = FirebaseDatabase.getInstance().getReference("Users");


    }
    private void initViews(){
        etFullname = (EditText) findViewById(R.id.et_fullname);
        etPassword = (EditText) findViewById(R.id.et_createpassword);
        etaPassword= (EditText) findViewById(R.id.et_confirmpassword);
        spRole = (Spinner) findViewById(R.id.sp_role);
        etEmail = (EditText) findViewById(R.id.et_createemail);
        etMobile = (EditText) findViewById(R.id.et_createmobile);
        etAddress=(EditText)findViewById(R.id.et_address);
        etIdentity=(EditText)findViewById(R.id.et_identity);
        tvrole=(TextView)findViewById (R.id.tv_role);
        btnCreatenewuser = (Button)findViewById(R.id.btn_createnewuser);
        progressBar = (ProgressBar)findViewById(R.id.progressbar_createuser);
    }
    private void get_values(){
        email = etEmail.getText().toString().toLowerCase().trim();
        password = etPassword.getText().toString().trim();
        aPassword=etaPassword.getText().toString().trim();
        id = null;
        name = etFullname.getText().toString().toUpperCase();
        role ="USER";
        mobilenumber = etMobile.getText().toString();
        address=etAddress.getText().toString().toUpperCase();
        identity=etIdentity.getText().toString().toUpperCase();
        status="1";
    }
    private Boolean validate(){
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Enter full name!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(role)) {
            Toast.makeText(this, "Enter role!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(mobilenumber)&&mobilenumber.length ()!=10) {
            Toast.makeText(this, "Enter Mobile Number!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Enter email address!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password.equals(aPassword)&&(TextUtils.isEmpty(password))) {
            Toast.makeText(this, "Both password are not same!", Toast.LENGTH_SHORT).show();
            return false;

        }
        if (password.length() < 6) {
            Toast.makeText(this, "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, "Enter Address!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(identity)) {
            Toast.makeText(this, "Enter aadhar or Voter id !", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void registerUser() {
        get_values();
        Boolean ans=validate();
        if(ans) {
            progressBar.setVisibility (View.VISIBLE);
            auth.createUserWithEmailAndPassword (email, password)
                    .addOnCompleteListener (new OnCompleteListener<AuthResult> () {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful ()) {

                                User user = new User (
                                        id,
                                        name,
                                        email,
                                        role,
                                        mobilenumber,
                                        address,
                                        identity,
                                        status

                                );

                                FirebaseDatabase.getInstance ().getReference ("Users")
                                        .child ((Objects.requireNonNull (FirebaseAuth.getInstance ().getCurrentUser ())).getUid ())
                                        .setValue (user)
                                        .addOnCompleteListener (new OnCompleteListener<Void> () {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                progressBar.setVisibility (View.GONE);
                                                if (task.isSuccessful ()) {

                                                    Toast.makeText (getApplicationContext (), "Registration Success", Toast.LENGTH_LONG).show ();
                                                    startActivity (new Intent (CreateuserActivity.this, LoginActivity.class));
                                                    finish ();
                                                }

                                            }
                                        });

                            } else {
                                Toast.makeText (getApplicationContext (), (Objects.requireNonNull (task.getException ())).getMessage (), Toast.LENGTH_LONG).show ();
                            }
                        }
                    });
        }

    }
}
