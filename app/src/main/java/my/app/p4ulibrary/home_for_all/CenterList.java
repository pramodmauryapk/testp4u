package my.app.p4ulibrary.home_for_all;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import my.app.p4ulibrary.R;
import my.app.p4ulibrary.classes.User;

public class CenterList extends ArrayAdapter<User> {
    private Activity context;
    private List<User> users;

    public CenterList(Activity context, List<User> users) {
        super(context, R.layout.layout_all_center, users);
        this.context = context;
        this.users = users;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"}) View listViewItem = inflater.inflate(R.layout.layout_all_center, null, true);
        if (position % 2 == 1) {
            listViewItem.setBackgroundColor(Color.YELLOW);
        } else {
            listViewItem.setBackgroundColor(Color.CYAN);
        }
        TextView textViewCenterName = (TextView) listViewItem.findViewById(R.id.tv_centername);
        TextView textViewCenterMobile = (TextView) listViewItem.findViewById(R.id.tv_centermobile);

        User user = users.get(position);
        textViewCenterName.setText(user.getUserAddress());
        textViewCenterMobile.setText(user.getUserMobile());

        return listViewItem;
    }
}