package com.p4u.parvarish.main_menu;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

import com.p4u.parvarish.R;
import com.p4u.parvarish.banner.BannerLayout;
import com.p4u.parvarish.banner.WebBannerAdapter;
import com.p4u.parvarish.galary.Image_Model;
import com.p4u.parvarish.marquee.NewsTextFragment;


public class HomeFragment extends Fragment {



    private View v;

    private Context context;
    private static final String TAG="HomeFragment";
    private List<String> list;
    private WebBannerAdapter webBannerAdapter;
    private DatabaseReference mDatabaseRef;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.home_fragment,container,false);
        context = container.getContext();
        assert this.getArguments() != null;
        String role = this.getArguments().getString("user_role");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("UPLOADED_IMAGES");
        MainActivity my = new MainActivity();
        my.fragmentStack = new Stack<>();
        //inherit child fragment for marquee
        Fragment child1Fragment = new NewsTextFragment();
        FragmentTransaction transaction1 = getChildFragmentManager().beginTransaction();
        transaction1.replace(R.id.child_fragment_container, child1Fragment).commit();
        //menu fragment and pass user role
       Bundle bundle=new Bundle();
       Fragment child2Fragment = new UserMenuFragment();
        bundle.putString("user_role", role);
        child2Fragment.setArguments(bundle);
        my.fragmentStack.push(child2Fragment);
        FragmentTransaction transaction2 = getChildFragmentManager().beginTransaction();
        transaction2.replace(R.id.menu_fragment_container, child2Fragment).commit();
        transaction2.addToBackStack(null);
        //add banner
        banner();
        return v;
    }



    private void banner(){
        final BannerLayout recyclerBanner =  v.findViewById(R.id.recycler);
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
    @Override
    public void onResume() {
        super.onResume();
    }

}
