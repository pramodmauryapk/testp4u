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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.p4u.parvarish.R;
import com.p4u.parvarish.menu_items.MainActivity;
import com.p4u.parvarish.user_pannel.Teacher;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class UserMobileAfterActivity extends AppCompatActivity {

    private static final String TAG = "UserRegistrationActivity";
    private TextInputLayout tlname;

    private TextInputLayout tladdress;
    private TextInputEditText etFullname;
    private String user_name,user_email,user_roll,user_img;
    private TextInputEditText etAddress;
    private Button btnregister;
    private DatabaseReference myref;
    private TextView login;
    private String mobile;
    private String name;
    private String email;
    private String password;
    private String role;
    private String mobilenumber;
    private String address;
    private String identity;
    private String status;
    private String feedback;
    private String news;
    private String rating;
    private String time;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_mobile_after);
        (requireNonNull(getSupportActionBar())).hide();
        if (getIntent().getExtras()!=null){

            mobile = getIntent().getExtras().getString("mobile");
        }
        initViews();
        ImageView top_curve = findViewById(R.id.top_curve);
        TextView login_title = findViewById(R.id.registration_title);
        CardView register_card = findViewById(R.id.register_card);


        Animation top_curve_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.top_down);
        top_curve.startAnimation(top_curve_anim);

        Animation editText_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.edittext_anim);
        etFullname.startAnimation(editText_anim);
        etAddress.startAnimation(editText_anim);


        Animation field_name_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.field_name_anim);
        tlname.startAnimation(field_name_anim);
        tladdress.startAnimation(field_name_anim);
        login_title.startAnimation(field_name_anim);

        Animation center_reveal_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.center_reveal_anim);
        register_card.startAnimation(center_reveal_anim);


        /*
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            myref = FirebaseDatabase.getInstance().getReference().child("USERS");
            myref.addValueEventListener(new ValueEventListener() {

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Log.d(TAG, "Accessing database");
                    getting_role(dataSnapshot);
                    Intent intent = new Intent(UserMobileAfterActivity.this, MainActivity.class);
                    intent.putExtra("user_name",user_name);
                    intent.putExtra("user_email",user_email);
                    intent.putExtra("user_role",user_roll);
                    intent.putExtra("user_img", user_img);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    /// Log.d(TAG, "failed to read values", databaseError.toException());
                }
            });

        }else {*/
            btnregister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    uploadFile();


                }
            });
       // }
        change_listner(etFullname, tlname);
        change_listner(etAddress, tladdress);


    }


    private void initViews() {
        etFullname = findViewById(R.id.et_fullname);
        etAddress = findViewById(R.id.et_address);
        tlname = findViewById(R.id.tv_createusername);
        tladdress = findViewById(R.id.tv_address);
        btnregister = findViewById(R.id.register_button);

    }


    private void get_values() {
        name = requireNonNull(etFullname.getText()).toString().toUpperCase();
        email="";
        password="";
        role = "USER";
        mobilenumber = mobile;
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


            String id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
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
                            null
                    );
                    myref.child(Objects.requireNonNull(id)).setValue(upload);
                    Toast.makeText(UserMobileAfterActivity.this, "Registration successful", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(UserMobileAfterActivity.this, MainActivity.class);
                    intent.putExtra("user_name",name);
                    intent.putExtra("user_email",mobilenumber);
                    intent.putExtra("user_role",role);
                    intent.putExtra("user_img", (String) null);
                    startActivity(intent);
                    finish();




        }
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void getting_role(DataSnapshot dataSnapshot) {

        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Teacher uInfo=ds.getValue(Teacher.class);
            if(requireNonNull(uInfo).getUserId().equals(requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())) {
                user_name = requireNonNull(uInfo).getUserName();
                user_email = uInfo.getUserEmail();
                user_roll = uInfo.getUserRole();
                user_img = uInfo.getImageURL();


            }

        }

    }

    private String get_current_time(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss");
        return sdf.format(new Date());
    }
}
