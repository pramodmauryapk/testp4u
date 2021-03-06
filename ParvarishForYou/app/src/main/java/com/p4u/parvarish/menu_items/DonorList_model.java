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

import java.util.List;

public class DonorList_model extends ArrayAdapter<Donor> {
    private static final String TAG = DonorList_model.class.getSimpleName();

    private Activity context;
    private List<Donor> books;

     DonorList_model(Activity context, List<Donor> books) {
        super(context, R.layout.layout_all_donor, books);
        this.context = context;
        this.books = books;
    }


    @Override
    @NonNull
    public View getView(int position, View convertView,@NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"}) View listViewItem = inflater.inflate(R.layout.layout_all_donor, null, true);

        TextView textViewDonorName = listViewItem.findViewById(R.id.textView_DonorName);
        TextView textViewDonorMobile = listViewItem.findViewById(R.id.textView_DonorMobile);
        Donor book = books.get(position);
        textViewDonorName.setText(book.getBookDonor());
        textViewDonorMobile.setText(book.getBookDonorMobile());

        return listViewItem;
    }
}