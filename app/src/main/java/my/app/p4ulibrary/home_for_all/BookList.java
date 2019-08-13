package my.app.p4ulibrary.home_for_all;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.AnimatorRes;
import androidx.annotation.AnyRes;
import androidx.annotation.AnyThread;
import androidx.annotation.NonNull;

import com.google.auto.value.AutoAnnotation;
import com.google.auto.value.AutoValue;

import java.util.List;

import my.app.p4ulibrary.R;
import my.app.p4ulibrary.classes.Book;

public class BookList extends ArrayAdapter<Book> {
    private Activity context;
    private List<Book> books;

     public BookList(Activity context, List<Book> books) {
        super(context, R.layout.layout_all_book, books);
        this.context = context;
        this.books = books;
    }


    @SuppressLint("SetTextI18n")
    @Override
    @NonNull
    public View getView(int position, View convertView,@NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        @SuppressLint({"ViewHolder", "InflateParams"}) View listViewItem = inflater.inflate(R.layout.layout_all_book, null, true);

        if (position % 2 == 1) {
            listViewItem.setBackgroundColor(Color.YELLOW);
        } else {
            listViewItem.setBackgroundColor(Color.CYAN);
        }
        TextView textViewid = (TextView) listViewItem.findViewById(R.id.tvbookidd);
        TextView textViewAuthor = (TextView) listViewItem.findViewById(R.id.textView_Book_Author);
        TextView textViewTitle = (TextView) listViewItem.findViewById(R.id.textView_BookTitle);
        TextView textViewYear = (TextView) listViewItem.findViewById(R.id.textView_BookYear);
        TextView textViewCost = (TextView) listViewItem.findViewById(R.id.textView_Book_Cost);

        Book book = books.get(position);
        textViewid.setText (book.getBookId ()+"-");
        textViewAuthor.setText(book.getBookAuthor());
        textViewTitle.setText(book.getBookTitle());
        textViewYear.setText(book.getBookYear());
        textViewCost.setText(book.getBookCost());
        return listViewItem;
    }
}