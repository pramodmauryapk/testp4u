package com.p4u.parvarish.menu_items;

import android.content.Context;
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

public class OurWorkFragment extends Fragment {

    private static final String TAG = OurWorkFragment.class.getSimpleName();

    private EditText editText;
    private TextView textView;
    private Button button;
    private Context context;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_our_work, container, false);
        context = container.getContext();
        initViews();
        return v;
    }
    private void initViews(){

    }

}
