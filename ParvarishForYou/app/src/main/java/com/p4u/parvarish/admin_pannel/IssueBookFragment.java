package com.p4u.parvarish.admin_pannel;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import com.p4u.parvarish.fancydialog.Animation;
import com.p4u.parvarish.fancydialog.FancyAlertDialog;
import com.p4u.parvarish.fancydialog.FancyAlertDialogListener;
import com.p4u.parvarish.fancydialog.Icon;

public class IssueBookFragment extends Fragment {

    private static final String TAG = "IssueBook";
    private ListView listViewBooks;
    private View dialogView;
    private List<Book> books;
    private DatabaseReference databaseBooks;
    private EditText spBookSubject,spBookName,spBookAuthor,spBookCost;
    private TextView dBookid,dAuthor,dTitle,dCost,dDonor,dDonorMobile,dLocation,dYear,dSubject,dDonorTime,dIssueTo,dIssueTime,tv2;
    private View v;
    private Button btnissue;
    private String IssueName;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_book_search,container,false);

        databaseBooks = FirebaseDatabase.getInstance().getReference().child("BOOKS");
        //getting views
        initViews();
        tv2.setText("Tap to Book for Issue");
        //list to store books
        books = new ArrayList<>();
        listViewBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Book book = books.get(i);
                IssueBookFragment.this.showDialog(
                        book.getBookId(),
                        book.getBookTitle(),
                        book.getBookAuthor(),
                        book.getBookSubject(),
                        book.getBookYear(),
                        book.getBookCost(),
                        book.getBookLocation(),
                        book.getBookDonor(),
                        book.getBookDonorMobile(),
                        book.getBookDonorTime(),
                        book.getBookHandoverTo(),
                        book.getBookHandoverTime());
            }
        });

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
        listViewBooks =  v.findViewById(R.id.view_list);

    }
    @SuppressLint("InflateParams")
    private void showDialog(final String dbookId,
                            final String dbookTitle,
                            final String dbookAuthor,
                            final String dbookSubject,
                            final String dbookYear,
                            final String dbookCost,
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
        //init dialog views
        init_dialog_views();

        dialogBuilder.setTitle("Book Record");
        final AlertDialog b = dialogBuilder.create();
        b.show();
        //set values
        dBookid.setText (dbookId);
        dAuthor.setText(dbookAuthor);
        dCost.setText(dbookCost);
        dTitle.setText(dbookTitle);
        dLocation.setText(dbookLocation);
        dDonor.setText(dbookDonor);
        dDonorMobile.setText(dbookDonorMobile);
        dYear.setText(dbookYear);
        dSubject.setText(dbookSubject);
        dIssueTo.setText(dbookHandoverTo);
        dIssueTime.setText(dbookHandoverTime);
        dDonorTime.setText(dbookDonorTime);
        btnissue.setText("Issue");
        btnissue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.cancel();
                AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(IssueBookFragment.this.getContext()));
                final EditText input = new EditText(IssueBookFragment.this.getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input)
                        .setMessage("Enter Aadhar ID:")
                        .setCancelable(false)
                        .setPositiveButton("Issue", new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {



                            IssueName = input.getText().toString().toUpperCase();
                            new FancyAlertDialog.Builder(getActivity())
                                    .setTitle("ParvarishForYou")
                                    .setMessage("Want to issue to " + IssueName)
                                    .setNegativeBtnText("Cancel")
                                    .setPositiveBtnText("OK")
                                    .setAnimation(Animation.POP)
                                    .isCancellable(true)
                                    .setIcon(R.drawable.logo, Icon.Visible)
                                    .OnPositiveClicked(new FancyAlertDialogListener() {
                                        @Override
                                        public boolean OnClick() {
                                            updateBook(dbookId,
                                                    dbookTitle,
                                                    dbookAuthor,
                                                    dbookSubject,
                                                    dbookYear,
                                                    dbookCost,
                                                    dbookLocation,
                                                    dbookDonor,
                                                    dbookDonorMobile,
                                                    dbookDonorTime,
                                                    IssueName,
                                                    get_current_time());
                                            Toast.makeText(IssueBookFragment.this.getContext(), "Book Issued Successfully", Toast.LENGTH_SHORT).show();
                                            return true;
                                        }
                                    })
                                    .OnNegativeClicked(new FancyAlertDialogListener() {
                                        @Override
                                        public boolean OnClick() {
                                            Toast.makeText(IssueBookFragment.this.getContext(), "Cancel", Toast.LENGTH_SHORT).show();

                                            return false;
                                        }
                                    })
                                    .build();
                        }
            })
                    .setNegativeButton("No", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int id) {

                    dialog.cancel();

                }
            });
            AlertDialog alert = builder.create();
		    alert.show();

        }
    });

    }

    private void updateBook(String bookId,
                            String bookTitle,
                            String bookAuthor,
                            String bookSubject,
                            String bookYear,
                            String bookCost,
                            String bookLocation,
                            String bookDonor,
                            String bookDonorMobile,
                            String bookDonorTime,
                            String bookHandoverTo,
                            String bookHandoverTime) {

        Book book = new Book (
                bookId,
                bookTitle,
                bookAuthor,
                bookSubject,
                bookYear,
                bookCost,
                "0",
                bookLocation,
                bookDonor,
                bookDonorMobile,
                bookDonorTime,
                bookHandoverTo,
                bookHandoverTime);

        databaseBooks.child(Objects.requireNonNull(bookId)).setValue(book);

    }
    private boolean onClickDialog() {
        new FancyAlertDialog.Builder(getActivity())
                .setTitle("ParvarishForYou")
                .setMessage("Want to issue to "+IssueName)
                .setNegativeBtnText("Cancel")
                .setPositiveBtnText("OK")
                .setAnimation(Animation.POP)
                .isCancellable(true)
                .setIcon(R.drawable.logo, Icon.Visible)
                .OnPositiveClicked(new FancyAlertDialogListener() {
                    @Override
                    public boolean OnClick() {
                        return true;
                    }
                })
                .OnNegativeClicked(new FancyAlertDialogListener() {
                    @Override
                    public boolean OnClick() {
                        Toast.makeText(IssueBookFragment.this.getContext(), "Cancel", Toast.LENGTH_SHORT).show();

                        return false;
                    }
                })
                .build();

            return false;
    }
    private void init_dialog_views(){
        dBookid=dialogView.findViewById (R.id.tvBookid);
        dTitle =dialogView.findViewById(R.id.tvTitle);
        dCost = dialogView.findViewById(R.id.tvCost);
        dAuthor =dialogView.findViewById(R.id.tvAuthor);
        dYear = dialogView.findViewById(R.id.tvYear);
        dSubject = dialogView.findViewById(R.id.tvSubject);
        dLocation = dialogView.findViewById(R.id.tvLocation);
        dDonor = dialogView.findViewById(R.id.tvDonor);
        dDonorMobile = dialogView.findViewById(R.id.tvDonorMobile);
        dDonorTime = dialogView.findViewById(R.id.tvDonateTime);
        dIssueTo =  dialogView.findViewById(R.id.tvBookIssueto);
        dIssueTime = dialogView.findViewById(R.id.tvIssueTime);
        btnissue=dialogView.findViewById(R.id.dbuttonBack);
    }
    public void onStart() {

        super.onStart();
        try {
            if (!spBookName.getText().toString().equals("")) {
                databaseBooks.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        books.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Book book = postSnapshot.getValue(Book.class);
                            if (Objects.requireNonNull(book).getBookTitle().startsWith(spBookName.getText().toString().toUpperCase()) && book.getBookAvaibility().equals("1")) {
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

            } else if (!spBookAuthor.getText().toString().equals("")) {
                databaseBooks.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        books.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Book book = postSnapshot.getValue(Book.class);
                            if (Objects.requireNonNull(book).getBookAuthor().startsWith(spBookAuthor.getText().toString().toUpperCase()) && book.getBookAvaibility().equals("1")) {
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

            } else if (!spBookSubject.getText().toString().equals("")) {
                databaseBooks.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        books.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Book book = postSnapshot.getValue(Book.class);
                            if (Objects.requireNonNull(book).getBookSubject().startsWith(spBookSubject.getText().toString().toUpperCase()) && book.getBookAvaibility().equals("1")) {
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

            } else if (!spBookCost.getText().toString().equals("")) {
                databaseBooks.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        books.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Book book = postSnapshot.getValue(Book.class);
                            if (Objects.requireNonNull(book).getBookCost().equals(spBookCost.getText().toString().toUpperCase()) && book.getBookAvaibility().equals("1")) {
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
        }catch (Exception e){
            Log.d(TAG, "Exception in Adding List"+e);
        }finally {
            databaseBooks.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    books.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Book book = postSnapshot.getValue(Book.class);
                        if (Objects.requireNonNull(book).getBookAvaibility().equals("1")) {
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
    }

    private String get_current_time(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss");
        return sdf.format(new Date());
    }
}