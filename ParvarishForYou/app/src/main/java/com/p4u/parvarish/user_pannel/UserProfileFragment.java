package com.p4u.parvarish.user_pannel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.p4u.parvarish.R;

import static java.util.Objects.requireNonNull;

public class UserProfileFragment extends Fragment {
    private static final String TAG = "UserProfileFragment";
    private String userID,userName,userEmail,userMobile,userIdentity;
    private String userPassword,userAgainPassword,userRole,userAddress,userStatus,userFeedback,userNews,userTime,userRating,userImg;
    private View v;
    private EditText username,email,mobile,identity,password,againpassword,feedback,news,time;
    private TextInputLayout l1,l2,l3,l4,l5;
    private TextView location,role;
    private List<Teacher> users;
    private Button save,change,back;
    private LinearLayout l9;
    private Spinner sp;
    private String userrole;
    private Context context;
    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate (R.layout.layout_profile, container, false);
        context = container.getContext();
        initViews ();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        //Declare database reference
        DatabaseReference myRef = mFirebaseDatabase.getReference().child("USERS");

        final FirebaseUser user = mAuth.getCurrentUser();
        userID = (requireNonNull(user)).getUid();
        back.setVisibility(View.GONE);
        change.setText("Change Password");
        save.setText("Save");
        myRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "Accessing database");
                show(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "failed to read values", databaseError.toException());
            }
        });
        save.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_values();
                updatedetails(userID,
                        userPassword,
                        userImg);
            }
        });

       change.setOnClickListener (new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               switchFragment(new UpdatePasswordFragment());
           }
       });
        change_listner(username,l1);
        change_listner(email,l2);
        change_listner(mobile,l3);
        change_listner(location,l4);
        change_listner(identity,l5);
        return v;
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
    private void initViews(){
        username=v.findViewById(R.id.etUserName);
        email=v.findViewById(R.id.etEmailID);
        mobile=v.findViewById(R.id.etMobile);
        identity=v.findViewById(R.id.etIdentity);
        location=v.findViewById(R.id.tvCenterName);
        role=v.findViewById(R.id.tvRole);
        sp=v.findViewById(R.id.sprole);
        back = v.findViewById(R.id.dbuttonBack);
        change=v.findViewById(R.id.dbuttonUpdate);
        save=  v.findViewById(R.id.dbuttonDelete);

        l1=v.findViewById(R.id.l1);
        l2=v.findViewById(R.id.l2);
        l3=v.findViewById(R.id.l3);
        l4=v.findViewById(R.id.l4);
        l5=v.findViewById(R.id.l5);
        //save=v.findViewById (R.id.dbuttonSave);
        //change=v.findViewById (R.id.dbuttonchangepassword);


    }
    private void get_values(){
        userName=username.getText ().toString ().trim ().toUpperCase ();
        userEmail=email.getText ().toString ().trim ();
        userMobile=mobile.getText ().toString ().trim ();
        userIdentity=identity.getText ().toString ().trim ().toUpperCase();
    }
    private void show(DataSnapshot dataSnapshot){
        try {
            for (DataSnapshot ds : dataSnapshot.getChildren ()) {
                    Teacher uInfo=ds.getValue(Teacher.class);
                    if(requireNonNull(uInfo).getUserId().equals(userID)) {
                        username.setText(requireNonNull(uInfo).getUserName());
                        email.setText(uInfo.getUserEmail());
                        mobile.setText(uInfo.getUserMobile());
                        location.setText(uInfo.getUserAddress());
                        identity.setText(uInfo.getUserIdentity());
                        userrole=uInfo.getUserRole();
                        if(userrole.equals("ADMIN")){
                            role.setVisibility(View.GONE);
                            sp.setVisibility(View.VISIBLE);
                        }
                        else {
                            role.setText(uInfo.getUserRole());
                            role.setEnabled(false);
                        }

                        userImg = uInfo.getImageURL();
                    }
            }
        }catch (Exception e){
            Log.d(TAG, "Failed to retrive values"+e);
        }
    }
    private void updatedetails(String ID,
                               String Password,
                               String Img) {

        String Name = username.getText().toString().trim().toUpperCase();
        String Email = email.getText().toString().trim().toLowerCase();
        String Mobile = mobile.getText().toString().trim();
        String Identity = identity.getText().toString().trim().toUpperCase();
        String Role;
        if(userrole.equals("ADMIN")){
            Role =sp.getSelectedItem().toString().toUpperCase();
        }else {
            Role = role.getText().toString().toUpperCase();
        }
        String Address = location.getText().toString().toUpperCase();
        String Status = "1";
        String Feedback = "";
        String News = "";
        String Rating = "";
        String Time = get_current_time();
        //updating artist
        boolean ans=validate(Name, Email, Mobile, Identity, Role, Address, Status, Feedback, News, Rating, Time);
        if(ans) {
            //getting the specified artist reference
            DatabaseReference dR = FirebaseDatabase.getInstance().getReference().child("USERS").child(ID);
            Teacher user = new Teacher(
                    ID,
                    Name,
                    Email,
                    Password,
                    Role,
                    Mobile,
                    Address,
                    Identity,
                    Status,
                    Feedback,
                    News,
                    Time,
                    Rating,
                    Img);

            dR.setValue(user);
            Toast.makeText(context, "User info Updated", Toast.LENGTH_LONG).show();
        }
    }

    private boolean validate(String name, String email, String mobile, String identity, String role, String address, String status, String feedback, String news, String rating, String time) {

        if (TextUtils.isEmpty(name)) {
            l1.setError("Enter Name");
            return false;
        }
        if (TextUtils.isEmpty(email)) {
            l2.setError("Enter Email");
            return false;
        }
        if (TextUtils.isEmpty(address)) {
            l4.setError("Enter Address");
            return false;
        }
        if (TextUtils.isEmpty(identity)) {
            l5.setError("Enter identity");
            return false;
        }
        if (mobile.length() != 10) {
            l3.setError("Enter Mobile");
            return false;
        }


        return true;
    }

    private String get_current_time(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss");
        return sdf.format(new Date());
    }
    
}