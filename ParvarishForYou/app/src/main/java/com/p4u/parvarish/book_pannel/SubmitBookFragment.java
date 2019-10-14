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

public class SubmitBookFragment extends Fragment {

    private static final String TAG = "SubmitBookFragment";
    private ListView listViewBooks;
    private View dialogView;
    private List<Book> books;
    private DatabaseReference databaseBooks,TempUser;
    private EditText spBookName;

    private TextView dBookid,dAuthor,dTitle,dCost,dDonor,dDonorMobile,dLocation,dYear,dSubject,dDonorTime,dIssueTo,dIssueTime,tv2;
    private View v;
    private Button btnissue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_book_search,container,false);
        databaseBooks = FirebaseDatabase.getInstance().getReference().child("BOOKS");
        initViews();

        tv2.setText("Tap to Book Handover to center");
        //list to store books
        books = new ArrayList<>();
        listViewBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Book book = books.get(i);
                SubmitBookFragment.this.showDialog(
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
    private void initViews(){

        spBookName =  v.findViewById(R.id.sp_Book_Name);
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

        init_dialog_views();

        dialogBuilder.setTitle("Book Record");
        final AlertDialog b = dialogBuilder.create();
        b.show();
        dBookid.setText(dbookId);
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
        btnissue.setText("Handover");
        btnissue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.cancel();
                new FancyAlertDialog.Builder(getActivity())
                        .setTitle("ParvarishForYou")
                        .setMessage("Want to Submit to Center")
                        .setNegativeBtnText("Cancel")
                        .setPositiveBtnText("OK")
                        .setAnimation(Animation.POP)
                        .isCancellable(true)
                        .setIcon(R.drawable.logo, Icon.Visible)
                        .OnPositiveClicked(new FancyAlertDialogListener() {

                            @Override
                            public void OnClick() {
                                boolean ans=updateBook(dbookId,dbookHandoverTo);
                                if(ans){
                                    Toast.makeText(getContext(), "Submission Successfully", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .OnNegativeClicked(new FancyAlertDialogListener()  {
                            @Override
                            public void OnClick() {
                                Toast.makeText(getContext(), "Cancel", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .build();


            }
        });


    }
    private boolean updateBook(String bookId,String userId) {


        databaseBooks = FirebaseDatabase.getInstance().getReference().child("BOOKS").child(bookId);
        databaseBooks.child("bookAvaibility").setValue("1");
        databaseBooks.child("bookHandoverTo").setValue("CENTER");
        databaseBooks.child("bookHandoverTime").setValue(get_current_time());
        TempUser = FirebaseDatabase.getInstance().getReference().child("TEMPUSERS").child(userId);
        TempUser.child("bookHaving").setValue("0");
        return true;

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
                            if(Objects.requireNonNull(book).getBookTitle().startsWith(spBookName.getText().toString().toUpperCase())||
                                    Objects.requireNonNull(book).getBookAuthor().startsWith(spBookName.getText().toString().toUpperCase())||
                                    Objects.requireNonNull(book).getBookSubject().startsWith(spBookName.getText().toString().toUpperCase())||
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

            }else {
                databaseBooks.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        books.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Book book = postSnapshot.getValue(Book.class);
                            if (Objects.requireNonNull(book).getBookAvaibility().equals("0")) {
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

            }
        }catch (Exception e)
        {
            Log.d(TAG, "Exception in Adding List "+e);
        }
    }


    private String get_current_time(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss");
        return sdf.format(new Date());
    }
}