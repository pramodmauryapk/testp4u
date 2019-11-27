package com.p4u.parvarish.video;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.p4u.parvarish.R;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public  class youtubeRecyclerAdapter extends RecyclerView.Adapter<youtubeRecyclerAdapter.RecyclerViewHolder>{
    private static final String TAG = "RecyclerAdapter_model";
    private Context mContext;
    private List<YoutubeVideo_Model> imageModels;
    private OnItemClickListener mListener;
    private YoutubeVideo_Model currentImageModel;
    private Bundle bundle;
    private String videoId;

    youtubeRecyclerAdapter(Context context, List<YoutubeVideo_Model> uploads) {
        mContext = context;
        imageModels = uploads;
    }


    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_row_item, parent, false);


        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        currentImageModel = imageModels.get(position);

        holder.textView.setText(currentImageModel.getName());
        String url = currentImageModel.getVideoURL();

        try
        {
            videoId=extractYoutubeId(url);
            Log.e("VideoId is->","" + videoId);
            String img_url="https://img.youtube.com/vi/"+videoId+"/0.jpg";

             Picasso.get()
                    .load(img_url)
                    .placeholder(R.drawable.logo)
                    .fit()
                    .centerCrop()
                    .into(holder.teacherImageView);

        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return imageModels.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {


        ImageView teacherImageView;
        TextView textView;
        RecyclerViewHolder(final View itemView) {
            super(itemView);

            teacherImageView = itemView.findViewById(R.id.ivImage);
            textView=itemView.findViewById(R.id.tvTitle);
            textView.setVisibility(View.VISIBLE);
            itemView.setOnClickListener(this);

           // itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);

                }

            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem showItem = menu.add( Menu.NONE, 1, 1, "Show");
            MenuItem deleteItem = menu.add(Menu.NONE, 2, 2, "Delete");
            showItem.setOnMenuItemClickListener(this);
            deleteItem.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {

                    switch (item.getItemId()) {
                        case 1:
                            mListener.onShowItemClick(position);

                            return true;
                        case 2:
                            mListener.onDeleteItemClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onShowItemClick(int position);
        void onDeleteItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public String extractYoutubeId(String url) throws MalformedURLException {
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
}
