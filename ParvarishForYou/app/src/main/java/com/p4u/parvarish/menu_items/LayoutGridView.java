package com.p4u.parvarish.menu_items;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.p4u.parvarish.R;

import static java.util.Objects.requireNonNull;

public class LayoutGridView extends BaseAdapter {
    private Context mContext;
    private final String[] gridViewString;
    private final int[] gridViewImageId;

    LayoutGridView(Context context, String[] gridViewString, int[] gridViewImageId) {
        mContext = context;
        this.gridViewImageId = gridViewImageId;
        this.gridViewString = gridViewString;
    }

    @Override
    public int getCount() {
        return gridViewString.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("InflateParams")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View gridViewAndroid;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            new View(mContext);
            gridViewAndroid = requireNonNull(inflater).inflate(R.layout.menu_layout, null);
            TextView textViewAndroid = gridViewAndroid.findViewById(R.id.android_gridview_text);
            ImageView imageViewAndroid = gridViewAndroid.findViewById(R.id.android_gridview_image);
            // Get the TextView LayoutParams
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) textViewAndroid.getLayoutParams();

            // Set the width of TextView widget (item of GridView)
          //  params.width = getPixelsFromDPs((Activity) gridViewAndroid.getContext(), 100);

            // Set the TextView height (GridView item/row equal height)
          // params.height = getPixelsFromDPs((Activity) gridViewAndroid.getContext(),50);



            imageViewAndroid.getLayoutParams().height=100;
            imageViewAndroid.getLayoutParams().width=100;
     //       textViewAndroid.setLayoutParams(params);
            textViewAndroid.setText(gridViewString[i]);
            imageViewAndroid.setImageResource(gridViewImageId[i]);
        } else {
            gridViewAndroid = convertView;
        }

        return gridViewAndroid;
    }

}
