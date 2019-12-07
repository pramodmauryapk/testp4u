package com.p4u.parvarish.Attandence.raw;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.p4u.parvarish.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ModifyattendanceFragment extends Fragment {


    public ModifyattendanceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_modifyattendance, container, false);
    }

}
