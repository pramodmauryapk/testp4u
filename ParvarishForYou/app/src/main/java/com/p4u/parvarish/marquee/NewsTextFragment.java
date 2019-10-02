package com.p4u.parvarish.marquee;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.p4u.parvarish.R;

public class NewsTextFragment extends Fragment {

    private TextView tv;
    private String newsId;
    private FirebaseAuth mAuth;
    private View v;
    private String text;

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_news_text, container, false);
        initViews();
        mAuth=FirebaseAuth.getInstance();
       newsId = "-Lozxh9p34me1oPFPFu6";
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Welcome_text");
        try {
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                 //  for (DataSnapshot ds : dataSnapshot.getChildren()) {

                   //    News news = ds.getValue(News.class);
                   //    if (news.getNewsId().equals(newsId)) {
                   //        text = requireNonNull(news).getMarqueeText();
                   //        tv.setText(text);
                   //    }
                  // }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e){
            tv.setText("This is an initiative by alumni of Jawahar Navodaya Vidyalaya. ");

        }finally {

            tv.setSelected(true);  // Set focus to the textview
        }
        return v;
    }


    private void initViews() {
        tv = v.findViewById(R.id.newsmarquee);
    }



}
