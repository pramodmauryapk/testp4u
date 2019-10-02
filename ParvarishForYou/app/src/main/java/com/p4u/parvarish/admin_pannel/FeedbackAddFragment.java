package com.p4u.parvarish.admin_pannel;

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

public class FeedbackAddFragment extends Fragment {


    private DatabaseReference myref;
    private EditText editText;
    private TextView textView;
    private Button button;
    private View v;
    private RatingBar ratingBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_news_add,container,false);

        myref = FirebaseDatabase.getInstance().getReference().child("News");

        initViews();
        textView.setText ("Enter Feedback ");
        button.setText ("Submit");
        ratingBar.setVisibility (View.VISIBLE);
        return v;
    }
    private void initViews(){
        editText= v.findViewById (R.id.marqueetext);
        textView= v.findViewById (R.id.txt1);
        button= v.findViewById (R.id.btn_add);
        ratingBar= v.findViewById (R.id.ratingBar);
    }


}
