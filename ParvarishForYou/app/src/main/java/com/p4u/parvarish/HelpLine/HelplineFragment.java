package com.p4u.parvarish.HelpLine;


import android.Manifest;
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
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.p4u.parvarish.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class HelplineFragment extends Fragment {
    private DatabaseReference databaseUsers;
    private List<Helpline> users;
    private ListView listViewUsers;
    private View v;
    private TextView tv1,tv2;
    private Context context;
    private Button btnback;
    public HelplineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_center_list, container, false);
        databaseUsers = FirebaseDatabase.getInstance().getReference().child("HELPLINE");
        context = container.getContext();
        //getting views
        initViews();
        tv1.setText("Helpline No's");
        tv2.setText("Emergency Services");
        users = new ArrayList<>();
        listViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Helpline user = users.get(i);
                call(user.getMobile());
            }
        });
       // Inflate the layout for this fragment
        return v;
    }

    private void call(final String mobile) {
        // Creating alert Dialog with two Buttons
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        // Setting Dialog Title
        alertDialog.setTitle("Do you want to call?");
        // Setting Dialog Message
        alertDialog.setMessage(mobile);
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
                        callIntent.setData(Uri.parse("tel:" +mobile));
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



    private void initViews() {
        listViewUsers = v.findViewById(R.id.view_list);
        tv1=v.findViewById(R.id.tv1);
        tv2=v.findViewById(R.id.tv2);
    }

    public void onStart() {

        super.onStart();
        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Helpline user = postSnapshot.getValue(Helpline.class);
                        users.add(user);

                }


                HelplineList_model userAdapter = new HelplineList_model(getActivity(), users);
                listViewUsers.setAdapter(userAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
