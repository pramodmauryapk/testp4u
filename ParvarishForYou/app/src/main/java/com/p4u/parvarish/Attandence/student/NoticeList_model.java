package com.p4u.parvarish.Attandence.student;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.p4u.parvarish.R;

import java.util.List;

public class NoticeList_model extends ArrayAdapter<NoticeData> {
    private static final String TAG = "leaveList_model";
    private Activity context;
    private List<NoticeData> users;

   public NoticeList_model(Activity context, List<NoticeData> users) {
        super(context, R.layout.layout_leave_item, users);
        this.context = context;
        this.users = users;
    }


    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"}) View listViewItem = inflater.inflate(R.layout.layout_notice_item, null, true);

        TextView textViewCenterName = listViewItem.findViewById(R.id.tv_centername);
        TextView textViewCenterMobile = listViewItem.findViewById(R.id.tv_centermobile);

        NoticeData user = users.get(position);
        textViewCenterName.setText(user.getNoticeid());
        textViewCenterMobile.setText(user.getNoticedescription());

        return listViewItem;
    }
}