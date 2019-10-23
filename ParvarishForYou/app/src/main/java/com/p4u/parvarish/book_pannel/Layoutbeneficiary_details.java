package com.p4u.parvarish.book_pannel;

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
import com.p4u.parvarish.user_pannel.TempUser;

import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class Layoutbeneficiary_details extends ArrayAdapter<TempUser> {
    private Activity context;
    private List<TempUser> users;

     Layoutbeneficiary_details(Activity context, List<TempUser> users) {
        super(context, R.layout.layout_beneficiary, users);
        this.context = context;
        this.users = users;
    }



    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"}) View listViewItem = inflater.inflate(R.layout.layout_beneficiary, null, true);
        try {


            TextView textViewName = listViewItem.findViewById(R.id.textView_BookTitle);
            TextView textViewEmail = listViewItem.findViewById(R.id.textView_Book_Author);
            TextView textViewMobile= listViewItem.findViewById(R.id.tvbookidd);
            TextView textViewAddress = listViewItem.findViewById(R.id.textView_Book_Cost);


            TempUser user = users.get(position);

            textViewName.setText(user.getName());
            textViewEmail.setText(user.getEmail());
            textViewMobile.setText(user.getMobile());
            textViewAddress.setText(user.getAddress());


        }
        catch (Exception e){
            Log.d(TAG,"Cannot Fatch record");
        }
        return listViewItem;
    }
}