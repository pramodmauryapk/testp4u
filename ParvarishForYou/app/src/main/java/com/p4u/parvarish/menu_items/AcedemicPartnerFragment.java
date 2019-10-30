package com.p4u.parvarish.menu_items;

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

import com.p4u.parvarish.R;

public class AcedemicPartnerFragment extends Fragment {

    private static final String TAG = "OurWorkFragment";
    private EditText editText;
    private TextView textView;
    private Button button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_acedemic_partner, container, false);

        initViews();
        return v;
    }
    private void initViews(){

    }

}
