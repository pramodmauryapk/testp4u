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
import com.p4u.parvarish.admin_pannel.Book;

public class DonorList_model extends ArrayAdapter<Book> {
    private static final String TAG = "DonorList_model";
    private Activity context;
    private List<Book> books;

     DonorList_model(Activity context, List<Book> books) {
        super(context, R.layout.layout_all_donor, books);
        this.context = context;
        this.books = books;
    }


    @Override
    @NonNull
    public View getView(int position, View convertView,@NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"}) View listViewItem = inflater.inflate(R.layout.layout_all_donor, null, true);
        if (position % 2 == 1) {
            listViewItem.setBackgroundColor(Color.YELLOW);
        } else {
            listViewItem.setBackgroundColor(Color.CYAN);
        }
        TextView textViewDonorName = listViewItem.findViewById(R.id.textView_DonorName);
        TextView textViewDonorMobile = listViewItem.findViewById(R.id.textView_DonorMobile);


        Book book = books.get(position);
        textViewDonorName.setText(book.getBookDonor());
        textViewDonorMobile.setText(book.getBookDonorMobile());

        return listViewItem;
    }
}