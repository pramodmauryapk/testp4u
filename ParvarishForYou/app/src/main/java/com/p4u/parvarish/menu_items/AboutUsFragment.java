package com.p4u.parvarish.menu_items;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;

import com.p4u.parvarish.R;

public class AboutUsFragment extends Fragment {


    private DatabaseReference myref;
    private Context context;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_about_us, container, false);
        context = container.getContext();


        initViews();

        return v;
    }
    private void initViews(){

    }


}
