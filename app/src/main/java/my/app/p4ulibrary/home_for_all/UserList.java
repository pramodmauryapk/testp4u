package my.app.p4ulibrary.home_for_all;

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

import my.app.p4ulibrary.R;
import my.app.p4ulibrary.classes.User;

public class UserList extends ArrayAdapter<User> {
    private Activity context;
    private List<User> users;

   public UserList(Activity context, List<User> users) {
        super(context, R.layout.layout_all_user, users);
        this.context = context;
        this.users = users;
    }


    @Override
    @NonNull
    public View  getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        User user = users.get(position);
        @SuppressLint({"ViewHolder", "InflateParams"}) View listViewItem = inflater.inflate(R.layout.layout_all_user, null, true);

            if (position % 2 == 1) {
                listViewItem.setBackgroundColor(Color.CYAN);
            } else {
                listViewItem.setBackgroundColor(Color.YELLOW);
            }
            TextView textViewName = (TextView) listViewItem.findViewById(R.id.tv_username);
            TextView textViewRole = (TextView) listViewItem.findViewById(R.id.tv_userrole);
            TextView textViewMobile = (TextView) listViewItem.findViewById(R.id.tv_usermobile);


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