package com.p4u.parvarish.user_pannel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.p4u.parvarish.R;
import com.p4u.parvarish.gallary.TouchImageView;
import com.squareup.picasso.Picasso;

public class UserDetailsFragment extends Fragment {

    private static final String TAG = UserDetailsFragment.class.getSimpleName();

    private TextView nameDetailTextView;
    private TextView Email;
    private TextView Role;
    private TextView Mobile,Identity,Address,Status;
    private TouchImageView teacherDetailImageView;
    private View v;
    private Context context;
    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_user_detail,container,false);
        context = container.getContext();
        initializeWidgets();
        //RECEIVE DATA FROM ITEMSACTIVITY VIA INTENT
        //Intent i=getActivity().getIntent();
        assert getArguments() != null;
        String name= getArguments().getString("NAME_KEY");
        String email=getArguments().getString("EMAIL_KEY");
        String imageURL=getArguments().getString("IMAGE_KEY");
        String mobile=getArguments().getString("MOBILE_KEY");
        String role=getArguments().getString("ROLE_KEY");
        String identity=getArguments().getString("IDENTITY_KEY");
        String address=getArguments().getString("ADDRESS_KEY");
        String status=getArguments().getString("STATUS_KEY");



        //SET RECEIVED DATA TO TEXTVIEWS AND IMAGEVIEWS
        nameDetailTextView.setText(name);
        Email.setText(email);
        Role.setText("ROLE: "+role);
        Mobile.setText("MOBILE: "+mobile);
        Identity.setText("IDENTITY: "+identity);
        Address.setText("ADDRESS: "+address);
        assert status != null;
        if(status.equals("1")){
            Status.setText("STATUS: "+"ACTIVE USER");

        }
        else {
            Status.setText("STATUS: "+"USER NOT ACTIVE");
        }


        Picasso.get()
                .load(imageURL)
                .placeholder(R.drawable.userpic)
                .fit()
                .centerCrop()
                .into(teacherDetailImageView);


        return v;
    }

    private void initializeWidgets(){
        nameDetailTextView= v.findViewById(R.id.nameDetailTextView);
        Email= v.findViewById(R.id.tviewemail);
        Role= v.findViewById(R.id.tviewrole);
        Mobile = v.findViewById(R.id.tviewmobile);
        Identity=v.findViewById(R.id.tviewidentity);
        Address=v.findViewById(R.id.tviewaddress);
        Status=v.findViewById(R.id.tviewstatus);
        teacherDetailImageView=v.findViewById(R.id.teacherDetailImageView);
    }



}
