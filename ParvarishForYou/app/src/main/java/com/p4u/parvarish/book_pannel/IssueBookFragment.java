package com.p4u.parvarish.book_pannel;

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

import com.google.android.material.textfield.TextInputEditText;
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
import com.p4u.parvarish.user_pannel.TempUser;

import static java.util.Objects.requireNonNull;

public class IssueBookFragment extends Fragment {

    private static final String TAG = "IssueBookFragment";
    private ListView listViewBooks;
    private View dialogView,issuedialog;
    private List<Book> books;
    private DatabaseReference databaseBooks,TempUser;
    private EditText spBookSubject,spBookName,spBookAuthor,spBookCost;
    private TextView dBookid,dAuthor,dTitle,dCost,dDonor,dDonorMobile,dLocation,dYear,dSubject,dDonorTime,dIssueTo,dIssueTime,tv2;
    private View v;
    private TextInputEditText etFullname,etEmail,etMobile,etAddress,etIdentity,etDepositpaid;
    private Button btnissue,issue;
    private String Fullname,Email,Mobile,Address,Identity,Deposit;
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
                AlertDialog.Builder dialogBuilder1 = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getLayoutInflater();
                issuedialog = inflater.inflate(R.layout.show_issuebook_dialog, null);
                dialogBuilder1.setView(issuedialog);
                init_issuedialog_views();
                dialogBuilder1.setTitle("Receiver Details");
                final AlertDialog id = dialogBuilder1.create();
                id.show();
                issue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        id.cancel();
                        getValue_from_dialog();

                        new FancyAlertDialog.Builder(getActivity())
                                .setTitle("ParvarishForYou")
                                .setMessage("Want to issue to " + Fullname)
                                .setNegativeBtnText("Cancel")
                                .setPositiveBtnText("OK")
                                .setAnimation(Animation.POP)
                                .isCancellable(true)
                                .setIcon(R.drawable.logo, Icon.Visible)
                                .OnPositiveClicked(new FancyAlertDialogListener() {
                                    @Override
                                    public void OnClick() {
                                        boolean ans=updateBook(dbookId,Mobile,Fullname,Email,Address,Identity,Deposit);
                                        //boolean ans=true;
                                        if(ans){
                                            Toast.makeText(getContext(), "Book Issued Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .OnNegativeClicked(new FancyAlertDialogListener() {
                                    @Override
                                    public void OnClick() {
                                        Toast.makeText(getContext(), "Cancel", Toast.LENGTH_SHORT).show();

                                    }
                                })
                                .build();
                    }
                });


            }
        });
    }

    private void getValue_from_dialog() {
        Fullname= Objects.requireNonNull(etFullname.getText()).toString().toUpperCase();
        Email= Objects.requireNonNull(etEmail.getText()).toString();
        Mobile = Objects.requireNonNull(etMobile.getText()).toString().toUpperCase();
        Address= Objects.requireNonNull(etAddress.getText()).toString().toUpperCase();
        Identity= Objects.requireNonNull(etIdentity.getText()).toString().toUpperCase();
        Deposit= requireNonNull(etDepositpaid.getText()).toString();
    }

    private boolean updateBook(String bookId,String Mobile,String Name,String Email,String Address, String Identity,String Deposit){
    databaseBooks = FirebaseDatabase.getInstance().getReference().child("BOOKS").child(bookId);
    TempUser = FirebaseDatabase.getInstance().getReference().child("TEMPUSERS");
    String UserId=TempUser.push().getKey();
    databaseBooks.child("bookAvaibility").setValue("0");
    databaseBooks.child("bookHandoverTo").setValue(UserId);
    databaseBooks.child("bookHandoverTime").setValue(get_current_time());
        TempUser user = new TempUser(
                UserId,
                Name,
                Email,
                Mobile,
                Address,
                Identity,
                bookId,
                Deposit);

        //Saving the Book
        TempUser.child(requireNonNull(UserId)).setValue(user);
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
    private void init_issuedialog_views(){
        etFullname = issuedialog.findViewById(R.id.et_fullname);
        etEmail = issuedialog.findViewById(R.id.et_createemail);
        etMobile = issuedialog.findViewById(R.id.et_createmobile);
        etAddress=issuedialog.findViewById(R.id.et_address);
        etIdentity=issuedialog.findViewById(R.id.et_identity);
        etDepositpaid=issuedialog.findViewById(R.id.et_depositpaid);
        issue = issuedialog.findViewById(R.id.dbuttonissue);


    }
    public void onStart() {

        super.onStart();
        load_list();

    }
    private void load_list() {
        try{
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
                            if (Objects.requireNonNull(book).getBookAvaibility().equals("1")) {
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
        }
        catch (Exception e){
            Log.d(TAG, "Exception in Adding List "+e);
        }
    }
    private String get_current_time(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss");
        return sdf.format(new Date());
    }

}