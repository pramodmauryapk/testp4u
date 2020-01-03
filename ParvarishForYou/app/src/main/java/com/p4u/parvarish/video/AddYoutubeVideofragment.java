package com.p4u.parvarish.video;

import android.content.Context;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.p4u.parvarish.R;

import java.util.Objects;

public class AddYoutubeVideofragment extends Fragment {

    private View v;
    private Context context;
    private static final String TAG = AddYoutubeVideofragment.class.getSimpleName();

    private EditText nameEditText;
    private EditText descriptionEditText;
    private DatabaseReference mDatabaseRef;
    private Button uploadBtn;
    public AddYoutubeVideofragment(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

       v = inflater.inflate(R.layout.upload_youtube_fragment, container, false);
        context = container.getContext();
        initViews();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("YOUTUBE_VIDEOS");
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   uploadURL();
            }
        });
        return v;
    }
    private void initViews(){
        uploadBtn = v.findViewById(R.id.uploadBtn);
        nameEditText =v.findViewById(R.id.nameEditText);
        descriptionEditText =v. findViewById ( R.id.descriptionEditText );


    }







    private void uploadURL() {
        if (!descriptionEditText.getText().equals("")&&!nameEditText.getText().equals("")) {
            String uploadId = mDatabaseRef.push().getKey();
            YoutubeVideo_Model upload = new YoutubeVideo_Model(nameEditText.getText().toString().trim(),
                                                             descriptionEditText.getText().toString(),
                  uploadId
            );
            mDatabaseRef.child(Objects.requireNonNull(uploadId)).setValue(upload);
            Toast.makeText(context, "URL UPLOADED", Toast.LENGTH_LONG).show();
            clearall();


        } else {
            Toast.makeText(context, "Enter Both", Toast.LENGTH_SHORT).show();
        }

    }

    private void clearall() {
        nameEditText.setText("");
        descriptionEditText.setText("");
    }
    //String url = "https://img.youtube.com/vi/"+{ID}+"/0.jpg";
//Glide.with(this).load(url).into(imageView);

}
