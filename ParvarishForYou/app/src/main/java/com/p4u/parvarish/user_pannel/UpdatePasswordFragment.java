package com.p4u.parvarish.user_pannel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import com.p4u.parvarish.R;
import com.p4u.parvarish.main_menu.HomeFragment;


public class UpdatePasswordFragment extends Fragment {
    private String oldpassword,newpassword;
    private EditText oldpass,newpass;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_update_password, container, false);
        oldpass = v.findViewById(R.id.et_oldpass);
        newpass = v.findViewById(R.id.et_newpass);
        Button btnsave = v.findViewById(R.id.btn_save);
        Button btnback = v.findViewById(R.id.btn_back);
        //get Firebase auth instance
        FirebaseAuth auth = FirebaseAuth.getInstance();

        String username = "ParvarishForYou";
        DatabaseReference myref = FirebaseDatabase.getInstance().getReference().child("Users").child(username).child("password");
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oldpassword = oldpass.getText().toString();
                newpassword = newpass.getText().toString();


            }
        });

/*
        myref.addValueEventListener(addListenerForSingleValueEvent(){
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                String password = dataSnapshot.getValue(String.class);
                if(password.equals(pass)){
                    myref.setValue(newpass);
                }else{
                    // show wrong pass dialog
                }
                // ...
            }

            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message

                // ...
            }
        };
*/
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

    @Override
    public void onResume() {
        super.onResume();
    }

    private void switchFragment(Fragment fragment) {

        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null).commit();
    }
}