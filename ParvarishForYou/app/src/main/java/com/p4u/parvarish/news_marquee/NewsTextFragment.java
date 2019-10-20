package com.p4u.parvarish.news_marquee;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.p4u.parvarish.R;

public class NewsTextFragment extends Fragment {
    private static final String TAG = "NewsTextFragment";
    private TextView tv;
    private View v;
    private String text;

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_news_text, container, false);
        initViews();
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("WELCOME_TEXT").child("marqueeText");

        try {
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String value = (String) dataSnapshot.getValue();
                    // do your stuff here with value

                    tv.setText(value);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }


            });
        } catch (Exception e){
            tv.setText("error fetching news ");

        }finally {

            tv.setSelected(true);
        }
        return v;
    }


    private void initViews() {
        tv = v.findViewById(R.id.newsmarquee);
    }



}
