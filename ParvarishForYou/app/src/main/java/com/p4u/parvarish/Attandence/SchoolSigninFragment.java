package com.p4u.parvarish.Attandence;


import android.content.Context;
import android.os.Bundle;
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
import com.p4u.parvarish.R;

import java.util.ArrayList;

import static java.util.Objects.requireNonNull;


public class SchoolSigninFragment extends Fragment {

private View v;
private Context context;
private Spinner spschoollist,spschoolrole;
private String Name,Passcode,key;
private Button Signin,cancel;
private TextInputEditText etpasscode;
    private String schoolname,role,sprole;
    private Bundle bundle;

    public SchoolSigninFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_signin, container, false);
        // Inflate the layout for this fragment
        context = container.getContext();
        init();
        Name=null;
        sprole=null;
        Signin.setEnabled(false);
        bundle=new Bundle();
        load_schools();


        spschoollist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Name=spschoollist.getSelectedItem().toString();
                load_roles(Name);

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                Name=spschoollist.getItemAtPosition(0).toString();
                load_roles(Name);
            }

        });

        spschoolrole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sprole=spschoolrole.getSelectedItem().toString();
                enable_button();
                load_passcode(Name,sprole);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                sprole=spschoolrole.getItemAtPosition(0).toString();
                enable_button();
                load_passcode(Name,sprole);
            }
        });
        Signin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(requireNonNull(etpasscode.getText()).toString().equals(Passcode))
                switch_menu(new AttandenceMenuFragment());

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

    private void load_passcode(String Name,String sprole) {
        DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("SCHOOL").child(Name).child(sprole).child("passcode");
        try {
            mref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Passcode = (String) dataSnapshot.getValue();


                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {

        }
    }

    private void enable_button() {
        if(Name!=null&&sprole!=null) {
            Signin.setEnabled(true);

        }
    }

    private void load_schools() {
        FirebaseDatabase.getInstance().getReference().child("SCHOOL").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> list = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    list.add(snapshot.getKey());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireNonNull(context), android.R.layout.simple_spinner_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spschoollist.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                Name=list.get(0);
                load_roles(Name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void load_roles(String Name) {
        FirebaseDatabase.getInstance().getReference().child("SCHOOL").child(Name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> rolelist = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    //here is your every post
                    rolelist.add(snapshot.getKey());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireNonNull(context), android.R.layout.simple_spinner_item, rolelist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spschoolrole.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                sprole=rolelist.get(0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void init() {
        spschoollist=v.findViewById(R.id.spschoollist);
        spschoolrole=v.findViewById(R.id.spRolelist);
        etpasscode=v.findViewById(R.id.etpasscode);
        etpasscode.setText("123456");
        Signin=v.findViewById(R.id.BtnSignin);
        cancel=v.findViewById(R.id.Btncancel);

    }
    private void switch_menu(Fragment fragment) {
       bundle.putString("SCHOOL_NAME",Name);
       bundle.putString("ROLE",sprole);
       fragment.setArguments(bundle);
        if(getActivity()!=null) {
            requireNonNull(getActivity()).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.menu_fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
