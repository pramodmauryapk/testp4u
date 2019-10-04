package com.p4u.parvarish.user_pannel;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import com.google.firebase.database.ValueEventListener;
import com.p4u.parvarish.R;
import com.p4u.parvarish.main_menu.HomeFragment;

import static java.util.Objects.requireNonNull;


public class UpdatePasswordFragment extends Fragment {
    private static final String TAG = "UpdatePasswordFragment";
    private String password,apassword;
    private EditText txtpass,txtapass;
    private TextInputLayout tf1,tf2;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_update_password, container, false);
        txtpass = v.findViewById(R.id.et_oldpass);
        txtapass = v.findViewById(R.id.et_newpass);
        Button btnsave = v.findViewById(R.id.btn_save);
        Button btnback = v.findViewById(R.id.btn_back);
        tf1=v.findViewById(R.id.tf1);
        tf2=v.findViewById(R.id.tf2);
        //get Firebase auth instance
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userID = (requireNonNull(user)).getUid();
        final DatabaseReference myref = FirebaseDatabase.getInstance().getReference().child("USERS").child(userID).child("userPassword");

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password = txtpass.getText().toString();
                apassword = txtapass.getText().toString();
                if (TextUtils.isEmpty(password))
                {
                    tf1.setError("Enter Password!");

                }
               else if(!password.equals(apassword)&&password.length()>=6){
                   tf2.setError("Both Password not equal and length>=6");
                }

                else{
                    myref.setValue(apassword);
                    Toast.makeText(getContext(),"Password Updated",Toast.LENGTH_LONG).show();
                }


            }
        });

       btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdatePasswordFragment.this.closefragment();
            }
        });
        return v;
    }
    private void closefragment() {
       switchFragment (new HomeFragment());

    }



    private void switchFragment(Fragment fragment) {

        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null).commit();
    }

}