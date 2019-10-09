package com.p4u.parvarish.admin_pannel;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import com.p4u.parvarish.R;

public class UpdateBookFragment extends Fragment {
    private static final String TAG = "UpdateBookFragment";
    private ListView listViewBooks;
    private List<Book> books;

    private DatabaseReference databaseBooks;
    private EditText deditBookAuthor,deditBookTitle,deditBookCost,deditDonor,deditDonorMobile,deditBookLocation;
    private Spinner dspinnerYear,dspinnerSubject;
    private EditText spBookSubject,spBookName,spBookAuthor,spBookCost;
    private TextView tv2;
    private Button dbuttonUpdate,dbuttonDelete,dbuttonBack;
    private View dialogView;
    private View v;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_book_search,container,false);
        databaseBooks = FirebaseDatabase.getInstance().getReference().child("BOOKS");
        initViews();
        tv2.setText("Tap to Book for Update");
        books = new ArrayList<>();
        listViewBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Book book = books.get(i);
                UpdateBookFragment.this.showUpdateDeleteDialog(
                        book.getBookId(),
                        book.getBookTitle(),
                        book.getBookAuthor(),
                        book.getBookSubject(),
                        book.getBookYear(),
                        book.getBookCost(),
                        book.getBookLocation(),
                        book.getBookDonor(),
                        book.getBookDonorMobile());
            }
        });
        //for search book from listview
        spBookAuthor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                onStart();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        spBookName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                onStart();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        spBookSubject.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                onStart();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        spBookCost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                onStart();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return v;
    }
    private void initViews(){
        spBookSubject = v.findViewById(R.id.sp_Book_Subject);
        spBookName =  v.findViewById(R.id.sp_Book_Name);
        spBookAuthor =  v.findViewById(R.id.sp_Book_Author);
        spBookCost =  v.findViewById(R.id.sp_Book_Cost);
        tv2=v.findViewById(R.id.tv2);
        listViewBooks = v.findViewById(R.id.view_list);

    }

    private boolean updateBook(String bookId,
                            String bookTitle,
                            String bookAuthor,
                            String bookSubject,
                            String bookYear,
                            String bookCost,
                            String bookLocation,
                            String bookDonor,
                            String bookDonorMobile) {


        databaseBooks = FirebaseDatabase.getInstance().getReference().child("BOOKS").child(bookId);
        databaseBooks.child("bookTitle").setValue(bookTitle);
        databaseBooks.child("bookAuthor").setValue(bookAuthor);
        databaseBooks.child("bookSubject").setValue(bookSubject);
        databaseBooks.child("bookYear").setValue(bookYear);
        databaseBooks.child("bookCost").setValue(bookCost);
        databaseBooks.child("bookLocation").setValue(bookLocation);
        databaseBooks.child("bookDonor").setValue(bookDonor);
        databaseBooks.child("bookDonorMobile").setValue(bookDonorMobile);
        return true;


    }
    @SuppressLint("InflateParams")
    private void showUpdateDeleteDialog(final String dbookId,
                                        final String dbookTitle,
                                        final String dbookAuthor,
                                        final String dbookSubject,
                                        final String dbookYear,
                                        final String dbookCost,
                                        final String dbookLocation,
                                        final String dbookDonor,
                                        final String dbookDonorMobile) {
        //setting alert dialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        final LayoutInflater inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Update Book");
        //initializing views
        init_dialog_views();
        final AlertDialog b = dialogBuilder.create();
        b.show();
        //setting values in views
        setting_values(dbookAuthor,dbookCost,dbookTitle,dbookLocation,dbookDonor,dbookDonorMobile);
        //update button
        dbuttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //setting values after update
                String Year, Subject;
                String Author = deditBookAuthor.getText().toString().toUpperCase().trim();
                String Title = deditBookTitle.getText().toString().toUpperCase().trim();
                String Cost = deditBookCost.getText().toString().trim();
                String Donor = deditDonor.getText().toString().toUpperCase().trim();
                String DonorMobile = deditDonorMobile.getText().toString().trim();
                if (dspinnerYear.getSelectedItem() != null) {
                    Year = dspinnerYear.getSelectedItem().toString();
                } else {
                    Year = "N/A";
                }
                if (dspinnerSubject.getSelectedItem() != null) {
                    Subject = dspinnerSubject.getSelectedItem().toString().toUpperCase();
                } else {
                    Subject = "N/A";
                }

                String Location = deditBookLocation.getText().toString().toUpperCase();

                //checking values
                boolean a=validate(Author,Title,Subject,Year,Cost,Location,Donor,DonorMobile);
                if (a) {
                   boolean ans= updateBook(dbookId,
                            Title,
                            Author,
                            Subject,
                            Year,
                            Cost,
                            Location,
                            Donor,
                            DonorMobile);
                   if(ans){
                       Toast.makeText(getContext(), "Book Record Updated", Toast.LENGTH_LONG).show();
                       b.dismiss();

                   }

                }

            }
        });


        dbuttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(Objects.requireNonNull(UpdateBookFragment.this.getContext()));
                builder.setMessage("Are you sure you want to Delete Book?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                deleteBook(dbookId);
                                dialog.cancel();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                androidx.appcompat.app.AlertDialog alert = builder.create();
                alert.show();


            }
        });
        dbuttonBack.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.cancel();
            }
        });
    }
private boolean validate(String dbookAuthor,String dbookTitle,String dbookSubject,String dbookYear,String dbookCost,String dbookLocation,String dbookDonor,String dbookDonorMobile){
    return !TextUtils.isEmpty(dbookAuthor) &&
            !TextUtils.isEmpty(dbookTitle) &&
            !TextUtils.isEmpty(dbookSubject) &&
            !TextUtils.isEmpty(dbookYear) &&
            !TextUtils.isEmpty(dbookCost) &&
            !TextUtils.isEmpty(dbookLocation) &&
            !TextUtils.isEmpty(dbookDonor) &&
            !TextUtils.isEmpty(dbookDonorMobile);

}
    private void setting_values(String dbookAuthor,String dbookCost,String dbookTitle,String dbookLocation,String dbookDonor,String dbookDonorMobile) {
        deditBookAuthor.setText(dbookAuthor);
        deditBookCost.setText(dbookCost);
        deditBookTitle.setText(dbookTitle);
        deditBookLocation.setText(dbookLocation);
        deditDonor.setText(dbookDonor);
        deditDonorMobile.setText(dbookDonorMobile);
    }

    private void deleteBook(String book_id) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference().child("books").child(book_id);
        dR.removeValue();
        Toast.makeText(getContext(), "Book Deleted", Toast.LENGTH_LONG).show();

    }
    private void init_dialog_views(){

        deditBookAuthor =  dialogView.findViewById(R.id.detBookAuthor);
        deditBookTitle = dialogView.findViewById(R.id.detBookTitle);
        deditBookCost =  dialogView.findViewById(R.id.detBookcost);
        deditDonor =  dialogView.findViewById(R.id.detDonor);
        deditDonorMobile =  dialogView.findViewById(R.id.detDonorMobile);
        deditBookLocation = dialogView.findViewById(R.id.detBookLocation);
        dspinnerSubject = dialogView.findViewById(R.id.dspBookSubject);
        dspinnerYear =  dialogView.findViewById(R.id.dspBookYear);
        dbuttonUpdate = dialogView.findViewById(R.id.dbuttonUpdate);
        dbuttonDelete = dialogView.findViewById(R.id.dbuttonDelete);
        dbuttonBack =  dialogView.findViewById(R.id.dbuttonBack);
    }
    public void onStart() {

        super.onStart();
        if (!spBookName.getText().toString().equals("")) {
            databaseBooks.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    books.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Book book = postSnapshot.getValue(Book.class);
                        if(Objects.requireNonNull(book).getBookTitle().startsWith(spBookName.getText().toString().toUpperCase())) {
                            books.add(book);
                        }

                    }

                    BookList bookAdapter = new BookList(getActivity(), books);
                    listViewBooks.setAdapter(bookAdapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        else if (!spBookAuthor.getText().toString().equals("")) {
            databaseBooks.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    books.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Book book = postSnapshot.getValue(Book.class);
                        if(Objects.requireNonNull(book).getBookAuthor().startsWith(spBookAuthor.getText().toString().toUpperCase())) {
                            books.add(book);
                        }

                    }

                    BookList bookAdapter = new BookList(getActivity(), books);
                    listViewBooks.setAdapter(bookAdapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        else if (!spBookSubject.getText().toString().equals("")) {
            databaseBooks.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    books.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Book book = postSnapshot.getValue(Book.class);
                        if(Objects.requireNonNull(book).getBookSubject().startsWith(spBookSubject.getText().toString().toUpperCase())) {
                            books.add(book);
                        }

                    }

                    BookList bookAdapter = new BookList(getActivity(), books);
                    listViewBooks.setAdapter(bookAdapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        else if (!spBookCost.getText().toString().equals("")) {
            databaseBooks.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    books.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Book book = postSnapshot.getValue(Book.class);
                        if(Objects.requireNonNull(book).getBookCost().equals(spBookCost.getText().toString().toUpperCase())) {
                            books.add(book);
                        }

                    }

                    BookList bookAdapter = new BookList(getActivity(), books);
                    listViewBooks.setAdapter(bookAdapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        else
        {
            databaseBooks.addValueEventListener(new ValueEventListener() {
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

}