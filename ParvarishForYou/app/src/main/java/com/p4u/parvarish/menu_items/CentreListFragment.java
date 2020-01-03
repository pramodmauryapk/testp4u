package com.p4u.parvarish.menu_items;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.p4u.parvarish.HomeFragment;
import com.p4u.parvarish.R;
import com.p4u.parvarish.user_pannel.Teacher;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;


public class CentreListFragment extends HomeFragment {
    private static final String TAG = CentreListFragment.class.getSimpleName();

    private ListView listViewUsers;
    private List<Teacher> users;
    private View dialogView;
    private TextView dtvUsername, dtvEmail, dtvMobile, dtvCenterName, dtvIdentity, dtvRole;
    private Button dbuttonSave, dbuttonChange, dbuttonUpload;
    private DatabaseReference databaseUsers;
    private androidx.appcompat.app.AlertDialog.Builder builder;
    private TextInputLayout t1;
    private TextView txt;
    private LinearLayout l12,l13;
    private View v;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate
                (R.layout.fragment_center_list, container, false);
        databaseUsers = FirebaseDatabase.getInstance().getReference().child("USERS");
        context = container.getContext();
        //getting views
        initViews();

        users = new ArrayList<>();
        listViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Teacher user = users.get(i);
                showDeleteDialog(
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

                HashSet<Teacher> hs; // users= name of arrayList from which u want to remove duplicates
                hs = new HashSet<>(users);

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

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
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
        //dtvIdentity.setText(userIdentity);
        dtvRole.setText(userRole);
        dtvRole.setEnabled(false);
        dbuttonSave.setText("CALL");
        dbuttonChange.setVisibility(View.GONE);
        dbuttonUpload.setVisibility(View.GONE);
        l12.setVisibility(View.GONE);

        txt.setVisibility(View.GONE);
        dbuttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                // Creating alert Dialog with two Buttons
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                // Setting Dialog Title
                alertDialog.setTitle("Do you want to call?");
                // Setting Dialog Message
                alertDialog.setMessage(userMobile);
                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.ic_phone_in_talk_black_24dp);
                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // Write your code here to execute after dialog
                                dialog.cancel();
                            }
                        });
                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE =1 ;
                            public void onClick(DialogInterface dialog,int which) {
                                // Write your code here to execute after dialog
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" +userMobile));
                                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
                                        Manifest.permission.CALL_PHONE)
                                        != PackageManager.PERMISSION_GRANTED) {

                                    ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),
                                            new String[]{Manifest.permission.CALL_PHONE},
                                            MY_PERMISSIONS_REQUEST_CALL_PHONE);

                                    // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
                                    // app-defined int constant. The callback method gets the
                                    // result of the request.
                                } else {
                                    //You already have permission
                                    try {
                                        startActivity(callIntent);
                                        dialog.cancel();
                                    } catch (SecurityException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }

                        });

                // Showing Alert Message
                alertDialog.show();
            }
        });
    }



    private void init_dialog_views(){

        dtvUsername =dialogView.findViewById(R.id.etUserName);
        dtvEmail = dialogView.findViewById(R.id.etEmailID);
        dtvMobile =  dialogView.findViewById(R.id.etMobile);
        dtvCenterName =  dialogView.findViewById(R.id.tvCenterName);
        dtvRole =  dialogView.findViewById(R.id.tvRole);
        dbuttonSave = dialogView.findViewById(R.id.dbuttonsave);
        dbuttonChange=dialogView.findViewById(R.id.dbuttonchange);
        dbuttonUpload =  dialogView.findViewById(R.id.upload_button);
        txt=dialogView.findViewById(R.id.txt);
        l12=dialogView.findViewById(R.id.l12);


    }


}
