package com.p4u.parvarish.classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import com.p4u.parvarish.R;

public class CropOptionAdapter extends ArrayAdapter<CropOption> {
    private static final String TAG = "CropOptionAdapter";
    private ArrayList<CropOption> mOptions;
    private LayoutInflater mInflater;

    CropOptionAdapter(Context context, ArrayList<CropOption> options) {
        super(context, R.layout.crop_selector, options);
        mOptions 	= options;
        mInflater	= LayoutInflater.from(context);
    }

    @SuppressLint("InflateParams")
    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup group) {
        if (convertView == null)
            convertView = mInflater.inflate(R.layout.crop_selector, null);

        CropOption item = mOptions.get(position);

        if (item != null) {
            ((ImageView) convertView.findViewById(R.id.iv_icon)).setImageDrawable(item.icon);
            ((TextView) convertView.findViewById(R.id.tv_name)).setText(item.title);

            return convertView;
        }

        return convertView;
    }
}