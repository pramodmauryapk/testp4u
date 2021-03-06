package com.p4u.parvarish.user_pannel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.p4u.parvarish.R;

import java.util.List;

public class UserList_model extends ArrayAdapter<Teacher> {
    private static final String TAG =UserList_model.class.getSimpleName();

    private Activity context;
    private List<Teacher> users;

   UserList_model(Activity context, List<Teacher> users) {
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

      /*      if (position % 2 == 1) {
                listViewItem.setBackgroundColor(Color.CYAN);
            } else {
                listViewItem.setBackgroundColor(Color.YELLOW);
            }*/
      try {
          TextView textViewName = listViewItem.findViewById(R.id.tv_username);
          TextView textViewRole = listViewItem.findViewById(R.id.tv_userrole);
          TextView textViewMobile = listViewItem.findViewById(R.id.tv_usermobile);
          textViewName.setText(user.getUserName());
          textViewRole.setText(user.getUserRole());
          textViewMobile.setText(user.getUserMobile());
      }catch (Exception e){
          Log.d(TAG,"Cannot fetch data");
      }




        return listViewItem;

    }

}