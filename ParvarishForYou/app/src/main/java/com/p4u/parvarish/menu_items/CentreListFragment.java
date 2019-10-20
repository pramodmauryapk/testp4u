package com.p4u.parvarish.menu_items;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import com.p4u.parvarish.R;
import com.p4u.parvarish.menu_grid.HomeFragment;
import com.p4u.parvarish.user_pannel.Teacher;


public class CentreListFragment extends HomeFragment {
    private static final String TAG = "CenterListFragment";
    private ListView listViewUsers;
    private List<Teacher> users;
    private View dialogView;
    private TextView dtvUsername, dtvEmail, dtvMobile, dtvCenterName, dtvIdentity, dtvRole;
    private Button dbuttonBack, dbuttonDelete, dbuttonUpdate;
    private DatabaseReference databaseUsers;
    private androidx.appcompat.app.AlertDialog.Builder builder;
    private TextInputLayout t1;
    private View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate
                (R.layout.fragment_center_list, container, false);
        databaseUsers = FirebaseDatabase.getInstance().getReference().child("USERS");

        //getting views
        initViews();

        users = new ArrayList<>();
        listViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Teacher user = users.get(i);
                CentreListFragment.this.showDeleteDialog(
                        //  user.getUserId (),
                        user.getUserName(),
                        user.getUserEmail(),
                        user.getUserRole(),
                        user.getUserMobile(),
                        user.getUserAddress(),
                        user.getUserIdentity());
            }
        });
        return v;
    }

    private void initViews() {

        listViewUsers = v.findViewById(R.id.view_list);

    }

    public void onStart() {

        super.onStart();
        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Teacher user = postSnapshot.getValue(Teacher.class);
                    if (Objects.requireNonNull(user).getUserRole().equals("HEAD")) {
                        users.add(user);
                    }
                }

                HashSet hs = new HashSet(users); // users= name of arrayList from which u want to remove duplicates

                users.clear();
                users.addAll(hs);
                CenterList_model userAdapter = new CenterList_model(getActivity(), users);
                listViewUsers.setAdapter(userAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    private void showDeleteDialog(
                                  final String userName,
                                  final String userEmail,
                                  final String userRole,
                                  final String userMobile,
                                  final String userAddress,
                                  final String userIdentity) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.layout_profile, null);
        dialogBuilder.setView(dialogView);

        init_dialog_views();

        dialogBuilder.setTitle("Center Head Details");
        final AlertDialog b = dialogBuilder.create();
        b.show();
        dtvUsername.setText(userName);
        dtvUsername.setEnabled(false);
        dtvEmail.setText(userEmail);
        dtvEmail.setEnabled(false);
        dtvMobile.setText(userMobile);
        dtvMobile.setEnabled(false);
        dtvCenterName.setText(userAddress);
        dtvCenterName.setEnabled(false);
        dtvIdentity.setText(userIdentity);
        dtvRole.setText(userRole);
        dtvRole.setEnabled(false);
        t1.setVisibility(View.GONE);
        dbuttonDelete.setVisibility(View.GONE);
        dbuttonUpdate.setText("CALL");
        dbuttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.cancel();
            }
        });

    }

    private void init_dialog_views(){

        dtvUsername =dialogView.findViewById(R.id.etUserName);
        dtvEmail = dialogView.findViewById(R.id.etEmailID);
        dtvMobile =  dialogView.findViewById(R.id.etMobile);
        dtvCenterName =  dialogView.findViewById(R.id.tvCenterName);
        dtvIdentity = dialogView.findViewById(R.id.etIdentity);
        dtvRole =  dialogView.findViewById(R.id.tvRole);
        dbuttonBack = dialogView.findViewById(R.id.dbuttonBack);
        dbuttonUpdate=dialogView.findViewById(R.id.dbuttonUpdate);
        dbuttonDelete =  dialogView.findViewById(R.id.dbuttonDelete);
        t1=dialogView.findViewById(R.id.l5);

    }


}
