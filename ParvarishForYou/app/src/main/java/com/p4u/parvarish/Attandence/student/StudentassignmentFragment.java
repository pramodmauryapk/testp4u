package com.p4u.parvarish.Attandence.student;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.p4u.parvarish.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentassignmentFragment extends Fragment {


    public StudentassignmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_studentassignment, container, false);
    }

}
