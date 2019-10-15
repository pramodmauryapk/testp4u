package com.p4u.parvarish.news_marquee;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.p4u.parvarish.R;

public class NewsAddFragment extends Fragment {
    private static final String TAG = "NewsAddFragment";
    private DatabaseReference myRef;
    private EditText editText;
    private Button save;
    private View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_news_add,container,false);
        myRef = FirebaseDatabase.getInstance().getReference().child("WELCOME_TEXT");
        initViews();
        save.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewsAddFragment.this.updatenews();
            }
        });



        return v;
    }
    private void initViews(){
    editText= v.findViewById (R.id.marqueetext);
    save= v.findViewById (R.id.btn_add);
    }
    private void updatenews(){
        String newsid = myRef.push().getKey();
        String newstext = editText.getText ().toString ();
        News news= new News(newsid, newstext);
        myRef.setValue(news);
        Toast.makeText(getContext(), "News Updated", Toast.LENGTH_LONG).show();

    }
    @Override
    public void onResume() {
        super.onResume();
    }

}
