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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.p4u.parvarish.R;

import static java.util.Objects.requireNonNull;


public class AddHelplineFragment extends Fragment {
    private DatabaseReference databaseUsers;
    private Context context;
    private TextInputEditText tv1,tv2;
    private TextInputLayout l1,l2;
    private Button btnpost;
    private View v;
    public AddHelplineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.layout_helpline, container, false);
        databaseUsers = FirebaseDatabase.getInstance().getReference().child("HELPLINE");
        context = container.getContext();
        init();
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

            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our Book
            String Id = databaseUsers.push().getKey();
            //creating an Book Object
            Helpline row = new Helpline (
                    tv1.getText().toString(),
                    tv2.getText().toString());

            //Saving the Book
            databaseUsers.child(requireNonNull(Id)).setValue(row);
            Toast.makeText(context, "No added", Toast.LENGTH_LONG).show();
            //setting edittext to blank again
            set_blank();

        } else {
            Toast.makeText(context, "fill All required fields ", Toast.LENGTH_LONG).show();
        }
    }
    private boolean validate() {

        if (TextUtils.isEmpty(tv1.getText())) {
            l1.setError("Enter Service Name");

            return false;
        }
        if (TextUtils.isEmpty(tv2.getText())) {
            l2.setError("Enter Mobile");
            return false;
        }

        return true;
    }

    private void set_blank(){

        tv1.setText("");
        tv2.setText("");


    }

}
