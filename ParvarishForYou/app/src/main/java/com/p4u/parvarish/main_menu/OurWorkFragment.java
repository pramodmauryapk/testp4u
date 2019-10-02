package com.p4u.parvarish.main_menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.p4u.parvarish.R;

public class OurWorkFragment extends Fragment {


    private EditText editText;
    private TextView textView;
    private Button button;
    private RatingBar ratingBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_our_work, container, false);
        DatabaseReference myref = FirebaseDatabase.getInstance().getReference().child("News");
        initViews();



        return v;
    }
    private void initViews(){

    }


}
