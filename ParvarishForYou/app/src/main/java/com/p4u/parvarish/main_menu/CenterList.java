package com.p4u.parvarish.main_menu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import com.p4u.parvarish.R;
import com.p4u.parvarish.user_pannel.Teacher;

public class CenterList extends ArrayAdapter<Teacher> {
    private Activity context;
    private List<Teacher> users;

   CenterList(Activity context, List<Teacher> users) {
        super(context, R.layout.layout_all_center, users);
        this.context = context;
        this.users = users;
    }


    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"}) View listViewItem = inflater.inflate(R.layout.layout_all_center, null, true);
        if (position % 2 == 1) {
            listViewItem.setBackgroundColor(Color.YELLOW);
        } else {
            listViewItem.setBackgroundColor(Color.CYAN);
        }
        TextView textViewCenterName = listViewItem.findViewById(R.id.tv_centername);
        TextView textViewCenterMobile = listViewItem.findViewById(R.id.tv_centermobile);

        Teacher user = users.get(position);
        textViewCenterName.setText(user.getUserAddress());
        textViewCenterMobile.setText(user.getUserMobile());

        return listViewItem;
    }
}