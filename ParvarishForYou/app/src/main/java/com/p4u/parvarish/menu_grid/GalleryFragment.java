package com.p4u.parvarish.menu_grid;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.p4u.parvarish.R;
import com.p4u.parvarish.banner_pannel.BannerLayout;
import com.p4u.parvarish.banner_pannel.WebBannerAdapter;
import com.p4u.parvarish.galary.Image_Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GalleryFragment extends Fragment {

    private static final String TAG = "OurWorkFragment";


    private Context context;

    private List<String> list;
    private WebBannerAdapter webBannerAdapter;
    private DatabaseReference mDatabaseRef;
    private View v;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_gallery, container, false);

        initViews();
        context = container.getContext();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("UPLOADED_IMAGES");

        banner_load();
        return v;
    }
    private void initViews(){


    }
    private void banner_load(){
        final BannerLayout recyclerBanner =  v.findViewById(R.id.recycler);
        recyclerBanner.setOrientation(1);

        list = new ArrayList<>();
        webBannerAdapter=new WebBannerAdapter(context,list);
        list.clear();
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot teacherSnapshot : dataSnapshot.getChildren()) {
                    Image_Model upload = teacherSnapshot.getValue(Image_Model.class);
                    Objects.requireNonNull(upload).setKey(teacherSnapshot.getKey());
                    list.add(upload.getImageUrl());

                }

                recyclerBanner.setAdapter(webBannerAdapter);

                webBannerAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                recyclerBanner.setAdapter(webBannerAdapter);

                webBannerAdapter.notifyDataSetChanged();
            }

        });
        recyclerBanner.setAdapter(webBannerAdapter);
    }


}