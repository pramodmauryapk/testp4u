package com.p4u.parvarish.Attandence;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.p4u.parvarish.Attandence.admin.SchoolData;
import com.p4u.parvarish.R;

import java.util.ArrayList;

import static com.android.volley.VolleyLog.TAG;
import static java.util.Objects.requireNonNull;


public class SchoolSigninFragment extends Fragment {

private View v;
private Context context;
private Spinner spschoollist,spschoolrole;
private String Name,Passcode;
private Button Signin,cancel;
private TextInputEditText etpasscode;
    private String schoolname,role,sprole;
    private Bundle bundle;
    public SchoolSigninFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_signin, container, false);
        // Inflate the layout for this fragment
        context = container.getContext();
        init();
        bundle=new Bundle();

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("SCHOOL").child("PRINCI");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                load_spinner_items(ds);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        spschoollist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Name=spschoollist.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                Name=spschoollist.getItemAtPosition(0).toString();
            }

        });

        spschoolrole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sprole=spschoolrole.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                sprole=spschoolrole.getItemAtPosition(0).toString();
            }
        });
        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference mRef=FirebaseDatabase.getInstance().getReference().child("SCHOOL").child(sprole);

                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot ds) {

                       Boolean ans=validate(ds);
                       if(ans){

                               switch_menu(new AttandenceMenuFragment());


                       }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etpasscode.setText("");
            }
        });
        return v;
    }

    private Boolean validate(DataSnapshot dataSnapshot) {
        try {

            for (DataSnapshot ds : dataSnapshot.getChildren ()) {
                SchoolData uInfo = ds.getValue(SchoolData.class);
                if (requireNonNull(uInfo).getSchoolName().equals(Name)) {
                    Passcode = uInfo.getPasscode();
                    schoolname=uInfo.getSchoolName();
                    role=uInfo.getSchoolRole();
                    return true;
                }


            }

        }catch (Exception e){
            Log.d(TAG, "Failed to retrive values"+e);
        }
        return false;
    }

    private void init() {
        spschoollist=v.findViewById(R.id.spschoollist);
        spschoolrole=v.findViewById(R.id.spRolelist);
        etpasscode=v.findViewById(R.id.etpasscode);
        etpasscode.setText("123456");
        Signin=v.findViewById(R.id.BtnSignin);
        cancel=v.findViewById(R.id.Btncancel);

    }

    @SuppressLint("SetTextI18n")
    private void load_spinner_items(DataSnapshot dataSnapshot){
        try {
            ArrayList<String> list = new ArrayList<>();
            for (DataSnapshot ds : dataSnapshot.getChildren ()) {

                SchoolData uInfo=ds.getValue(SchoolData.class);
                assert uInfo != null;
                list.add(uInfo.getSchoolName());
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(requireNonNull(context), android.R.layout.simple_spinner_item, list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spschoollist.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

            }



        }catch (Exception e){
            Log.d(TAG, "Failed to retrive values"+e);
        }
    }
    private void switch_menu(Fragment fragment) {
       bundle.putString("SCHOOL_NAME",schoolname);
       bundle.putString("ROLE",role);
       fragment.setArguments(bundle);

       requireNonNull(getActivity()).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.menu_fragment, fragment)
                    .addToBackStack(null)
                    .commit();
        }



}
