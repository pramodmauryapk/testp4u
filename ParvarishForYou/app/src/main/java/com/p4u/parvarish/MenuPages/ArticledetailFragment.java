package com.p4u.parvarish.MenuPages;

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
import com.p4u.parvarish.gallary.TouchImageView;
import com.p4u.parvarish.video.youtubeplayer.player.YouTubePlayer;
import com.p4u.parvarish.video.youtubeplayer.player.YouTubePlayerView;
import com.p4u.parvarish.video.youtubeplayer.player.playerUtils.FullScreenHelper;
import com.squareup.picasso.Picasso;

public class ArticledetailFragment extends Fragment {

    private static final String TAG = "OurWorkFragment";
    private EditText editText;
    private TextView textView;
    private Button button;
    private Context context;
    private YouTubePlayerView player;
    private FullScreenHelper fullScreenHelper;
    private YouTubePlayer youTubePlayer;
    private TextView mDescription;
    private TouchImageView mImage;
    private View v;
    private String videoId;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.activity_detail, container, false);
        fullScreenHelper = new FullScreenHelper();
        context = container.getContext();
        initViews();
        player.setVisibility(View.GONE);
        assert this.getArguments() != null;
        final String imageurl = this.getArguments().getString("Video");
        String desc=this.getArguments().getString("Description");

        Picasso.get()
                    .load(imageurl)
                    .placeholder(R.drawable.placeholder)
                    .into(mImage);
            mDescription.setText(desc);


        return v;
    }
    private void initViews(){
        player=v.findViewById(R.id.player1);
        mImage = v.findViewById(R.id.ivImage);
        mDescription =v. findViewById(R.id.tvDescription);
    }


}
