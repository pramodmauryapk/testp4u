package com.p4u.parvarish.Attandence.student;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.p4u.parvarish.R;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewnoticeFragment extends Fragment {

    private DatabaseReference databaseUsers;
    private List<NoticeData> users;
    private ListView listViewUsers;
    private String schoolname;
    private Bundle bundle;
    private View v;
    private TextView tv1,tv2;
    private Context context;
    private Button btnback;
    public ViewnoticeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v=inflater.inflate(R.layout.fragment_viewleave, container, false);
        bundle=new Bundle();
        schoolname = requireNonNull(this.getArguments()).getString("SCHOOL_NAME");
        databaseUsers = FirebaseDatabase.getInstance().getReference().child("SCHOOL").child(schoolname).child("STUDENTS").child("NOTICES");
        context = container.getContext();
        //getting views
        init();
        tv1.setText("Notice Board");
        users = new ArrayList<>();
        listViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Helpline user = users.get(i);

            }
        });
        return v;
    }
    private void init() {
        listViewUsers = v.findViewById(R.id.view_list);
        tv1=v.findViewById(R.id.tv1);
    }
    public void onStart() {

        super.onStart();
        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    NoticeData user = postSnapshot.getValue(NoticeData.class);
                    users.add(user);

                }


                NoticeList_model userAdapter = new NoticeList_model(getActivity(), users);
                listViewUsers.setAdapter(userAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
