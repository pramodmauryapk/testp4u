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

public class CreateuserActivity extends AppCompatActivity {
    private static final String TAG = "CreateuserActivity";
    private EditText etFullname, etPassword,etaPassword, etEmail, etMobile,etAddress,etIdentity;
    private FirebaseAuth auth;
    private Spinner spRole;
    private ProgressBar progressBar;
    private DatabaseReference myref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createuser);
        etFullname = (EditText) findViewById(R.id.et_fullname);
        etPassword = (EditText) findViewById(R.id.et_createpassword);
        etaPassword= (EditText) findViewById(R.id.et_confirmpassword);
        //spRole = (Spinner) findViewById(R.id.sp_role);
        etEmail = (EditText) findViewById(R.id.et_createemail);
        etMobile = (EditText) findViewById(R.id.et_createmobile);
        etAddress=(EditText)findViewById(R.id.et_address);
        etIdentity=(EditText)findViewById(R.id.et_identity);

        Button btnCreatenewuser = (Button)findViewById(R.id.btn_createnewuser);
        progressBar = (ProgressBar)findViewById(R.id.progressbar_createuser);

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
    private void registerUser() {
        final String email = etEmail.getText().toString().toLowerCase().trim();
        final String password = etPassword.getText().toString().trim();
        final String aPassword=etaPassword.getText().toString().trim();
        final String id = null;
        final String name = etFullname.getText().toString().toUpperCase();
        final String role ="USER";
                //spRole.getSelectedItem().toString();
        final String mobilenumber = etMobile.getText().toString();
        final String address=etAddress.getText().toString().toUpperCase();
        final String identity=etIdentity.getText().toString().toUpperCase();
        final String status="1";

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Enter full name!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(role)) {
            Toast.makeText(this, "Enter role!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(mobilenumber)) {
            Toast.makeText(this, "Enter Mobile Number!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.equals(aPassword)&&(TextUtils.isEmpty(password))) {
                Toast.makeText(this, "Both password are not same!", Toast.LENGTH_SHORT).show();
                return;

        }
        if (password.length() < 6) {
            Toast.makeText(this, "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, "Enter Address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(identity)) {
            Toast.makeText(this, "Enter aadhar or Voter id !", Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult> () {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

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
                          //  myref.child(Objects.requireNonNull(id)).setValue(user)
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child((Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser())).getUid())
                                    .setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void> () {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {

                                        Toast.makeText(getApplicationContext(), "Registration Success", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(CreateuserActivity.this, LoginActivity.class));
                                        finish();
                                    }  //display a failure message

                                }
                            });

                        } else {
                            Toast.makeText(getApplicationContext(),(Objects.requireNonNull(task.getException())).getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}
