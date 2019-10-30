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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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


import static java.util.Objects.requireNonNull;

public class SubmitBookFragment extends Fragment {

    private static final String TAG = "SubmitBookFragment";
    private ListView listViewBooks;
    private View dialogView,handover_dialog;
    private List<Book> books;
    private DatabaseReference databaseBooks;
    private EditText spBookName;
    private TextView dBookid,dAuthor,dTitle,dCost,dDonor,dDonorMobile,dLocation,dYear,dSubject,dDonorTime,dIssueTo,dIssueTime,tv2,tv;
    private View v;
    private Button btnissue,handover;
    private int noofbook,userdeposit,userrefund;
    private TextInputEditText et_deposit_paid;
    private TextInputLayout tv1;
    private String userid=null;

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_book_search,container,false);
        databaseBooks = FirebaseDatabase.getInstance().getReference().child("BOOKS");
        initViews();


        assert this.getArguments() != null;
        userid = this.getArguments().getString("user_id");
        noofbook=Integer.parseInt(requireNonNull(this.getArguments().getString("user_noofbooks")));
        userdeposit=Integer.parseInt(requireNonNull(this.getArguments().getString("user_deposit")));
        userrefund=Integer.parseInt(requireNonNull(this.getArguments().getString("user_refund")));
        tv2.setText("Tap to Book Handover to center");
        //list to store books
        books = new ArrayList<>();

        listViewBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Book book = books.get(i);
                showDialog(
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

        set_dialog_values(dbookId,dbookAuthor,dbookCost,dbookTitle,dbookLocation,
                dbookDonor,dbookDonorMobile,dbookYear,dbookSubject,dbookHandoverTo,
                dbookHandoverTime,dbookDonorTime);



        btnissue.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                b.cancel();
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getLayoutInflater();
                handover_dialog = inflater.inflate(R.layout.show_issuebook_dialog, null);
                dialogBuilder.setView(handover_dialog);
                init_dialog_views();
                dialogBuilder.setTitle("Amount To be Paid");
                final AlertDialog dialog = dialogBuilder.create();
                dialog.show();

                et_deposit_paid=dialog.findViewById(R.id.et_depositpaid);
                handover=dialog.findViewById(R.id.dbuttonissue);
                tv1=dialog.findViewById(R.id.tv_depositpaid);
                tv=dialog.findViewById(R.id.tvuserid);
                tv.setText("ISSUE DATE:"+dbookHandoverTime);
                tv1.setHint("Refund Money");

                handover.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
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
                                        boolean ans=updateBook(dbookId,dbookHandoverTo, requireNonNull(et_deposit_paid.getText()).toString());
                                       // load_list();
                                        if(ans){
                                            Toast.makeText(getContext(), "Submission Successfully", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show();
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
        });


    }


    @SuppressLint("SetTextI18n")
    private void set_dialog_values(String dbookId, String dbookAuthor, String dbookCost, String dbookTitle, String dbookLocation,
                                   String dbookDonor, String dbookDonorMobile, String dbookYear, String dbookSubject, String dbookHandoverTo,
                                   String dbookHandoverTime, String dbookDonorTime) {
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
    }


    private boolean updateBook(String bookId, final String UserId,String amount) {

        try {
            databaseBooks = FirebaseDatabase.getInstance().getReference().child("BOOKS").child(bookId);
            databaseBooks.child("bookAvaibility").setValue("1");
            databaseBooks.child("bookHandoverTo").setValue("CENTER");
            databaseBooks.child("bookHandoverTime").setValue(get_current_time());
            DatabaseReference temp_User = FirebaseDatabase.getInstance().getReference().child("TEMPUSERS").child(UserId);

            noofbook = noofbook - 1;
            temp_User.child("bookHaving").setValue(String.valueOf(noofbook));
            temp_User.child("bookDeposit").setValue(userdeposit-Integer.parseInt(amount));
            temp_User.child("bookRefund").setValue(userrefund+Integer.parseInt(amount));

            return true;
        }catch (Exception e){
            return false;
        }

    }
    public void onStart() {

        super.onStart();
        load_list();

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
    private void load_list() {


            if (!spBookName.getText().toString().equals("")) {
                databaseBooks.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        books.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Book book = postSnapshot.getValue(Book.class);
                            if(Objects.requireNonNull(book).getBookAvaibility().equals("0")&&(book.getBookHandoverTo().equals(userid))){
                            if(Objects.requireNonNull(book).getBookTitle().startsWith(spBookName.getText().toString().toUpperCase())||
                                    Objects.requireNonNull(book).getBookAuthor().startsWith(spBookName.getText().toString().toUpperCase())||
                                    Objects.requireNonNull(book).getBookSubject().startsWith(spBookName.getText().toString().toUpperCase())||
                                    Objects.requireNonNull(book).getBookCost().equals(spBookName.getText().toString())) {

                                    books.add(book);

                                }
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
                            if (Objects.requireNonNull(book).getBookAvaibility().equals("0")&&(book.getBookHandoverTo().equals(userid))) {
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
    private String get_current_time(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss");
        return sdf.format(new Date());
    }
}