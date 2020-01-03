package com.p4u.parvarish.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import com.p4u.parvarish.R;
import com.p4u.parvarish.user_pannel.Teacher;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class UserRegistrationActivity extends AppCompatActivity {

    private static final String TAG = UserRegistrationActivity.class.getSimpleName();

    private TextInputLayout tlname;
    private TextInputLayout tlpassword;
    private TextInputLayout tlapassword;
    private TextInputLayout tlemail;
    private TextInputLayout tlmobile;
    private TextInputLayout tladdress;
    private TextInputEditText etFullname;
    private TextInputEditText etPassword;
    private TextInputEditText etaPassword;
    private TextInputEditText etEmail;
    private TextInputEditText etMobile;
    private TextInputEditText etAddress;
    private FirebaseAuth auth;
    private Button btnregister;
    private DatabaseReference myref;
    private TextView login;
    private String email, password, aPassword, id, name, role, mobilenumber, address, identity, status, feedback, news, rating, time;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_registration);
        (requireNonNull(getSupportActionBar())).hide();
        initViews();
        ImageView top_curve = findViewById(R.id.top_curve);
        TextView login_title = findViewById(R.id.registration_title);
        LinearLayout already_have_account_layout = findViewById(R.id.already_have_account_text);
        CardView register_card = findViewById(R.id.register_card);


        Animation top_curve_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.top_down);
        top_curve.startAnimation(top_curve_anim);

        Animation editText_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.edittext_anim);
        etFullname.startAnimation(editText_anim);
        etPassword.startAnimation(editText_anim);
        etaPassword.startAnimation(editText_anim);
        etEmail.startAnimation(editText_anim);
        etMobile.startAnimation(editText_anim);
        etAddress.startAnimation(editText_anim);


        Animation field_name_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.field_name_anim);
        tlname.startAnimation(field_name_anim);
        tlemail.startAnimation(field_name_anim);

        tladdress.startAnimation(field_name_anim);
        tlapassword.startAnimation(field_name_anim);
        tlpassword.startAnimation(field_name_anim);
        tlmobile.startAnimation(field_name_anim);
        login_title.startAnimation(field_name_anim);

        Animation center_reveal_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.center_reveal_anim);
        register_card.startAnimation(center_reveal_anim);

        Animation new_user_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.down_top);
        already_have_account_layout.startAnimation(new_user_anim);
        //get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        myref = FirebaseDatabase.getInstance().getReference().child("USERS");






        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserRegistrationActivity.this, Login_emailActivity.class));
                finish();
            }
        });
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    uploadFile();


            }
        });
        change_listner(etFullname, tlname);
        change_listner(etEmail, tlemail);
        change_listner(etMobile, tlmobile);
        change_listner(etPassword, tlpassword);
        change_listner(etaPassword, tlapassword);
        change_listner(etAddress, tladdress);


    }


    private void initViews() {
        etFullname = findViewById(R.id.et_fullname);
        etPassword = findViewById(R.id.et_createpassword);
        etaPassword = findViewById(R.id.et_confirmpassword);

        etEmail = findViewById(R.id.et_createemail);
        etMobile = findViewById(R.id.et_createmobile);
        etAddress = findViewById(R.id.et_address);

        tlname = findViewById(R.id.tv_createusername);
        tlemail = findViewById(R.id.tv_createemail);
        tlmobile = findViewById(R.id.tv_createmobile);
        tlpassword = findViewById(R.id.tv_createpassword);
        tlapassword = findViewById(R.id.tv_comfirmpassword);
        tladdress = findViewById(R.id.tv_address);
        login = findViewById(R.id.login);
        btnregister = findViewById(R.id.register_button);

    }


    private void get_values() {
        email = requireNonNull(etEmail.getText()).toString().toLowerCase().trim();
        password = requireNonNull(etPassword.getText()).toString().trim();
        aPassword = requireNonNull(etaPassword.getText()).toString().trim();
        name = requireNonNull(etFullname.getText()).toString().toUpperCase();
        role = "USER";
        mobilenumber = requireNonNull(etMobile.getText()).toString();
        address = requireNonNull(etAddress.getText()).toString().toUpperCase();
        identity = "";
        status = "1";
        feedback = "";
        news = "";
        rating = "";
        time = get_current_time();
    }

    private Boolean validate() {
        if (TextUtils.isEmpty(name)) {
            tlname.setError("Enter full name!");
            return false;
        }
        if (mobilenumber.length() != 10) {
            tlmobile.setError("Enter Mobile Number!");
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            tlemail.setError("Enter email address!");
            return false;
        }
        if (!password.equals(aPassword)) {
            tlapassword.setError("Both password are not same!");
            return false;
        }
        if (TextUtils.isEmpty(address)) {
            tladdress.setError("Enter Address!");
            return false;
        }

        return true;
    }

    private void change_listner(final TextView v, final TextInputLayout til) {


        v.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                til.setErrorEnabled(false);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

        });
    }

    private void uploadFile() {
        get_values();
        Boolean ans = validate();
        if (ans) {

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                    Teacher upload = new Teacher(
                            id,
                            name,
                            email,
                            password,
                            role,
                            mobilenumber,
                            address,
                            identity,
                            status,
                            feedback,
                            news,
                            time,
                            rating,
                            "",
                            1,
                            null
                    );
                    myref.child(Objects.requireNonNull(id)).setValue(upload);
                    Toast.makeText(UserRegistrationActivity.this, "Registration successful", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(UserRegistrationActivity.this, Login_emailActivity.class));
                    finish();
                }
            });

        }
    }





    private String get_current_time(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss");
        return sdf.format(new Date());
    }
}
