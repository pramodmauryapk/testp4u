package com.p4u.parvarish.HelpLine;

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

public class HelplineList_model extends ArrayAdapter<Helpline> {
    private static final String TAG = HelplineList_model.class.getSimpleName();

    private Activity context;
    private List<Helpline> users;

   public HelplineList_model(Activity context, List<Helpline> users) {
        super(context, R.layout.layout_helpline_item, users);
        this.context = context;
        this.users = users;
    }


    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"}) View listViewItem = inflater.inflate(R.layout.layout_helpline_item, null, true);

        TextView textViewCenterName = listViewItem.findViewById(R.id.tv_centername);
        TextView textViewCenterMobile = listViewItem.findViewById(R.id.tv_centermobile);

        Helpline user = users.get(position);
        textViewCenterName.setText(user.getService());
        textViewCenterMobile.setText(user.getMobile());

        return listViewItem;
    }
}