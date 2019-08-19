package my.app.p4ulibrary.book_cornor;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import my.app.p4ulibrary.main_menu.HomeFragment;
import my.app.p4ulibrary.R;
import my.app.p4ulibrary.classes.Book;
import my.app.p4ulibrary.home_for_all.BookList;

public class AddBookFragment extends HomeFragment {
    private EditText etBookId,etBookAuthor,etBookTitle,etBookCost,etDonor,etDonorMobile,etBookLocation;
    private Button btnAddBook;
    private Spinner spBookSubject,spBookYear;
    private ListView listViewBooks;
    private List<Book> books;
    private View v;

    private TextView dBookid,dAuthor,dTitle,dCost,dDonor,dDonorMobile,dLocation,dYear,dSubject,dDonorTime,dIssueTo,dIssueTime;
    private Button dBack;
    private View dialogView;

    private DatabaseReference databaseBooks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_add_book, container, false);
        databaseBooks = FirebaseDatabase.getInstance().getReference("books");
        initViews();
        books = new ArrayList<>();
        btnAddBook.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                addBooks();
            }
        });
        listViewBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Book book = books.get(i);
                showDialog(
                        book.getBookId(),
                        book.getBookTitle(),
                        book.getBookCost(),
                        book.getBookAuthor(),
                        book.getBookYear(),
                        book.getBookSubject(),
                        book.getBookAvaibility(),
                        book.getBookLocation(),
                        book.getBookDonor(),
                        book.getBookDonorMobile(),
                        book.getBookDonorTime(),
                        book.getBookHandoverTo(),
                        book.getBookHandoverTime());
            }
        });
        return v;
    }

    private void addBooks() {

        //getting the values to save
        String bookYear,bookSubject;
        String bookId=etBookId.getText ().toString ().toUpperCase ().trim ();
        String bookTitle = etBookTitle.getText().toString().toUpperCase().trim();
        String bookCost = etBookCost.getText().toString().trim();
        String bookAuthor= etBookAuthor.getText().toString().toUpperCase().trim();
        String bookDonor= etDonor.getText().toString().toUpperCase().trim();
        String bookDonorMobile= etDonorMobile.getText().toString().trim();
        if(spBookYear.getSelectedItem ()!=null) {
            bookYear = spBookYear.getSelectedItem().toString();

        }
        else {
            bookYear="N/A";

        }
        if(spBookSubject.getSelectedItem ()!=null)
        {
            bookSubject = spBookSubject.getSelectedItem().toString();
        }
        else
        {
            bookSubject="N/A";
        }
        String bookAvaibility="1";
        String bookLocation=etBookLocation.getText().toString().toUpperCase().trim ();
        String bookDonorTime=get_current_time();
        String bookHandoverTo="CENTER";
        String bookHandoverTime=get_current_time();
        //checking if the value is provided
        if ((!TextUtils.isEmpty(bookId)) &&
                (!TextUtils.isEmpty(bookTitle))&&
                (!TextUtils.isEmpty(bookCost))&&
                        (!TextUtils.isEmpty(bookAuthor))&&
                                (!TextUtils.isEmpty(bookDonor))&&
                                        (!TextUtils.isEmpty(bookDonorMobile))&&
                                                (!TextUtils.isEmpty(bookLocation))&&
                                                    (!TextUtils.isEmpty(bookYear))&&
                                                            (!TextUtils.isEmpty(bookSubject)))
        {

            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our Artist
            //String bookId = databaseBooks.push().getKey();

            //creating an Book Object
            Book book = new Book (
                    bookId,
                    bookTitle,
                    bookAuthor,
                    bookSubject,
                    bookYear,
                    bookCost,
                    bookAvaibility,
                    bookLocation,
                    bookDonor,
                    bookDonorMobile,
                    bookDonorTime,
                    bookHandoverTo,
                    bookHandoverTime);

            //Saving the Book
            databaseBooks.child(Objects.requireNonNull(bookId)).setValue(book);

            //setting edittext to blank again
            etBookId.setText ("");
            etBookTitle.setText("");
            etBookAuthor.setText("");
            etBookCost.setText("");
            etBookLocation.setText("");
            etDonorMobile.setText("");
            etDonor.setText("");


            //displaying a success toast
          Toast.makeText(getContext(), "Book added", Toast.LENGTH_LONG).show();
        } else {
            //if the value is not given displaying a toast
            Toast.makeText(getContext(), "Please enter a name", Toast.LENGTH_LONG).show();
        }
    }
    public void onStart() {

        super.onStart();
        databaseBooks.addValueEventListener(new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //clearing the previous artist list
                books.clear();
                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Book book = postSnapshot.getValue(Book.class);
                    //adding artist to the list
                    books.add(book);

                }

                //creating adapter
                BookList bookAdapter = new BookList(getActivity(), books);
                //attaching adapter to the listview
                listViewBooks.setAdapter(bookAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @SuppressLint("InflateParams")
    private void showDialog(final String dbookId,
                                        final String dbookTitle,
                                        final String dbookAuthor,
                                        final String dbookSubject,
                                        final String dbookYear,
                                        final String dbookCost,
                                        final String dbookAvaibility,
                                        final String dbookLocation,
                                        final String dbookDonor,
                                        final String dbookDonorMobile,
                                        final String dbookDonorTime,
                                        final String dbookHandoverTo,
                                        final String dbookHandoverTime) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.show_book_dialog, null);
        dialogBuilder.setView(dialogView);
        init_dialog_views();
        dialogBuilder.setTitle("Book Record");
        final AlertDialog b = dialogBuilder.create();
        b.show();
        dBookid.setText (dbookId);
        dAuthor.setText(dbookSubject);///field missmath
        dCost.setText(dbookAuthor);//mismatch
        dTitle.setText(dbookTitle);
        dLocation.setText(dbookLocation);
        dDonor.setText(dbookDonor);
        dDonorMobile.setText(dbookDonorMobile);
        dYear.setText(dbookYear);
        dSubject.setText(dbookCost);//mismatch
        dIssueTo.setText(dbookHandoverTo);
        dIssueTime.setText(dbookHandoverTime);
        dDonorTime.setText(dbookDonorTime);
        dBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.cancel();
            }
        });


    }


    private void initViews(){
        etBookId=(EditText)v.findViewById (R.id.etBookId);
        etBookAuthor = (EditText) v.findViewById(R.id.etBookAuthor);
        etBookTitle = (EditText) v.findViewById(R.id.etBookTitle);
        etBookCost = (EditText) v.findViewById(R.id.etBookcost);
        etDonor = (EditText) v.findViewById(R.id.etDonor);
        etDonorMobile = (EditText) v.findViewById(R.id.etDonorMobile);
        etBookLocation = (EditText) v.findViewById(R.id.etBookLocation);
        spBookSubject = (Spinner) v.findViewById(R.id.spBookSubject);
        spBookYear = (Spinner) v.findViewById(R.id.spBookYear);
        listViewBooks = (ListView) v.findViewById(R.id.listViewBooks);
        btnAddBook = (Button) v.findViewById(R.id.btnAddBook);
    }
    private void init_dialog_views(){
        dBookid=(TextView)dialogView.findViewById (R.id.tvBookid);
        dTitle = (TextView) dialogView.findViewById(R.id.tvTitle);
        dCost = (TextView) dialogView.findViewById(R.id.tvCost);
        dAuthor = (TextView) dialogView.findViewById(R.id.tvAuthor);
        dYear = (TextView) dialogView.findViewById(R.id.tvYear);
        dSubject = (TextView) dialogView.findViewById(R.id.tvSubject);
        dLocation = (TextView) dialogView.findViewById(R.id.tvLocation);
        dDonor = (TextView) dialogView.findViewById(R.id.tvDonor);
        dDonorMobile = (TextView) dialogView.findViewById(R.id.tvDonorMobile);
        dDonorTime = (TextView) dialogView.findViewById(R.id.tvDonateTime);
        dIssueTo = (TextView) dialogView.findViewById(R.id.tvBookIssueto);
        dIssueTime = (TextView) dialogView.findViewById(R.id.tvIssueTime);
        dBack=(Button)dialogView.findViewById(R.id.dbuttonBack);
    }

    private String get_current_time(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss");
        return sdf.format(new Date());
    }

    private void switchFragment(Fragment fragment) {

        // mDrawerLayout.closeDrawer(mDrawerList);
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack("my_fragment").commit();
    }
    @Override
    public void onResume() {
        super.onResume();
    }

}
