package com.p4u.parvarish.video;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;

import com.p4u.parvarish.R;
import com.p4u.parvarish.video.youtubeplayer.player.AbstractYouTubePlayerListener;
import com.p4u.parvarish.video.youtubeplayer.player.YouTubePlayer;
import com.p4u.parvarish.video.youtubeplayer.player.YouTubePlayerInitListener;
import com.p4u.parvarish.video.youtubeplayer.player.YouTubePlayerView;

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private String[] videoIds;
    private Lifecycle lifecycle;

    RecyclerViewAdapter(String[] videoIds, Lifecycle lifecycle) {
        this.videoIds = videoIds;
        this.lifecycle = lifecycle;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        YouTubePlayerView youTubePlayerView = (YouTubePlayerView) LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        youTubePlayerView.getPlayerUIController().showFullscreenButton(false);
        lifecycle.addObserver(youTubePlayerView);

        return new ViewHolder(youTubePlayerView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.cueVideo(videoIds[position]);

    }

    @Override
    public int getItemCount() {
        return videoIds.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private YouTubePlayer youTubePlayer;
        private String currentVideoId;

        ViewHolder(YouTubePlayerView playerView) {
            super(playerView);

            playerView.initialize(new YouTubePlayerInitListener() {
                                             @Override
                                             public void onInitSuccess(@NonNull final YouTubePlayer initializedYouTubePlayer) {
                                                 initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
                                                     @Override
                                                     public void onReady() {
                                                         youTubePlayer = initializedYouTubePlayer;
                                                         youTubePlayer.cueVideo(currentVideoId, 0);
                                                     }
                                                 });
                                             }
                                         }, true
            );
        }

        void cueVideo(String videoId) {
            currentVideoId = videoId;

            if(youTubePlayer == null)
                return;

            youTubePlayer.cueVideo(videoId, 0);
        }
    }
}
