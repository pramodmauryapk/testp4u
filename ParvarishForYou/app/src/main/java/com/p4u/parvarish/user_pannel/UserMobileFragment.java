package com.p4u.parvarish.user_pannel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.p4u.parvarish.R;

public class UserMobileFragment extends Fragment {
    private static final String TAG = "UserMobileFragment";
    private ListView listViewUsers;
    private List<Teacher> users;
    private View dialogView;
    private DatabaseReference databaseUsers;
    private EditText dtvUsername,dtvEmail, dtvMobile , dtvCenterName, dtvIdentity ;
    private TextView dtvRole;
    private Button dbuttonBack,dbuttonDelete,dbuttonUpdate;
    private View v;
    private Builder builder;
    private String Name,Email,Mobile,location,Identity,Role,status;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate
                (R.layout.fragment_user_mobiles, container, false);
        databaseUsers = FirebaseDatabase.getInstance().getReference().child("USERS");
        //getting views

        initViews();

        users = new ArrayList<>();


        return v;
    }


    private void initViews(){

        listViewUsers = v.findViewById(R.id.view_list);


    }
    public void onStart() {

        super.onStart();
        databaseUsers.addValueEventListener(new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                users.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Teacher user = postSnapshot.getValue(Teacher.class);

                        users.add (user);
                }
                UserList userAdapter = new UserList(getActivity(), users);
                listViewUsers.setAdapter(userAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void switchFragment(Fragment fragment) {

       Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null).commit();
    }
    @Override
    public void onResume() {
        super.onResume();
    }

}
