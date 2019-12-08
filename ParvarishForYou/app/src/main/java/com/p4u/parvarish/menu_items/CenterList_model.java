package com.p4u.parvarish.menu_items;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.p4u.parvarish.R;
import com.p4u.parvarish.user_pannel.Teacher;

import java.util.List;

public class CenterList_model extends ArrayAdapter<Teacher> {
    private static final String TAG = "CenterList_model";
    private Activity context;
    private List<Teacher> users;

   CenterList_model(Activity context, List<Teacher> users) {
        super(context, R.layout.layout_all_center, users);
        this.context = context;
        this.users = users;
    }


    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"}) View listViewItem = inflater.inflate(R.layout.layout_all_center, null, true);

        TextView textViewCenterName = listViewItem.findViewById(R.id.tv_centername);
        TextView textViewCenterMobile = listViewItem.findViewById(R.id.tv_centermobile);

        Teacher user = users.get(position);
        textViewCenterName.setText(user.getUserAddress());
        textViewCenterMobile.setText(user.getUserMobile());

        return listViewItem;
    }
}