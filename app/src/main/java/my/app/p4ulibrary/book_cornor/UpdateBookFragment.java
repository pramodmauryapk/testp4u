package my.app.p4ulibrary.book_cornor;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

public class UpdateBookFragment extends HomeFragment {
    private ListView listViewBooks;
    private List<Book> books;

    private DatabaseReference databaseBooks;
    private EditText deditBookAuthor,deditBookTitle,deditBookCost,deditDonor,deditDonorMobile,deditBookLocation;
    private Spinner dspinnerYear,dspinnerSubject;
    private Button dbuttonUpdate,dbuttonDelete,dbuttonBack;
    private View dialogView;
    View v;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_update_book,container,false);
        databaseBooks = FirebaseDatabase.getInstance().getReference("books");
        //getting views
        initViews();

        books = new ArrayList<>();
        listViewBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Book book = books.get(i);
                showUpdateDeleteDialog(
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
    private void initViews(){

        listViewBooks = (ListView) v.findViewById(R.id.view_list);

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
    private void updateBook(String bookId,
                            String bookTitle,
                            String bookAuthor,
                            String bookSubject,
                            String bookYear,
                            String bookCost,
                            String bookAvaibility,
                            String bookLocation,
                            String bookDonor,
                            String bookDonorMobile,
                            String bookDonorTime,
                            String bookHandoverTo,
                            String bookHandoverTime) {
        //getting the specified artist reference
       // DatabaseReference dR = FirebaseDatabase.getInstance().getReference("books").child(bookId);
        //DatabaseReference dR = FirebaseDatabase.getInstance().getReference("books");

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

       // dR.setValue(book);
        databaseBooks.child(Objects.requireNonNull(bookId)).setValue(book);
        Toast.makeText(getContext(), "Book Record Updated", Toast.LENGTH_LONG).show();
    }
    @SuppressLint("InflateParams")
    private void showUpdateDeleteDialog(final String dbookId,
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
        dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        init_dialog_views();

        dialogBuilder.setTitle("Update Book");
        final AlertDialog b = dialogBuilder.create();
        b.show();
        deditBookAuthor.setText(dbookSubject);
        deditBookCost.setText(dbookAuthor);

        deditBookTitle.setText(dbookTitle);
        deditBookLocation.setText(dbookLocation);
        deditDonor.setText(dbookDonor);
        deditDonorMobile.setText(dbookDonorMobile);
        dbuttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dbookYear,dbookSubject;
                String dbookAuthor = deditBookAuthor.getText().toString().toUpperCase().trim();
                String dbookTitle = deditBookTitle.getText().toString().toUpperCase().trim();
                String dbookCost = deditBookCost.getText().toString().trim();
                String dbookDonor= deditDonor.getText().toString().toUpperCase().trim();
                String dbookDonorMobile= deditDonorMobile.getText().toString().trim();
                if(dspinnerYear.getSelectedItem ()!=null) {
                    dbookYear = dspinnerYear.getSelectedItem ().toString ();
                }
                else
                {
                    dbookYear="N/A";
                }
                if(dspinnerSubject.getSelectedItem ()!=null){
                    dbookSubject = dspinnerSubject.getSelectedItem().toString().toUpperCase();
                }
                else
                {
                    dbookSubject = "N/A";
                }
                String dbookAvaibility="1";
                String dbookLocation=deditBookLocation.getText().toString().toUpperCase();
                String dbookDonorTime=get_current_time();
                String dbookHandoverTo="CENTER";
                String dbookHandoverTime=get_current_time();
                if (!TextUtils.isEmpty(dbookAuthor)&&
                        !TextUtils.isEmpty(dbookTitle)&&
                        !TextUtils.isEmpty(dbookSubject)&&
                        !TextUtils.isEmpty(dbookYear)&&
                        !TextUtils.isEmpty(dbookCost)&&
                        !TextUtils.isEmpty(dbookLocation)&&
                        !TextUtils.isEmpty(dbookDonor)&&
                        !TextUtils.isEmpty(dbookDonorMobile)) {
                    updateBook(dbookId,
                            dbookTitle,
                            dbookAuthor,
                            dbookSubject,
                            dbookYear,
                            dbookCost,
                            dbookAvaibility,
                            dbookLocation,
                            dbookDonor,
                            dbookDonorMobile,
                            dbookDonorTime,
                            dbookHandoverTo,
                            dbookHandoverTime);
                    b.dismiss();
                }

            }
        });


        dbuttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Objects.requireNonNull(getContext()));
                builder.setMessage("Are you sure you want to Delete Book?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteBook(dbookId);
                                dialog.cancel ();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();

                            }
                        });
                androidx.appcompat.app.AlertDialog alert = builder.create();
                alert.show();



            }
        });
        dbuttonBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                b.cancel ();
            }
        });
    }

    private void deleteBook(String book_id) {
        //getting the specified book reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("books").child(book_id);
        //removing book
        dR.removeValue();
        Toast.makeText(getContext(), "Book Deleted", Toast.LENGTH_LONG).show();

    }
    private void init_dialog_views(){

        deditBookAuthor = (EditText) dialogView.findViewById(R.id.detBookAuthor);
        deditBookTitle = (EditText) dialogView.findViewById(R.id.detBookTitle);
        deditBookCost = (EditText) dialogView.findViewById(R.id.detBookcost);
        deditDonor = (EditText) dialogView.findViewById(R.id.detDonor);
        deditDonorMobile = (EditText) dialogView.findViewById(R.id.detDonorMobile);
        deditBookLocation = (EditText) dialogView.findViewById(R.id.detBookLocation);
        dspinnerSubject = (Spinner) dialogView.findViewById(R.id.dspBookSubject);
        dspinnerYear = (Spinner) dialogView.findViewById(R.id.dspBookYear);
        dbuttonUpdate = (Button) dialogView.findViewById(R.id.dbuttonUpdate);
        dbuttonDelete = (Button) dialogView.findViewById(R.id.dbuttonDelete);
        dbuttonBack = (Button) dialogView.findViewById(R.id.dbuttonBack);
    }
    private String get_current_time(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss");
        return sdf.format(new Date());
    }
}