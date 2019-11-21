package com.p4u.parvarish.menu_items;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.p4u.parvarish.R;
import com.p4u.parvarish.book_pannel.Donor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;


public class DonorListFragment extends Fragment {
    private static final String TAG = "DonorListFragment";
    private ListView listViewdonors;
    private List<Donor> donors;
    private TextInputEditText txtname;
    private DatabaseReference databaseBooks;
    private View v;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate
                (R.layout.fragment_donor_list, container, false);
        databaseBooks = FirebaseDatabase.getInstance().getReference().child("BOOKS");
        Context context = container.getContext();
        //getting views
        initViews();

        donors = new ArrayList<>();
        txtname.addTextChangedListener(new TextWatcher() {
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

        listViewdonors = v.findViewById(R.id.view_list);
        txtname =  v.findViewById(R.id.txtName);
    }

    public void onStart() {

        super.onStart();
        load_list();

    }
    private void load_list() {
        try {
            if (!Objects.requireNonNull(txtname.getText()).toString().equals("")) {
                databaseBooks.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        donors.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Donor donor = postSnapshot.getValue(Donor.class);
                            if (Objects.requireNonNull(donor).getBookDonor().startsWith(txtname.getText().toString().toUpperCase())) {
                                donors.add(donor);
                            }


                        }
                        HashSet<Donor> hs = new HashSet<>(donors); // donor= name of arrayList from which u want to remove duplicates
                        donors.clear();
                        donors.addAll(hs);
                        //creating adapter
                        if(getActivity()!=null) {
                            DonorList_model donorAdapter = new DonorList_model(getActivity(), donors);
                            listViewdonors.setAdapter(donorAdapter);
                            donorAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            } else {
                databaseBooks.addValueEventListener(new ValueEventListener () {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        donors.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Donor donor = postSnapshot.getValue(Donor.class);

                            // mobiles.add(donor.getBookDonorMobile());
                            donors.add(donor);

                        }

                        // Create a new ArrayList

                        HashSet<Donor> hs = new HashSet<>(donors); // donor= name of arrayList from which u want to remove duplicates
                        donors.clear();
                        donors.addAll(hs);
                        //creating adapter
                        if(getActivity()!=null) {
                            DonorList_model donorAdapter = new DonorList_model(getActivity(), donors);
                            listViewdonors.setAdapter(donorAdapter);
                            donorAdapter.notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        }catch (Exception e){
            Log.d(TAG, "Exception in Adding List "+e);
        }
    }




}
