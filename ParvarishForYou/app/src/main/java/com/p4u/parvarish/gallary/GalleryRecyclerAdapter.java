package com.p4u.parvarish.gallary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.p4u.parvarish.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public  class GalleryRecyclerAdapter extends RecyclerView.Adapter<GalleryRecyclerAdapter.RecyclerViewHolder>{
    private static final String TAG = "userwiseAdapter_model";
    private Context mContext;
    private List<Image_Model> imageModels;
    private OnItemClickListener mListener;
    private Image_Model currentImageModel;
    private Bundle bundle;
    GalleryRecyclerAdapter(Context context, List<Image_Model> uploads) {
        mContext = context;
        imageModels = uploads;
    }


    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.gallery_row_item, parent, false);


        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        currentImageModel = imageModels.get(position);
       // holder.nameTextView.setText(currentImageModel.getName());
       // holder.descriptionTextView.setText(currentImageModel.getDescription());
       // holder.dateTextView.setText(getDateToday());
        Picasso.get()
                .load(currentImageModel.getImageUrl())
                .placeholder(R.drawable.logo)
                .fit()
                .centerCrop()
                .into(holder.teacherImageView);
    }

    @Override
    public int getItemCount() {
        return imageModels.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        //TextView nameTextView,descriptionTextView,dateTextView;
        ImageView teacherImageView;

        RecyclerViewHolder(final View itemView) {
            super(itemView);
            //nameTextView =itemView.findViewById ( R.id.nameTextView );
            //descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            //dateTextView = itemView.findViewById(R.id.dateTextView);
            teacherImageView = itemView.findViewById(R.id.ivImage);

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
    private String getDateToday(){
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd");
        Date date=new Date();
        return dateFormat.format(date);
    }

}
