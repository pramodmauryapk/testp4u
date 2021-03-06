package com.p4u.parvarish.Beneficiary;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.p4u.parvarish.R;
import com.p4u.parvarish.book_pannel.SelectBookFragment;
import com.p4u.parvarish.user_pannel.TempUser;

import java.util.ArrayList;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class AddBeneficiaryFragment extends Fragment {

    private static final String TAG = AddBeneficiaryFragment.class.getSimpleName();

    private View dialogView;
    private View beneficiarydialog;
    private ArrayList<String> beneficiary_list;
    private DatabaseReference mRef;
    private Spinner spbeneficiary;
    private TextView tv1;
    private View v;
    private TextInputEditText etFullname,etEmail,etMobile,etAddress,etIdentity,etDepositpaid,etsearch;
    private TextInputLayout tlname,tlemail,tlmobile,tladdress,tlidentity,tldeposit;
    private Button handover;
    private Button btnadd;
    private Button btnsearch;
    private Button buttonback;
    private Button btnreset;
    private Bundle bundle;
    private Boolean ans=true;
    private String Userid,Fullname,Email,Mobile,Address,Identity,Deposit,beneficaiary_mobile,name;
    private Boolean amount_validation=false;
    private Context context;
    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_add_beneficiary,container,false);

        context = container.getContext();
        //getting views
        initViews();
        blank_all();
        bundle=new Bundle();

        beneficiary_list = new ArrayList<>();

        btnadd.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("InflateParams")
            @Override
            public void onClick(View view) {
                get_values();
                boolean res=validate();
                check_mobile(Mobile.trim());

                if(res && ans ) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                    LayoutInflater inflater = getLayoutInflater();
                    dialogView = inflater.inflate(R.layout.show_issuebook_dialog, null);
                    dialogBuilder.setView(dialogView);
                    init_dialog_views();
                    dialogBuilder.setTitle("Amount To be Paid");
                    final AlertDialog dialog = dialogBuilder.create();
                    dialog.show();

                    etDepositpaid.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            get_dialog_values();
                            amount_validation=validate_deposit();

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            if (amount_validation) {
                                handover.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.cancel();
                                        Userid=create_user(Fullname,Email.trim(),Address,Mobile.trim(),Identity.trim(),Deposit);
                                        disable_all();
                                        tv1.setText("BENEFICIARY REGISTRED");
                                        Toast.makeText(context, "Beneficiary Created", Toast.LENGTH_SHORT).show();
                                        switchFragment(new SelectBookFragment());
                                    }

                                });
                            }
                        }
                    });

                }else{
                    Toast.makeText(context, "Check Beneficiary already registered", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnsearch.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("InflateParams")
            @Override
            public void onClick(View view) {

                blank_all();
                tv1.setText("BENEFICIARY DETAILS");

                Userid=null;
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                LayoutInflater inflater = getLayoutInflater();
                beneficiarydialog = inflater.inflate(R.layout.beneficiary_dialog, null);
                dialogBuilder.setView(beneficiarydialog);
                spbeneficiary=beneficiarydialog.findViewById(R.id.beneficiry_list);
                etsearch=beneficiarydialog.findViewById(R.id.etsearch);
                buttonback=beneficiarydialog.findViewById(R.id.dbuttonBack);
                dialogBuilder.setTitle("Beneficiary List");
                final AlertDialog b = dialogBuilder.create();
                b.show();
                load_beneficiary();
                buttonback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        b.cancel();

                    }
                });
                spbeneficiary.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        etsearch.setText(spbeneficiary.getSelectedItem().toString());
                        beneficaiary_mobile= requireNonNull(etsearch.getText()).toString();
                        load_values(beneficaiary_mobile);
                        disable_all();
                      //  btnadd.setEnabled(false);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            }
        });
        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blank_all();
                enable_all();
                tv1.setText("NEW BENEFICIARY");
            }
        });
        change_listner(etFullname,tlname);
        change_listner(etEmail,tlemail);
        change_listner(etMobile,tlmobile);
        change_listner(etAddress,tladdress);
        change_listner(etIdentity,tlidentity);
        return v;
    }
    public void onResume(){
        super.onResume();
        blank_all();

    }
    private void change_listner(final TextView v,final TextInputLayout til){



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
    private void load_values(final String beneficaiary_mobile) {
        try {
            mRef = FirebaseDatabase.getInstance().getReference().child("BENEFICIARY");
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        TempUser beneficiary = postSnapshot.getValue(TempUser.class);
                        if(beneficaiary_mobile.equals(requireNonNull(beneficiary).getMobile())){
                            set_values(beneficiary.getId(),beneficiary.getName(),beneficiary.getEmail(),beneficiary.getAddress(),beneficiary.getMobile(),beneficiary.getIdentity());
                        }



                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }catch (Exception ignored){

        }
    }
    private void check_mobile(final String mobile) {
        try {
            mRef = FirebaseDatabase.getInstance().getReference().child("BENEFICIARY");

            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        TempUser beneficiary = postSnapshot.getValue(TempUser.class);
                        if(mobile.equals(requireNonNull(beneficiary).getMobile())){
                            ans=false;
                        }



                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }catch (Exception ignored){

        }

    }
    private void set_values(String id, String name, String email, String address, String mobile, String identity) {
        Userid=id;
        amount_validation=false;
        Fullname=name;
        etFullname.setText(name);
        etEmail.setText(email);
        etMobile.setText(mobile);
        etAddress.setText(address);
        etIdentity.setText(identity);
    }
    private void load_beneficiary() {

        mRef = FirebaseDatabase.getInstance().getReference().child("BENEFICIARY");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                beneficiary_list.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    TempUser beneficiary = postSnapshot.getValue(TempUser.class);

                    assert beneficiary != null;
                    beneficiary_list.add(beneficiary.getMobile());


                }
                ArrayAdapter<String> beneficiary_adapter = new ArrayAdapter<>(requireNonNull(context), android.R.layout.simple_spinner_item, beneficiary_list);
                spbeneficiary.setAdapter(beneficiary_adapter);
                beneficiary_adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
    private void blank_all() {
        Userid=null;
        Deposit="";
        Fullname=null;
        etFullname.setText("");
        etEmail.setText("");
        etMobile.setText("");
        etAddress.setText("");
        etIdentity.setText("");
    }
    private void disable_all() {
        etFullname.setEnabled(false);
        etEmail.setEnabled(false);
        etMobile.setEnabled(false);
        etAddress.setEnabled(false);
        etIdentity.setEnabled(false);
        if(!etFullname.isEnabled()){
            btnadd.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    switchFragment(new SelectBookFragment());
                }
            });
        }

    }
    private void enable_all() {
        etFullname.setEnabled(true);
        etEmail.setEnabled(true);
        etMobile.setEnabled(true);
        etAddress.setEnabled(true);
        etIdentity.setEnabled(true);
        btnadd.setEnabled(true);
        amount_validation=false;
        Userid=null;

    }
    private void get_dialog_values() {

        Deposit= requireNonNull(etDepositpaid.getText()).toString();

    }
    private void initViews(){


        btnadd=v.findViewById(R.id.btnAdd);
        btnsearch=v.findViewById(R.id.btnsearch);
        btnreset=v.findViewById(R.id.btnreset);
        tv1=v.findViewById(R.id.tv1);
        etFullname=v.findViewById(R.id.et_fullname);
        etEmail=v.findViewById(R.id.et_createemail);
        etAddress=v.findViewById(R.id.et_address);
        etMobile=v.findViewById(R.id.et_createmobile);
        etIdentity=v.findViewById(R.id.et_identity);
        tlname=v.findViewById(R.id.tv_createusername);
        tlemail=v.findViewById(R.id.tv_createemail);
        tladdress=v.findViewById(R.id.tv_address);
        tlmobile=v.findViewById(R.id.tv_createmobile);
        tlidentity=v.findViewById(R.id.tv_identity);


    }
    private void get_values(){
        Fullname= Objects.requireNonNull(etFullname.getText()).toString().toUpperCase();
        Email= Objects.requireNonNull(etEmail.getText()).toString();
        Mobile = Objects.requireNonNull(etMobile.getText()).toString().toUpperCase();
        Address= Objects.requireNonNull(etAddress.getText()).toString().toUpperCase();
        Identity= Objects.requireNonNull(etIdentity.getText()).toString().toUpperCase();


    }
    private boolean validate() {


        if (TextUtils.isEmpty(Fullname)) {
            tlname.setError("Enter full name!");
            return false;
        }
        if (Mobile.length ()!=10) {
            tlmobile.setError("Enter Mobile Number!");
            return false;
        }

        if (TextUtils.isEmpty(Email)) {
            tlemail.setError("Enter email address!");
            return false;
        }

        if (TextUtils.isEmpty(Address)) {
            tladdress.setError("Enter Address!");
            return false;
        }
        if (TextUtils.isEmpty(Identity)) {
            tlidentity.setError("Enter aadhar or Voter id !");
            return false;
        }

        return true;
    }
    private boolean validate_deposit(){
        if (TextUtils.isEmpty(Deposit)) {
            tldeposit.setError("Enter Valid Amount!");
            return false;
        }
        return true;
    }
    private void init_dialog_views(){

        etDepositpaid=dialogView.findViewById(R.id.et_depositpaid);
        handover = dialogView.findViewById(R.id.dbuttonissue);
        tldeposit=dialogView.findViewById(R.id.tv_depositpaid);

    }
    private String create_user(String Fullname,String Email,String Address,String Mobile,String Identity,String Deposit) {
        DatabaseReference temp_User = FirebaseDatabase.getInstance().getReference().child("BENEFICIARY");
        String UserId = temp_User.push().getKey();
        name=Fullname;

        TempUser user = new TempUser(
                UserId,
                Fullname,
                Email,
                Mobile,
                Address,
                Identity,
                "0",
                Deposit,
                "0");

        //Saving the Book
        temp_User.child(requireNonNull(UserId)).setValue(user);

        return UserId;
    }
    // switching fragment
    private void switchFragment(Fragment fragment) {
        bundle.putString("Beneficiary_Id", Userid);
        bundle.putString("Beneficiary_Name", name);
        fragment.setArguments(bundle);
        if (getActivity() != null) {
            requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .addToBackStack(null).commit();

        }
    }





}