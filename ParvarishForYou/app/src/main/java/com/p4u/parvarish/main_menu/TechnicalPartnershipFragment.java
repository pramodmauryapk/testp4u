package com.p4u.parvarish.main_menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.p4u.parvarish.R;

public class TechnicalPartnershipFragment extends Fragment {



    private View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_technical_partnership,container,false);
        //Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        initViews();



        return v;
    }
    private void initViews(){

    }


}
