package com.p4u.parvarish.book_pannel;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.p4u.parvarish.R;

public class SearchBookFragment extends Fragment {
    private static final String TAG = "SearchBookFragment";
    private ListView listViewBooks;
    private List<Book> books;
    private DatabaseReference databaseBooks;
    private EditText spBookName;

    private View v;
    private TextView dBookid,dAuthor,dTitle,dCost,dDonor,dDonorMobile,dLocation,dYear,dSubject,dDonorTime,dIssueTo,dIssueTime;
    private Button dBack;
    private View dialogView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_book_search,container,false);

        databaseBooks = FirebaseDatabase.getInstance().getReference().child("BOOKS");
        //getting views
        initViews();

        //list to store books
        books = new ArrayList<>();
        listViewBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Book book = books.get(i);
                SearchBookFragment.this.showDialog(
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


        return v;
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
    private void init_dialog_views(){
        dBookid= dialogView.findViewById (R.id.tvBookid);
        dTitle = dialogView.findViewById(R.id.tvTitle);
        dCost =  dialogView.findViewById(R.id.tvCost);
        dAuthor = dialogView.findViewById(R.id.tvAuthor);
        dYear = dialogView.findViewById(R.id.tvYear);
        dSubject =  dialogView.findViewById(R.id.tvSubject);
        dLocation = dialogView.findViewById(R.id.tvLocation);
        dDonor = dialogView.findViewById(R.id.tvDonor);
        dDonorMobile = dialogView.findViewById(R.id.tvDonorMobile);
        dDonorTime =  dialogView.findViewById(R.id.tvDonateTime);
        dIssueTo = dialogView.findViewById(R.id.tvBookIssueto);
        dIssueTime = dialogView.findViewById(R.id.tvIssueTime);
        dBack=dialogView.findViewById(R.id.dbuttonBack);
    }

    private void initViews(){

         spBookName =  v.findViewById(R.id.sp_Book_Name);

        listViewBooks =  v.findViewById(R.id.view_list);

    }
    public void onStart() {

        super.onStart();
        load_list();

    }

    private void load_list() {
        try {
            if (!spBookName.getText().toString().equals("")) {
                databaseBooks.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        books.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Book book = postSnapshot.getValue(Book.class);
                            if (Objects.requireNonNull(book).getBookTitle().startsWith(spBookName.getText().toString().toUpperCase()) ||
                                    Objects.requireNonNull(book).getBookAuthor().startsWith(spBookName.getText().toString().toUpperCase()) ||
                                    Objects.requireNonNull(book).getBookSubject().startsWith(spBookName.getText().toString().toUpperCase()) ||
                                    Objects.requireNonNull(book).getBookCost().equals(spBookName.getText().toString())) {
                                books.add(book);
                            }


                        }

                        LayoutBookList bookAdapter = new LayoutBookList(getActivity(), books);
                        listViewBooks.setAdapter(bookAdapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            } else {
                databaseBooks.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        books.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Book book = postSnapshot.getValue(Book.class);
                            books.add(book);

                        }

                        LayoutBookList bookAdapter = new LayoutBookList(getActivity(), books);
                        listViewBooks.setAdapter(bookAdapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        }catch (Exception e){
            Log.d(TAG, "Exception in Adding List "+e);
        }
    }


}
