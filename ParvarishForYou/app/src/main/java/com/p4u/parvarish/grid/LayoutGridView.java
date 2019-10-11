package com.p4u.parvarish.grid;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.p4u.parvarish.R;
import com.p4u.parvarish.main_menu.MainActivity;
import com.p4u.parvarish.main_menu.UserMenuFragment;

import static java.util.Objects.requireNonNull;

public class LayoutGridView extends BaseAdapter {
    private Context mContext;
    private final String[] gridViewString;
    private final int[] gridViewImageId;

    public LayoutGridView(Context context, String[] gridViewString, int[] gridViewImageId) {
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View gridViewAndroid;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            gridViewAndroid = new View(mContext);
            gridViewAndroid = requireNonNull(inflater).inflate(R.layout.gridview_layout, null);
            TextView textViewAndroid = (TextView) gridViewAndroid.findViewById(R.id.android_gridview_text);
            ImageView imageViewAndroid = (ImageView) gridViewAndroid.findViewById(R.id.android_gridview_image);
            // Get the TextView LayoutParams
          //  LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) textViewAndroid.getLayoutParams();

            // Set the width of TextView widget (item of GridView)
     //       params.width = getPixelsFromDPs((Activity) gridViewAndroid.getContext(), 100);

            // Set the TextView height (GridView item/row equal height)
     //       params.height = getPixelsFromDPs((Activity) gridViewAndroid.getContext(),50);



            imageViewAndroid.getLayoutParams().height=100;
     //       textViewAndroid.setLayoutParams(params);
            textViewAndroid.setText(gridViewString[i]);
            imageViewAndroid.setImageResource(gridViewImageId[i]);
        } else {
            gridViewAndroid = (View) convertView;
        }

        return gridViewAndroid;
    }
    // Method for converting DP value to pixels
    public static int getPixelsFromDPs(Activity activity, int dps){
        Resources r = activity.getResources();
        int  px = (int) (TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dps, r.getDisplayMetrics()));
        return px;
    }

}
