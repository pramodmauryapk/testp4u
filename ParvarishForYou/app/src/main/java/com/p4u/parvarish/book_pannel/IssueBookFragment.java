package com.p4u.parvarish.book_pannel;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.ListView;
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
import com.p4u.parvarish.fancydialog.Animation;
import com.p4u.parvarish.fancydialog.FancyAlertDialog;
import com.p4u.parvarish.fancydialog.FancyAlertDialogListener;
import com.p4u.parvarish.fancydialog.Icon;
import com.p4u.parvarish.user_pannel.TempUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class IssueBookFragment extends Fragment {

    private static final String TAG = "IssueBookFragment";
    private ListView listViewBooks;
    private View dialogView,issuedialog,benefiarydialog;
    private List<Book> books;
    private ArrayList<String> benefiary;
    private DatabaseReference mRef;
    private EditText spBookName;
    private Spinner spbenfiary;
    private TextView tv2;
    private TextView tv1;
    private View v;
    private TextInputEditText etFullname,etEmail,etMobile,etAddress,etIdentity,etDepositpaid;
    private TextInputLayout tlname,tlemail,tlmobile,tladdress,tlidentity,tldeposit;
    private Button handover;
    private Button btnadd;
    private Button btnsearch;
    private Button buttonback;
    private Button btnreset;
    private Bundle bundle;
    private String Userid,Fullname,Email,Mobile,Address,Identity,Deposit,beneficaiary_mobile,name;
    private Boolean amount_validation=false;
    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_book_issue,container,false);
        DatabaseReference myref = FirebaseDatabase.getInstance().getReference().child("BOOKS");

        //getting views
        initViews();
        blank_all();
        bundle=new Bundle();

        benefiary = new ArrayList<>();

        btnadd.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("InflateParams")
            @Override
            public void onClick(View view) {
                get_values();
                boolean res=validate();
                if(res) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
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
                                        Userid=create_user(Fullname,Email,Address,Mobile,Identity,Deposit);
                                        disable_all();
                                        tv1.setText("BENEFICIARY REGISTRED");
                                        Toast.makeText(getContext(), "Beneficiary Created", Toast.LENGTH_SHORT).show();
                                        switchFragment(new SelectBookFragment());
                                    }

                                });
                            }
                        }
                    });

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
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getLayoutInflater();
                benefiarydialog = inflater.inflate(R.layout.beneficiary_dialog, null);
                dialogBuilder.setView(benefiarydialog);
                spbenfiary=benefiarydialog.findViewById(R.id.beneficiry_list);
                buttonback=benefiarydialog.findViewById(R.id.dbuttonBack);
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
                spbenfiary.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        beneficaiary_mobile=spbenfiary.getSelectedItem().toString();

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
        return v;
    }

    private void load_values(final String beneficaiary_mobile) {
        try {
            mRef = FirebaseDatabase.getInstance().getReference().child("TEMPUSERS");
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

        mRef = FirebaseDatabase.getInstance().getReference().child("TEMPUSERS");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                benefiary.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    TempUser beneficiary = postSnapshot.getValue(TempUser.class);

                    assert beneficiary != null;
                    benefiary.add(beneficiary.getMobile());


                }
                ArrayAdapter<String> beneficiary_adapter = new ArrayAdapter<>(requireNonNull(getContext()), android.R.layout.simple_spinner_item, benefiary);
                spbenfiary.setAdapter(beneficiary_adapter);
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
        DatabaseReference temp_User = FirebaseDatabase.getInstance().getReference().child("TEMPUSERS");
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
        bundle.putString("Beneficiary_Id",Userid);
        bundle.putString("Beneficiary_Name",name);
        fragment.setArguments(bundle);
        requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null).commit();

    }





}