package com.p4u.parvarish.menu_items;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.p4u.parvarish.R;

import java.util.Objects;

public class FeedbackAddFragment extends Fragment {
    private static final String TAG = "FeedbackAddFragment";

    private EditText editText;
    private TextView textView;
    private Button button;
    private View v;
    private RatingBar ratingBar;
    private Context context;

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_news_add,container,false);

        context = container.getContext();
        initViews();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    final String userID = Objects.requireNonNull(user).getUid();
                    feedback_submit(userID);


                }else{
                    Toast.makeText(context, "Please Register", Toast.LENGTH_LONG).show();
                }
            }
        });
        textView.setText ("Enter Feedback ");
        button.setText ("Submit");
        ratingBar.setVisibility (View.VISIBLE);

        return v;
    }

    private void feedback_submit(String userID) {

        try{

            DatabaseReference myref = FirebaseDatabase.getInstance().getReference().child("USERS").child(userID);
            myref.child("userFeedback").setValue(editText.getText().toString());
            myref.child("userRating").setValue(String.valueOf(ratingBar.getRating()));
            Toast.makeText(context, "Feedback Submitted", Toast.LENGTH_LONG).show();

        }catch (Exception e){

            Toast.makeText(context, "Please Register", Toast.LENGTH_LONG).show();
        }

    }

    private void initViews(){
        editText= v.findViewById (R.id.marqueetext);
        textView= v.findViewById (R.id.txt1);
        button= v.findViewById (R.id.btn_add);
        ratingBar= v.findViewById (R.id.ratingBar);
    }


}
