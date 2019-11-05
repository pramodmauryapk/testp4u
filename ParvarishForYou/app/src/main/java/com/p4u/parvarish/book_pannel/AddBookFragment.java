package com.p4u.parvarish.book_pannel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Objects;

import com.p4u.parvarish.R;
import com.p4u.parvarish.user_pannel.Teacher;

import static java.util.Objects.requireNonNull;

public class AddBookFragment extends Fragment {
    private static final String TAG = "AddBookFragment";
    private EditText etBookId,etBookAuthor,etBookTitle,etBookCost,etDonor,etDonorMobile;
    private Spinner spBookLocation;
    private Button btnAddBook,btnlistbook;
    private Spinner spBookSubject,spBookYear;
    private View v;
    private TextInputLayout tf2;
    private TextInputLayout tf3;
    private TextInputLayout tf4;
    private TextInputLayout tf6;
    private TextInputLayout tf7;
    private DatabaseReference databaseBooks;
    private List<Book> books;
    private String UserID,bookYear,bookSubject,bookId,bookTitle,bookCost,bookAuthor,bookDonor,bookDonorMobile,bookAvaibility,bookLocation;
    private String bookDonorTime,bookHandoverTo,bookHandoverTime,booklocation;
    private int t=0;
    private Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_book_add, container, false);

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("USERS");
        UserID = requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        databaseBooks = FirebaseDatabase.getInstance().getReference().child("BOOKS");
        context = container.getContext();
        //initilize views
        initViews();

        etBookTitle.requestFocus();
        etBookId.setEnabled(false);
        spBookLocation.setEnabled(false);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                load_spinner_items(ds);
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
        btnlistbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFragment(new BookListFragment());
            }
        });
        spBookLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                load_bookid(spBookLocation.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                load_bookid(spBookLocation.getItemAtPosition(0).toString());
            }

        });
        change_listner(etBookTitle,tf2);
        change_listner(etBookAuthor,tf4);
        change_listner(etBookCost,tf3);
        change_listner(etDonor,tf6);
        change_listner(etDonorMobile,tf7);
        return v;
    }
    private void load_bookid(final String location) {
        if (!location.isEmpty()) {
            databaseBooks.addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    books.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Book book = postSnapshot.getValue(Book.class);
                        if (Objects.requireNonNull(book).getBookLocation().contains(location)) {
                            books.add(book);
                        }


                    }
                    if (getActivity() != null) {
                        LayoutBookList bookAdapter = new LayoutBookList(getActivity(), books);
                        t = bookAdapter.getCount();
                        etBookId.setText(booklocation + (t + 1));

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }
    private void change_listner(final TextView v, final TextInputLayout til){



        v.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                til.setErrorEnabled(false);
            }

            public void beforeTextChanged(CharSequence s, int start,int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,int before, int count) {

            }

        });
    }


    @SuppressLint("SetTextI18n")
    private void load_spinner_items(DataSnapshot dataSnapshot){
        String location;
        try {
            ArrayList<String> list = new ArrayList<>();
            for (DataSnapshot ds : dataSnapshot.getChildren ()) {

                Teacher uInfo=ds.getValue(Teacher.class);

                if(requireNonNull(uInfo).getUserRole().equals("ADMIN")){
                    //etBookId.setEnabled(true);
                    spBookLocation.setEnabled(true);
                }
                if(requireNonNull(uInfo).getUserId().equals(UserID)) {

                    booklocation = uInfo.getUserAddress().substring(0, 3);
                    location=uInfo.getUserAddress();
                    list.add(location);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(requireNonNull(context), android.R.layout.simple_spinner_item, list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spBookLocation.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

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
            Toast.makeText(context, "Book added", Toast.LENGTH_LONG).show();
            //setting edittext to blank again
            set_blank();

        } else {
            Toast.makeText(context, "fill All required fields ", Toast.LENGTH_LONG).show();
        }
    }
    private boolean validate() {

        if (TextUtils.isEmpty(bookTitle)) {
            tf2.setError("Enter Book Title");

            return false;
        }
        if (TextUtils.isEmpty(bookCost)) {
            tf3.setError("Enter Book Cost");
            return false;
        }
        if (TextUtils.isEmpty(bookAuthor)) {
            tf4.setError("Enter Book Author");
            return false;
        }
        if (TextUtils.isEmpty(bookDonor)) {
            tf6.setError("Enter Book Donor Name");
            return false;
        }
        if (bookDonorMobile.length() != 10) {
            tf7.setError("Enter Book Donor Mobile");
            return false;
        }



        return true;
    }
    private void set_values() {

        bookId=etBookId.getText ().toString ().toUpperCase ().trim ();
        bookTitle = etBookTitle.getText().toString().toUpperCase();
        bookCost = etBookCost.getText().toString();
        bookAuthor= etBookAuthor.getText().toString().toUpperCase();
        bookDonor= etDonor.getText().toString().toUpperCase();
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


        tf2=v.findViewById(R.id.tf2);
        tf3=v.findViewById(R.id.tf3);
        tf4=v.findViewById(R.id.tf4);

        tf6=v.findViewById(R.id.tf6);
        tf7=v.findViewById(R.id.tf7);
        btnAddBook = v.findViewById(R.id.btnAddBook);
        btnlistbook=v.findViewById(R.id.btnlistBook);
    }

    private String get_current_time(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss");
        return sdf.format(new Date());
    }
    // switching fragment
    private void switchFragment(Fragment fragment) {
        if (getActivity() != null) {
            requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .addToBackStack(null).commit();
        }
    }



}
