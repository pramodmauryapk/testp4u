package com.p4u.parvarish.menu_items;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.p4u.parvarish.HomeFragment;
import com.p4u.parvarish.R;
import com.p4u.parvarish.user_pannel.ReqestedUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Stack;

import static java.util.Objects.requireNonNull;

public class JoinUsFragment extends Fragment {

    private static final String TAG = JoinUsFragment.class.getSimpleName();

    private Button btnregister;
    private View v;
    private TextInputLayout tlname,tlpassword,tlapassword,tlemail,tlmobile,tladdress,tlidentity;
    private TextInputEditText etFullname;
    private TextInputEditText etEmail;
    private TextInputEditText etMobile;
    private TextInputEditText etAddress;
    private Stack<Fragment> fragmentStack;
    private ProgressBar progressBar;
    private Fragment newContent;
    private Bundle bundle;
    private DatabaseReference myref;
    private String email;
    private Intent intent;
    private String name,user_name,user_email,user_roll,user_img;
    private Context context;
    private String mobilenumber;
    private String address;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_joinus, container, false);
        context = container.getContext();
        initViews();

        //get Firebase auth instance
        myref = FirebaseDatabase.getInstance().getReference().child("REQUESTED_USERS");
        //intent object
        Intent intent = requireNonNull(getActivity()).getIntent();
        user_name = intent.getStringExtra("user_name");
        user_email=intent.getStringExtra("user_email");
        user_roll=intent.getStringExtra("user_role");
        user_img = intent.getStringExtra("user_img");

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    upload_info();

            }
        });
        return v;
    }

    private void initViews(){


        etFullname = v.findViewById(R.id.et_fullname);
        etEmail =v. findViewById(R.id.et_createemail);
        etMobile = v.findViewById(R.id.et_createmobile);
        etAddress=v.findViewById(R.id.et_address);
        tlname=v.findViewById(R.id.tv_createusername);
        tlemail=v.findViewById(R.id.tv_createemail);
        tlmobile=v.findViewById(R.id.tv_createmobile);
        tladdress=v.findViewById(R.id.tv_address);
        progressBar = v.findViewById(R.id.pb);
        btnregister=v.findViewById(R.id.register_button);

    }

    private void get_values(){
        name = requireNonNull(etFullname.getText()).toString().toUpperCase();
        email = requireNonNull(etEmail.getText()).toString().toLowerCase().trim();
        mobilenumber = requireNonNull(etMobile.getText()).toString();
        address= requireNonNull(etAddress.getText()).toString().toUpperCase();

    }
    private Boolean validate(){
        if (TextUtils.isEmpty(name)) {
            tlname.setError("Enter full name!");
            return false;
        }
        if (mobilenumber.length ()!=10) {
            tlmobile.setError("Enter Mobile Number!");
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            tlemail.setError("Enter email address!");
            return false;
        }

        if (TextUtils.isEmpty(address)) {
            tladdress.setError("Enter Address!");
            return false;
        }

        return true;
    }
    private void upload_info() {
        get_values();
        Boolean ans=validate();
        if(ans) {


            progressBar.setVisibility(View.VISIBLE);
            progressBar.setIndeterminate(true);
            //Do what you want with the url
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setIndeterminate(false);
                    progressBar.setProgress(0);
                }
            }, 500);
            String id = myref.push().getKey();
            ReqestedUser upload = new ReqestedUser(
                    id,
                    name,
                    email,
                    mobilenumber,
                    address,
                    get_current_time()
            );
            myref.child(Objects.requireNonNull(id)).setValue(upload);
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(context, "Request Send Successfully", Toast.LENGTH_LONG).show();

            //login default homefragment
            fragmentStack = new Stack<>();
            //transferring some fields to fragment
            Bundle bundle=new Bundle();
            bundle.putString("user_name",user_email);
            bundle.putString("user_role",user_roll);
            bundle.putString("user_name",user_name);
            bundle.putString("user_img",user_img);
            newContent = new HomeFragment();
            newContent.setArguments(bundle);
            FragmentManager fragmentManager = requireNonNull(getActivity()).getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.add(R.id.content_frame, newContent);
            fragmentStack.push(newContent);
            ft.commit();


        }


        else {
            Toast.makeText(context, "Request Cannot send", Toast.LENGTH_SHORT).show();
        }

    }
    private String get_current_time(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss");
        return sdf.format(new Date());
    }



}
