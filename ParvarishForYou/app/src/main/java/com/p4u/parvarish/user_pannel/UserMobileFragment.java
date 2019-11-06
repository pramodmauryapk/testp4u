package com.p4u.parvarish.user_pannel;

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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
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
    private Context context;
    private TextInputLayout t1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_user_mobiles, container, false);
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
                if(getActivity()!=null) {
                    UserList_model userAdapter = new UserList_model(getActivity(), users);
                    listViewUsers.setAdapter(userAdapter);
                }
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
        dialogBuilder.setTitle("Registered User Details");
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
        dbuttonUpdate.setOnClickListener(new View.OnClickListener() {
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
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // Write your code here to execute after dialog
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + userMobile));
                                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling

                                    //    Activity#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for Activity#requestPermissions for more details.
                                }else {
                                    Objects.requireNonNull(context).startActivity(callIntent);
                                    dialog.cancel();
                                }


                            }

                            private int checkSelfPermission(String callPhone) {
                                return 0;
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
        dtvIdentity = dialogView.findViewById(R.id.etIdentity);
        dtvRole =  dialogView.findViewById(R.id.tvRole);
        dbuttonBack = dialogView.findViewById(R.id.dbuttonBack);
        dbuttonUpdate=dialogView.findViewById(R.id.dbuttonUpdate);
        dbuttonDelete =  dialogView.findViewById(R.id.dbuttonDelete);
        t1=dialogView.findViewById(R.id.l5);

    }

    private void switchFragment(Fragment fragment) {
        if(getActivity()!=null) {
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .addToBackStack(null).commit();
        }
    }


}
