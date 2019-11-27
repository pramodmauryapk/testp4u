package com.p4u.parvarish.gallary;

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
import com.p4u.parvarish.video.youtubeplayer.player.YouTubePlayerView;
import com.squareup.picasso.Picasso;

public class imagedetailFragment extends Fragment {

    private static final String TAG = "OurWorkFragment";
    private EditText editText;
    private TextView textView;
    private Button button;
    private Context context;
    private TouchImageView mImage;
    private TextView mDescription;
    private YouTubePlayerView player;
    private View v;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.activity_detail, container, false);
        context = container.getContext();
        initViews();
        player.setVisibility(View.GONE);

        assert this.getArguments() != null;
        String imageurl = this.getArguments().getString("Image");
        String desc=this.getArguments().getString("Description");


            Picasso.get()
                    .load(imageurl)
                    .placeholder(R.drawable.placeholder)
                    .into(mImage);
            mDescription.setText(desc);

        return v;
    }
    private void initViews(){
        mImage = v.findViewById(R.id.ivImage);
        player=v.findViewById(R.id.player1);
        mDescription =v. findViewById(R.id.tvDescription);

    }

}
