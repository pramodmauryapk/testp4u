package com.p4u.parvarish.book_pannel;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.p4u.parvarish.user_pannel.TempUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class BenficiaryFragment extends Fragment {

    private static final String TAG = "BeneficiaryFragment";
    private DatabaseReference mRef;
    private View dialogView;
    private RelativeLayout rl;
    private TextView tv2;
    private List<TempUser> users;
    private ListView beneficiarylist;
    private TextInputEditText search_beneficiary;
    private TextInputLayout tf1;
    private TextView duserid,dtvname,dtvemail,dtvmobile,dtvaddress,dtvidentity,dtvbookshaving,dtvdeposit,dtvrefund;
    private View v;
    private Button btnselect;
    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_book_search, container, false);

        initViews();
        mRef= FirebaseDatabase.getInstance().getReference().child("TEMPUSERS");
        initViews();
        rl.setVisibility(View.GONE);
        tv2.setText("Tap to User to Return Book");
        tf1.setHint("Beneficiary-Name/Mobile/Email");
        //list to store books
        users = new ArrayList<>();

      beneficiarylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final TempUser user = users.get(i);
                showDialog(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getAddress(),
                        user.getMobile(),
                        user.getIdentity(),
                        user.getBookHaving(),
                        user.getBookDeposit(),
                        user.getBookRefund());


            }
        });


        search_beneficiary.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                onStart();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return v;
    }
    private void initViews(){
        search_beneficiary =  v.findViewById(R.id.sp_Book_Name);
        tv2=v.findViewById(R.id.tv2);
        beneficiarylist =  v.findViewById(R.id.view_list);
        rl=v.findViewById(R.id.userlayout);
        tf1=v.findViewById(R.id.tf1);

    }
    public void onStart() {

        super.onStart();
        load_list();

    }
    private void load_list() {


            if (!requireNonNull(search_beneficiary.getText()).toString().equals("")) {
                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        users.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            TempUser user = postSnapshot.getValue(TempUser.class);
                            if(Objects.requireNonNull(user).getName().startsWith(search_beneficiary.getText().toString().toUpperCase())||
                                    Objects.requireNonNull(user).getMobile().startsWith(search_beneficiary.getText().toString())||
                                    Objects.requireNonNull(user).getEmail().startsWith(search_beneficiary.getText().toString())) {
                                try {
                                    users.add(user);
                                }catch (Exception e)
                                {
                                    Log.d(TAG, "Exception in Adding List "+e);
                                }
                            }

                        }

                        Layoutbeneficiary_details UserAdapter = new Layoutbeneficiary_details(getActivity(), users);
                        beneficiarylist.setAdapter(UserAdapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }else {
                mRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        users.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            TempUser user = postSnapshot.getValue(TempUser.class);
                            try {
                                users.add(user);
                            }catch (Exception e)
                            {
                                Log.d(TAG, "Exception in Adding List "+e);
                            }

                        }

                        Layoutbeneficiary_details UserAdapter = new Layoutbeneficiary_details(getActivity(), users);
                        beneficiarylist.setAdapter(UserAdapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

    }
    private void init_dialog_views(){
        duserid=dialogView.findViewById (R.id.tvUserId);
        dtvname = dialogView.findViewById(R.id.tvName);
        dtvemail =dialogView.findViewById(R.id.tvEmail);
        dtvmobile = dialogView.findViewById(R.id.tvMobile);
        dtvaddress = dialogView.findViewById(R.id.tvAddress);
        dtvidentity = dialogView.findViewById(R.id.tvIdentity);
        dtvbookshaving = dialogView.findViewById(R.id.tvBooksHaving);
        dtvdeposit = dialogView.findViewById(R.id.tvDeposit);
        dtvrefund = dialogView.findViewById(R.id.tvRefund);
        btnselect=dialogView.findViewById(R.id.dbuttonSelect);
    }
    private void set_dialog_values(String Id,String Name,String Email,String Mobile,String Address,String Identity,String booksCount,String Deposit,String Refund ) {
        duserid.setText(Id);
        dtvname.setText(Name);
        dtvemail.setText(Email);
        dtvmobile.setText(Mobile);
        dtvaddress.setText(Address);
        dtvidentity.setText(Identity);
        dtvbookshaving.setText(booksCount);
        dtvdeposit.setText(Deposit);
        dtvrefund.setText(Refund);


    }
    @SuppressLint("InflateParams")
    private void showDialog(final String duserId,
                            final String duserName,
                            final String duserEmail,
                            final String duserMobile,
                            final String duserAddress,
                            final String duserIdentity,
                            final String duserBookHaving,
                            final String duserBookDeposit,
                            final String duserBookRefund) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.show_beneficiary_dialog, null);
        dialogBuilder.setView(dialogView);

        init_dialog_views();

        dialogBuilder.setTitle("Benficiary Details");
        final AlertDialog b = dialogBuilder.create();
        b.show();

        set_dialog_values(duserId,duserName,duserEmail,duserMobile,duserAddress,duserIdentity,duserBookHaving,duserBookDeposit,duserBookRefund);
        btnselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.cancel();
                Bundle bundle=new Bundle();
                Fragment newfragment = new SubmitBookFragment();
                bundle.putString("user_id", duserId);
                bundle.putString("user_noofbooks", duserBookHaving);
                bundle.putString("user_deposit", duserBookDeposit);
                bundle.putString("user_refund", duserBookRefund);
                newfragment.setArguments(bundle);
                switchFragment(newfragment);
            }
        });


    }

    // switching fragment
    private void switchFragment(Fragment fragment) {

        requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null).commit();

    }


}
