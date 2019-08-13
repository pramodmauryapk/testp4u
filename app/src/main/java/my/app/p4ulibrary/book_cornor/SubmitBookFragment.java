package my.app.p4ulibrary.book_cornor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import my.app.p4ulibrary.main_menu.HomeFragment;
import my.app.p4ulibrary.R;
import my.app.p4ulibrary.classes.Book;
import my.app.p4ulibrary.home_for_all.BookList;

public class SubmitBookFragment extends HomeFragment {

    private ListView listViewBooks;
    private List<Book> books;
    FirebaseHelper helper;
    private DatabaseReference databaseBooks;
    private View v;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_submit_book,container,false);
        databaseBooks = FirebaseDatabase.getInstance().getReference("books");
        initViews();

        books = new ArrayList<>();
       // helper=new FirebaseHelper(databaseBooks);
       // spBookAuthor.setAdapter(new ArrayAdapter<String>(getContext(),R.layout.support_simple_spinner_dropdown_item,helper.retrieve()));
        //attaching listener to listview
    /*    listViewBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterV sp.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,helper.retrieve()));iew, View view, int i, long l) {
                Book book = books.get(i);
                showUpdateDeleteDialog(book.getBookId(), book.getBookTitle(),book.getBookCost(),book.getBookAuthor(),book.getBookYear(),book.getBookSubject(),book.getBookAvaibility());
            }
        });
*/


        return v;
    }
    private void initViews(){
        Spinner spBookSubject = (Spinner) v.findViewById(R.id.sp_Book_Subject);
        Spinner spBookName = (Spinner) v.findViewById(R.id.sp_Book_Name);
        Spinner spBookAuthor = (Spinner) v.findViewById(R.id.sp_Book_Author);
        Spinner spBookCost = (Spinner) v.findViewById(R.id.sp_Book_Cost);
        listViewBooks = (ListView) v.findViewById(R.id.view_list);
        Button btnAddBook = (Button) v.findViewById(R.id.btn_check);
    }
    public void onStart() {

        super.onStart();
        databaseBooks.addValueEventListener(new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                books.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Book book = postSnapshot.getValue(Book.class);
                    books.add(book);

                }
                BookList bookAdapter = new BookList(getActivity(), books);
                listViewBooks.setAdapter(bookAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
