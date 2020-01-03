package com.p4u.parvarish.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.p4u.parvarish.MainActivity;
import com.p4u.parvarish.R;
import com.p4u.parvarish.user_pannel.Teacher;

import static java.util.Objects.requireNonNull;

public class Login_mobileActivity extends AppCompatActivity {


    private static final String TAG = Login_mobileActivity.class.getSimpleName();

    private Spinner spinner;
    private EditText editText;
    private DatabaseReference myref;
    private String user_name,user_email,user_roll,user_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_mobile_login);
        (requireNonNull (getSupportActionBar ())).hide ();
        spinner = findViewById(R.id.spinnerCountries);
        spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, CountryData.countryNames));
        editText = findViewById(R.id.editTextPhone);

        findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String code = CountryData.countryAreaCodes[spinner.getSelectedItemPosition()];
                String code="91";
                String number = editText.getText().toString().trim();

                if (number.isEmpty() || number.length() < 10) {
                    editText.setError("Valid number is required");
                    editText.requestFocus();
                    return;
                }

                String phoneNumber = "+" + code + number;

                Intent intent = new Intent(Login_mobileActivity.this, MobileVerifyActivity.class);
                intent.putExtra("phonenumber", phoneNumber);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

       if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            myref = FirebaseDatabase.getInstance().getReference().child("USERS");

            myref.addValueEventListener(new ValueEventListener() {

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Log.d(TAG, "Accessing database");
                    getting_role(dataSnapshot);
                    Intent intent = new Intent(Login_mobileActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("user_name",user_name);
                    intent.putExtra("user_email",user_email);
                    intent.putExtra("user_role",user_roll);
                    intent.putExtra("user_img", user_img);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                     Log.d(TAG, "failed to read values", databaseError.toException());
                }
            });
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
}
