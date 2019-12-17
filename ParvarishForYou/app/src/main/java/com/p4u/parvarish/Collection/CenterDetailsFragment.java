package com.p4u.parvarish.Collection;

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
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.p4u.parvarish.R;
import com.p4u.parvarish.gallary.TouchImageView;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class CenterDetailsFragment extends Fragment {

    private static final String TAG = "UserDetailsFragment";
    private TextView nameDetailTextView;
    private TextView Email;
    private TextView Role;
    private ImageButton call;
    private TextView Mobile,Identity,Address,Status;
    private TouchImageView teacherDetailImageView;
    private View v;
    private Context context;
    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_center_detail,container,false);
        context = container.getContext();
        initializeWidgets();
        //RECEIVE DATA FROM ITEMSACTIVITY VIA INTENT
        //Intent i=getActivity().getIntent();
        assert getArguments() != null;
        String name= getArguments().getString("NAME_KEY");
        String address=getArguments().getString("ADDRESS_KEY");
        String imageURL=getArguments().getString("IMAGE_KEY");
        final String mobile=getArguments().getString("MOBILE_KEY");




        //SET RECEIVED DATA TO TEXTVIEWS AND IMAGEVIEWS
        nameDetailTextView.setText(name);
        Mobile.setText("MOBILE: "+mobile);
        Address.setText(address);


        Picasso.get()
                .load(imageURL)
                .placeholder(R.drawable.userpic)
                .fit()
                .centerCrop()
                .into(teacherDetailImageView);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call(mobile);
            }
        });


        return v;
    }

    private void call(final String mobile) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Do you want to call?");
        alertDialog.setMessage(mobile);
        alertDialog.setIcon(R.drawable.ic_phone_in_talk_black_24dp);
        alertDialog.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE =1 ;
                    public void onClick(DialogInterface dialog,int which) {

                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" +mobile));

                        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
                                Manifest.permission.CALL_PHONE)
                                != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),
                                    new String[]{Manifest.permission.CALL_PHONE},
                                    MY_PERMISSIONS_REQUEST_CALL_PHONE);


                        } else {

                            try {
                                startActivity(callIntent);
                                dialog.cancel();
                            } catch (SecurityException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                });


        alertDialog.show();
    }

    private void initializeWidgets(){
        nameDetailTextView= v.findViewById(R.id.nameDetailTextView);
        call=v.findViewById(R.id.callbtn);
        Mobile = v.findViewById(R.id.tviewmobile);
        Address=v.findViewById(R.id.tviewaddress);
        teacherDetailImageView=v.findViewById(R.id.teacherDetailImageView);
    }



}
