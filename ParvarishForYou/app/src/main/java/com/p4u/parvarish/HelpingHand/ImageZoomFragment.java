package com.p4u.parvarish.HelpingHand;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.p4u.parvarish.R;
import com.p4u.parvarish.gallary.TouchImageView;
import com.squareup.picasso.Picasso;


public class ImageZoomFragment extends Fragment {

    private View v;
private TouchImageView img;
    private Context context;

    public ImageZoomFragment() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.zoomimagefragment, container, false);
        init();
        assert getArguments() != null;
        String url= getArguments().getString("PHOTO");
        Picasso.get()
                .load(url)
                .placeholder(R.drawable.placeholder)
                .fit()
                .centerInside()
                .into(img);
        return v;
    }

    private void init() {
        img=v.findViewById(R.id.image1);
    }




}
