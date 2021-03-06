package com.p4u.parvarish.book_pannel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import com.p4u.parvarish.R;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class LayoutBookList extends ArrayAdapter<Book> {
    private Activity context;
    private List<Book> books;

     LayoutBookList(Activity context, List<Book> books) {
        super(context, R.layout.layout_all_book, books);
        this.context = context;
        this.books = books;
    }



    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"}) View listViewItem = inflater.inflate(R.layout.layout_all_book, null, true);
        try {

            TextView textViewid = listViewItem.findViewById(R.id.tvbookidd);
            TextView textViewAuthor = listViewItem.findViewById(R.id.textView_Book_Author);
            TextView textViewTitle = listViewItem.findViewById(R.id.textView_BookTitle);
            TextView textViewYear = listViewItem.findViewById(R.id.textView_BookYear);
            TextView textViewCost = listViewItem.findViewById(R.id.textView_Book_Cost);


            Book book = books.get(position);
            textViewid.setText(book.getBookId() + "-");
            textViewAuthor.setText(book.getBookAuthor());
            textViewTitle.setText(book.getBookTitle());
            textViewYear.setText(book.getBookYear());
            textViewCost.setText(book.getBookCost());


        }
        catch (Exception e){
            Log.d(TAG,"Cannot Fatch record");
        }
        return listViewItem;
    }
}