package com.p4u.parvarish.main_menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.p4u.parvarish.R;
import com.p4u.parvarish.admin_pannel.Book;


public class DonorListFragment extends Fragment {

    private ListView listViewdonors;
    private List<Book> donors;

    private DatabaseReference databaseBooks;
    private View v;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate
                (R.layout.fragment_donor_list, container, false);
        databaseBooks = FirebaseDatabase.getInstance().getReference().child("books");
        //getting views
        initViews();

        donors = new ArrayList<>();

        return v;
    }
    private void initViews(){

        listViewdonors = v.findViewById(R.id.view_list);

    }
    public void onStart() {

        super.onStart();
        databaseBooks.addValueEventListener(new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                donors.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Book donor = postSnapshot.getValue(Book.class);

                        donors.add(donor);



                }
                HashSet hs = new HashSet();

                hs.addAll(donors); // donor= name of arrayList from which u want to remove duplicates

                donors.clear();
                donors.addAll(hs);
                //creating adapter

               DonorList donorAdapter = new DonorList(getActivity(), donors);
               listViewdonors.setAdapter(donorAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }






}
