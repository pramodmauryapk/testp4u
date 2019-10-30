package com.p4u.parvarish.menu_items;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.p4u.parvarish.R;
import com.squareup.picasso.Picasso;

public class ViewHolder extends RecyclerView.ViewHolder {

   View mView;



    public ViewHolder(@NonNull View itemView) {
        super(itemView);


        mView=itemView;
    }
    public void set_details(Context ctx,String title,String img){
        TextView textView=mView.findViewById(R.id.rTitleTv);
        ImageView imageView=mView.findViewById(R.id.rImageView);
        textView.setText(title);
        Picasso.get().load(img).into(imageView);
    }
}
