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
import my.app.p4ulibrary.classes.Book;

public class DonorList extends ArrayAdapter<Book> {
    private Activity context;
    private List<Book> books;

    public DonorList(Activity context, List<Book> books) {
        super(context, R.layout.layout_all_donor, books);
        this.context = context;
        this.books = books;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"}) View listViewItem = inflater.inflate(R.layout.layout_all_donor, null, true);
        if (position % 2 == 1) {
            listViewItem.setBackgroundColor(Color.YELLOW);
        } else {
            listViewItem.setBackgroundColor(Color.CYAN);
        }
        TextView textViewDonorName = (TextView) listViewItem.findViewById(R.id.textView_DonorName);
        TextView textViewDonorMobile = (TextView) listViewItem.findViewById(R.id.textView_DonorMobile);


        Book book = books.get(position);
        textViewDonorName.setText(book.getBookDonor());
        textViewDonorMobile.setText(book.getBookDonorMobile());

        return listViewItem;
    }
}