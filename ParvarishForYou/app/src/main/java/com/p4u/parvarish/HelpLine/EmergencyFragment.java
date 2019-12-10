package com.p4u.parvarish.HelpLine;


import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.p4u.parvarish.R;


public class EmergencyFragment extends Fragment {
    private DatabaseReference databaseUsers;
    private Context context;
    private TextInputEditText tv1,tv2;
    private TextInputLayout l1,l2;
    private Button btnpost;
    private View v;
    public EmergencyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.layout_helpline, container, false);
        databaseUsers = FirebaseDatabase.getInstance().getReference().child("USERS").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        context = container.getContext();
        init();
        l1.setVisibility(View.GONE);
        btnpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addno();
            }
        });
        // Inflate the layout for this fragment
        return v;
    }

    private void init() {
        tv1=v.findViewById(R.id.et_service);
        tv2=v.findViewById(R.id.et_mobile);
        l1=v.findViewById(R.id.tv_service);
        l2=v.findViewById(R.id.tv_mobile);
        btnpost=v.findViewById(R.id.btnpost);
    }
    private void addno() {


        //validating
        boolean ans=validate();
        if(ans)
        {
         //Saving the Book
            databaseUsers.child("userRelative").setValue(tv2.getText().toString());
            Toast.makeText(context, "No added", Toast.LENGTH_LONG).show();
            //setting edittext to blank again
            set_blank();

        } else {
            Toast.makeText(context, "fill All required fields ", Toast.LENGTH_LONG).show();
        }
    }
    private boolean validate() {


        if (TextUtils.isEmpty(tv2.getText())) {
            l2.setError("Enter Mobile");
            return false;
        }

        return true;
    }

    private void set_blank(){


        tv2.setText("");


    }

}
