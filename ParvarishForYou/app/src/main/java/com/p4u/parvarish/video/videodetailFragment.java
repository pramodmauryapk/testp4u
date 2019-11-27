package com.p4u.parvarish.video;

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
import com.p4u.parvarish.video.youtubeplayer.player.AbstractYouTubePlayerListener;
import com.p4u.parvarish.video.youtubeplayer.player.YouTubePlayer;
import com.p4u.parvarish.video.youtubeplayer.player.YouTubePlayerFullScreenListener;
import com.p4u.parvarish.video.youtubeplayer.player.YouTubePlayerInitListener;
import com.p4u.parvarish.video.youtubeplayer.player.YouTubePlayerView;

import java.net.MalformedURLException;
import java.net.URL;

public class videodetailFragment extends Fragment {

    private static final String TAG = "OurWorkFragment";
    private EditText editText;
    private TextView textView;
    private Button button;
    private Context context;
    private YouTubePlayerView player;
    private YouTubePlayer youTubePlayer;
    private TextView mDescription;
    private TouchImageView mImage;
    private View v;
    private String videoId;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.activity_detail, container, false);
        context = container.getContext();
        initViews();
        mImage.setVisibility(View.GONE);
        assert this.getArguments() != null;
        final String imageurl = this.getArguments().getString("Video");
        String desc=this.getArguments().getString("Description");
        try {
            videoId=extractYoutubeId(imageurl);
            player.initialize(new YouTubePlayerInitListener() {
                                  @Override
                                  public void onInitSuccess(@NonNull final YouTubePlayer initializedYouTubePlayer) {
                                      initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                                          @Override
                                          public void onReady() {
                                              youTubePlayer = initializedYouTubePlayer;

                                              youTubePlayer.loadVideo(videoId, 0);
                                              youTubePlayer.play();


                                          }
                                      });

                                  }
                              }, true
            );
            player.addFullScreenListener(new YouTubePlayerFullScreenListener() {
                @Override
                public void onYouTubePlayerEnterFullScreen() {
                    player.setRotation(90);
                    player.enterFullScreen();
                }

                @Override
                public void onYouTubePlayerExitFullScreen() {
                    player.setRotation(-90);
                    player.exitFullScreen();
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //player.cueVideo(Objects.requireNonNull(imageurl),0);
           /* Picasso.get()
                    .load(imageurl)
                    .placeholder(R.drawable.placeholder)
                    .into(mImage);*/
            mDescription.setText(desc);


        return v;
    }
    private void initViews(){
        player=v.findViewById(R.id.player1);
        mImage = v.findViewById(R.id.ivImage);
        mDescription =v. findViewById(R.id.tvDescription);
    }
    private String extractYoutubeId(String url) throws MalformedURLException {
        String query = new URL(url).getQuery();
        String[] param = query.split("&");
        String id = null;
        for (String row : param) {
            String[] param1 = row.split("=");
            if (param1[0].equals("v")) {
                id = param1[1];
            }
        }
        return id;
    }

    @Override
    public void onPause() {
        super.onPause();
        try{
        youTubePlayer.pause();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
