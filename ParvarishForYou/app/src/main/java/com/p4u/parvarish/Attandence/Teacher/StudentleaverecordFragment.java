package com.p4u.parvarish.Attandence.Teacher;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.p4u.parvarish.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class StudentleaverecordFragment extends Fragment {

    private View v;

    public StudentleaverecordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_studentleaverecord, container, false);
        // Inflate the layout for this fragment
        return v;
    }

}
