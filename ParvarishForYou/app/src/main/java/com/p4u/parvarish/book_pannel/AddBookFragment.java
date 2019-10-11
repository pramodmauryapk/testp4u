package com.p4u.parvarish.book_pannel;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.p4u.parvarish.R;
import com.p4u.parvarish.user_pannel.Teacher;

import static java.util.Objects.requireNonNull;

public class AddBookFragment extends Fragment {
    private static final String TAG = "AddBookFragment";
    private EditText etBookId,etBookAuthor,etBookTitle,etBookCost,etDonor,etDonorMobile;
    private Spinner spBookLocation;
    private Button btnAddBook;
    private Spinner spBookSubject,spBookYear;
    private ListView listViewBooks;
    private List<Book> books;
    private View v;
    private TextView dBookid,dAuthor,dTitle,dCost,dDonor,dDonorMobile,dLocation,dYear,dSubject,dDonorTime,dIssueTo,dIssueTime;
    private Button dBack;
    private View dialogView;
    private String UserID;
    private TextInputLayout tf1,tf2,tf3,tf4,tf5,tf6,tf7;
    private DatabaseReference databaseBooks;
    private String booklocation;
    private String bookYear,bookSubject,bookId,bookTitle,bookCost,bookAuthor,bookDonor,bookDonorMobile,bookAvaibility,bookLocation,bookDonorTime,bookHandoverTo,bookHandoverTime,b;
    private int t=0,k=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_book_add, container, false);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference myRef = mFirebaseDatabase.getReference().child("USERS");
        UserID = (requireNonNull(user)).getUid();
        databaseBooks = FirebaseDatabase.getInstance().getReference().child("BOOKS");
        initViews();
        etBookTitle.requestFocus();
        etBookId.setEnabled(false);
        spBookLocation.setEnabled(false);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                show(ds);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        books = new ArrayList<>();
        btnAddBook.setOnClickListener(new View.OnClickListener() {
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

    @SuppressLint("SetTextI18n")
    private void show(DataSnapshot dataSnapshot){
        String location=null;
        try {
            ArrayList<String> list = new ArrayList<>();
            for (DataSnapshot ds : dataSnapshot.getChildren ()) {

                Teacher uInfo=ds.getValue(Teacher.class);

                if(requireNonNull(uInfo).getUserRole().equals("ADMIN")){
                    etBookId.setEnabled(true);
                    spBookLocation.setEnabled(true);
                }
                if(requireNonNull(uInfo).getUserId().equals(UserID)) {

                   booklocation= uInfo.getUserAddress().substring(0,3);
                   location=uInfo.getUserAddress();
                   list.add(location);


                }



            }



        }catch (Exception e){
            Log.d(TAG, "Failed to retrive values"+e);
        }
    }

    private void addBooks() {

        //setting the values to save
        set_values();
        //validating
        boolean ans=validate();
        if(ans)
        {

            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our Book
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
            databaseBooks.child(requireNonNull(bookId)).setValue(book);
            Toast.makeText(getContext(), "Book added", Toast.LENGTH_LONG).show();
            //setting edittext to blank again
            set_blank();

        } else {
            Toast.makeText(getContext(), "fill All required fields ", Toast.LENGTH_LONG).show();
        }
    }

    private boolean validate() {

        if (!TextUtils.isEmpty(bookTitle)) {
            tf2.setError("Enter Book Title");
            return false;
        }
        else if (!TextUtils.isEmpty(bookCost)) {
            tf3.setError("Enter Book Cost");
            return false;
        }
        else if (!TextUtils.isEmpty(bookAuthor)) {
            tf4.setError("Enter Book Author");
            return false;
        }
        else if (!TextUtils.isEmpty(bookDonor)) {
            tf5.setError("Enter Book Donor Name");
            return false;
        }
        else if (bookDonorMobile.length() != 10) {
            tf6.setError("Enter Book Donor Mobile");
            return false;
        }

        else

        return true;
    }

    private void set_values() {

        bookId=etBookId.getText ().toString ().toUpperCase ().trim ();
        bookTitle = etBookTitle.getText().toString().toUpperCase().trim();
        bookCost = etBookCost.getText().toString().trim();
        bookAuthor= etBookAuthor.getText().toString().toUpperCase().trim();
        bookDonor= etDonor.getText().toString().toUpperCase().trim();
        bookDonorMobile= etDonorMobile.getText().toString().trim();
        if(spBookYear.getSelectedItem()!=null) {
            bookYear = spBookYear.getSelectedItem().toString();
        }else{
            bookYear="N/A";
        }
        if(spBookSubject.getSelectedItem()!=null) {
            bookSubject = spBookSubject.getSelectedItem().toString();
        }else{
            bookSubject="N/A";
        }
        bookAvaibility="1";
        bookLocation=spBookLocation.getSelectedItem().toString();
        bookDonorTime=get_current_time();
        bookHandoverTo="CENTER";
        bookHandoverTime=get_current_time();

    }
    private void set_blank(){

        etBookTitle.setText("");
        etBookAuthor.setText("");
        etBookCost.setText("");

        etDonorMobile.setText("");
        etDonor.setText("");

    }
    public void onStart() {

        super.onStart();
        databaseBooks.addValueEventListener(new ValueEventListener () {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                books.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Book book = postSnapshot.getValue(Book.class);
                    books.add(book);
                }
                LayoutBookList bookAdapter = new LayoutBookList(getActivity(), books);
                listViewBooks.setAdapter(bookAdapter);
                t=bookAdapter.getCount();
                etBookId.setText(booklocation+(t+1));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @SuppressLint("InflateParams")
    private void showDialog(final String dbookId,
                                        final String dbookTitle,
                                        final String dbookCost,
                                        final String dbookAuthor,
                                        final String dbookYear,
                                        final String dbookSubject,
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
        dAuthor.setText(dbookAuthor);
        dCost.setText(dbookCost);
        dTitle.setText(dbookTitle);
        dLocation.setText(dbookLocation);
        //dDonor.setText(dbookDonor);
        //dDonorMobile.setText(dbookDonorMobile);
        dYear.setText(dbookYear);
        dSubject.setText(dbookSubject);
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
        etBookId=v.findViewById (R.id.etBookId);
        etBookAuthor = v.findViewById(R.id.etBookAuthor);
        etBookTitle = v.findViewById(R.id.etBookTitle);
        etBookCost =  v.findViewById(R.id.etBookcost);
        etDonor = v.findViewById(R.id.etDonor);
        etDonorMobile = v.findViewById(R.id.etDonorMobile);
        spBookLocation =  v.findViewById(R.id.etBookLocation);
        spBookSubject =  v.findViewById(R.id.spBookSubject);
        spBookYear =  v.findViewById(R.id.spBookYear);

        tf1=v.findViewById(R.id.tf1);
        tf2=v.findViewById(R.id.tf2);
        tf3=v.findViewById(R.id.tf3);
        tf4=v.findViewById(R.id.tf4);
        tf6=v.findViewById(R.id.tf6);
        tf7=v.findViewById(R.id.tf7);

        listViewBooks = v.findViewById(R.id.listViewBooks);
        btnAddBook = v.findViewById(R.id.btnAddBook);
    }
    private void init_dialog_views(){
        dBookid=dialogView.findViewById (R.id.tvBookid);
        dTitle =  dialogView.findViewById(R.id.tvTitle);
        dCost =  dialogView.findViewById(R.id.tvCost);
        dAuthor = dialogView.findViewById(R.id.tvAuthor);
        dYear =  dialogView.findViewById(R.id.tvYear);
        dSubject = dialogView.findViewById(R.id.tvSubject);
        dLocation = dialogView.findViewById(R.id.tvLocation);
        dDonor =  dialogView.findViewById(R.id.tvDonor);
        dDonorMobile = dialogView.findViewById(R.id.tvDonorMobile);
        dDonorTime = dialogView.findViewById(R.id.tvDonateTime);
        dIssueTo = dialogView.findViewById(R.id.tvBookIssueto);
        dIssueTime = dialogView.findViewById(R.id.tvIssueTime);
        dBack=dialogView.findViewById(R.id.dbuttonBack);
    }

    private String get_current_time(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss");
        return sdf.format(new Date());
    }




}
