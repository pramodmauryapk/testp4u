package com.p4u.parvarish.book_pannel;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.p4u.parvarish.R;
import com.p4u.parvarish.fancydialog.Animation;
import com.p4u.parvarish.fancydialog.FancyAlertDialog;
import com.p4u.parvarish.fancydialog.FancyAlertDialogListener;
import com.p4u.parvarish.fancydialog.Icon;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class SelectBookFragment extends Fragment {

    private static final String TAG = "AddBeneficiaryFragment";
    private ListView listViewBooks;
    private List<Book> books;
    private DatabaseReference myref;
    private EditText spBookName;

    private TextView tv2;

    private View v;

    private String Userid=null;

    private int noofbooks;
    private Context context;
    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_book_search,container,false);
        myref = FirebaseDatabase.getInstance().getReference().child("BOOKS");
        context = container.getContext();
        //getting views
        initViews();

        assert this.getArguments() != null;
        Userid = this.getArguments().getString("Beneficiary_Id");
        String name = this.getArguments().getString("Beneficiary_Name");
        tv2.setText("Tap to Book for Issue");
        //list to store books
        books = new ArrayList<>();

        load_list();
        TextWatcher watch = new TextWatcher(){

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                load_list();
                //Toast.makeText(getApplicationContext(), "Maximum Limit Reached", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence s, int a, int b, int c) {
                // TODO Auto-generated method stub



            }};
        spBookName.addTextChangedListener(watch);

        listViewBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Book book = books.get(i);
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



        return v;
    }







    private void initViews(){

        spBookName =  v.findViewById(R.id.sp_Book_Name);
        tv2=v.findViewById(R.id.tv2);
        listViewBooks =  v.findViewById(R.id.view_list);



    }



    private void load_list() {

        if (!spBookName.getText().toString().equals("")) {
            myref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    books.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Book book = postSnapshot.getValue(Book.class);
                        if (Objects.requireNonNull(book).getBookAvaibility().equals("1")){
                            if (Objects.requireNonNull(book).getBookTitle().startsWith(spBookName.getText().toString().toUpperCase()) ||
                                    Objects.requireNonNull(book).getBookAuthor().startsWith(spBookName.getText().toString().toUpperCase()) ||
                                    Objects.requireNonNull(book).getBookSubject().startsWith(spBookName.getText().toString().toUpperCase()) ||
                                    Objects.requireNonNull(book).getBookCost().equals(spBookName.getText().toString())) {

                                books.add(book);

                            }
                        }

                    }
                    if (getActivity() != null) {
                        LayoutBookList bookAdapter = new LayoutBookList(getActivity(), books);
                        listViewBooks.setAdapter(bookAdapter);
                        bookAdapter.notifyDataSetChanged();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else {
            myref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    books.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Book book = postSnapshot.getValue(Book.class);
                        if (Objects.requireNonNull(book).getBookAvaibility().equals("1")) {

                            books.add(book);

                        }
                    }
                    if(getActivity()!=null) {
                        LayoutBookList bookAdapter = new LayoutBookList(getActivity(), books);
                        listViewBooks.setAdapter(bookAdapter);
                        bookAdapter.notifyDataSetChanged();
                    }

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
    @SuppressLint({"InflateParams", "SetTextI18n"})
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

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.show_book_dialog, null);
        dialogBuilder.setView(dialogView);
        //init dialog views
        TextView dBookid = dialogView.findViewById(R.id.tvBookid);
        TextView dTitle = dialogView.findViewById(R.id.tvTitle);
        TextView dCost = dialogView.findViewById(R.id.tvCost);
        TextView dAuthor = dialogView.findViewById(R.id.tvAuthor);
        TextView dYear = dialogView.findViewById(R.id.tvYear);
        TextView dSubject = dialogView.findViewById(R.id.tvSubject);
        TextView dLocation = dialogView.findViewById(R.id.tvLocation);
        TextView dDonor = dialogView.findViewById(R.id.tvDonor);
        TextView dDonorMobile = dialogView.findViewById(R.id.tvDonorMobile);
        TextView dDonorTime = dialogView.findViewById(R.id.tvDonateTime);
        TextView dIssueTo = dialogView.findViewById(R.id.tvBookIssueto);
        TextView dIssueTime = dialogView.findViewById(R.id.tvIssueTime);
        Button btnissue = dialogView.findViewById(R.id.dbuttonBack);

        dialogBuilder.setTitle("Book Record");
        final AlertDialog b = dialogBuilder.create();
        b.show();
        //set values
        dBookid.setText(dbookId);
        dTitle.setText(dbookTitle);
        dCost.setText(dbookCost);
        dAuthor.setText(dbookAuthor);
        dYear.setText(dbookYear);
        dSubject.setText(dbookSubject);
        dLocation.setText(dbookLocation);
        dDonor.setText(dbookDonor);
        dDonorMobile.setText(dbookDonorMobile);
        dDonorTime.setText(dbookDonorTime);
        dIssueTo.setText(dbookHandoverTo);
        dIssueTime.setText(dbookHandoverTime);
        btnissue.setText("Issue");

        btnissue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Userid!=null) {
                    b.cancel();

                    new FancyAlertDialog.Builder(getActivity())
                            .setTitle("ParvarishForYou")
                            .setMessage("Want to issue to " + Userid)
                            .setNegativeBtnText("Cancel")
                            .setPositiveBtnText("OK")
                            .setAnimation(Animation.POP)
                            .isCancellable(true)
                            .setIcon(R.drawable.logo, Icon.Visible)
                            .OnPositiveClicked(new FancyAlertDialogListener() {
                                @Override
                                public void OnClick() {
                                    boolean ans = updateBook(Userid, dbookId);
                                    // load_list();
                                    if (ans) {
                                        Toast.makeText(context, "Book Issued Successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .OnNegativeClicked(new FancyAlertDialogListener() {
                                @Override
                                public void OnClick() {
                                    Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();

                                }
                            })
                            .build();

                }
            }
        });
    }





    private boolean updateBook(String userid,String bookId){
        try {
            DatabaseReference issueref = FirebaseDatabase.getInstance().getReference().child("BOOKS").child(bookId);
            DatabaseReference temp_User = FirebaseDatabase.getInstance().getReference().child("BENEFICIARY").child(userid);

            issueref.child("bookAvaibility").setValue("0");
            issueref.child("bookHandoverTo").setValue(userid);
            issueref.child("bookHandoverTime").setValue(get_current_time());
            noofbooks=noofbooks+1;
            temp_User.child("bookHaving").setValue(String.valueOf(noofbooks));


            return true;
        }catch (Exception e){
            return false;
        }


    }




}