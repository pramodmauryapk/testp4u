package com.p4u.parvarish.user_pannel;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.p4u.parvarish.R;

import java.util.Objects;

import static java.util.Objects.requireNonNull;


public class UpdatePasswordFragment extends Fragment {
    private static final String TAG = "UpdatePasswordFragment";
    private String oldpassword,password, apassword;
    private EditText txtoldpass,txtpass, txtapass;
    private TextInputLayout tf1, tf2,tf3;
    private Context context;
    private FirebaseUser user;
    private DatabaseReference myref;
    private String userID;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_update_password, container, false);
        context = container.getContext();
        txtoldpass=v.findViewById(R.id.et_oldpass);
        txtpass = v.findViewById(R.id.et_newpass);
        txtapass = v.findViewById(R.id.et_confirmpassword);
        Button btnsave = v.findViewById(R.id.btn_save);

        tf1 = v.findViewById(R.id.tf1);
        tf2 = v.findViewById(R.id.tf2);
        tf3 = v.findViewById(R.id.tf3);
        //get Firebase auth instance
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


       if(getArguments() != null) {
           userID = getArguments().getString("ID_KEY");
       }else{
           userID = (requireNonNull(user)).getUid();
       }

        myref = FirebaseDatabase.getInstance().getReference().child("USERS").child(userID).child("userPassword");
        change_listner(txtoldpass,tf1);
        change_listner(txtpass,tf2);
        change_listner(txtapass,tf3);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oldpassword=txtoldpass.getText().toString();
                password = txtpass.getText().toString();
                apassword = txtapass.getText().toString();
                boolean ans=validate();
                if(ans){
                    change_password(oldpassword,apassword);
                }


            }
        });



        return v;
    }

    private boolean validate() {
        if (TextUtils.isEmpty(oldpassword)) {
            tf1.setError("Enter old Password!");
            return false;

        } else if (TextUtils.isEmpty(password)) {
            tf2.setError("Enter Password!");
            return false;

        }
        else if(!password.equals(apassword) && password.length() >= 6){
            tf3.setError("Both Password not equal and length>=6");
            return false;
        }
        return true;
    }



    private void change_password(String oldpass, final String newPass) {

        user = FirebaseAuth.getInstance().getCurrentUser();
        final String email = requireNonNull(user).getEmail();
        AuthCredential credential = EmailAuthProvider.getCredential(requireNonNull(email), oldpass);
        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(context,"Password Not Updated",Toast.LENGTH_LONG).show();
                            } else {

                                myref.setValue(apassword);
                                Toast.makeText(context,"Password Updated",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void change_listner(final TextView v, final TextInputLayout til){
    v.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                til.setErrorEnabled(false);
            }

            public void beforeTextChanged(CharSequence s, int start,int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,int before, int count) {

            }

        });
    }

    private void switchFragment(Fragment fragment) {

        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null).commit();
    }

}