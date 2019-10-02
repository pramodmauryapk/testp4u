package com.p4u.parvarish.user_pannel;

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

public class UserList extends ArrayAdapter<Teacher> {
    private Activity context;
    private List<Teacher> users;

   public UserList(Activity context, List<Teacher> users) {
        super(context, R.layout.layout_all_user, users);
        this.context = context;
        this.users = users;
    }


    @Override
    @NonNull
    public View  getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        Teacher user = users.get(position);
        @SuppressLint({"ViewHolder", "InflateParams"}) View listViewItem = inflater.inflate(R.layout.layout_all_user, null, true);

            if (position % 2 == 1) {
                listViewItem.setBackgroundColor(Color.CYAN);
            } else {
                listViewItem.setBackgroundColor(Color.YELLOW);
            }
            TextView textViewName = listViewItem.findViewById(R.id.tv_username);
            TextView textViewRole = listViewItem.findViewById(R.id.tv_userrole);
            TextView textViewMobile = listViewItem.findViewById(R.id.tv_usermobile);


            textViewName.setText(user.getUserName());
            textViewRole.setText(user.getUserRole());
            textViewMobile.setText(user.getUserMobile());



    //    }
       // else
       // {
    //        listViewItem.setVisibility(View.GONE);

      //}

        return listViewItem;

    }

}