package com.p4u.parvarish.book_pannel;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
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
import com.p4u.parvarish.user_pannel.TempUser;

import static java.util.Objects.requireNonNull;

public class IssueBookFragment extends Fragment {

    private static final String TAG = "IssueBookFragment";
    private ListView listViewBooks;
    private View dialogView,issuedialog,benefiarydialog;
    private List<Book> books;
    private ArrayList<String> benefiary;
    private DatabaseReference myref,mRef;
    private EditText spBookName;
    private Spinner spbenfiary;
    private TextView dBookid,dAuthor,dTitle,dCost,dDonor,dDonorMobile,dLocation,dYear,dSubject,dDonorTime,dIssueTo,dIssueTime,tv2,tv1;
    private View v;
    private TextInputEditText etFullname,etEmail,etMobile,etAddress,etIdentity,etDepositpaid;
    private TextInputLayout tlname,tlemail,tlmobile,tladdress,tlidentity,tldeposit;
    private Button btnissue,handover,btnadd,btnsearch,buttonback,btnreset;
    private String Userid,Fullname,Email,Mobile,Address,Identity,Deposit,beneficaiary_mobile;
    private int noofbooks;
    Boolean amount_validation=false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_book_search,container,false);
        myref = FirebaseDatabase.getInstance().getReference().child("BOOKS");

        //getting views
        initViews();
        tv2.setText("Tap to Book for Issue");
        //list to store books
        books = new ArrayList<>();
        benefiary = new ArrayList<String>();
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


        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_values();
                boolean res=validate();
                if(res) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                    LayoutInflater inflater = getLayoutInflater();
                    dialogView = inflater.inflate(R.layout.show_issuebook_dialog, null);
                    dialogBuilder.setView(dialogView);
                    init_dialog_views();
                    dialogBuilder.setTitle("Amount To be Paid");
                    final AlertDialog dialog = dialogBuilder.create();
                    dialog.show();

                    etDepositpaid.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            get_dialog_values();
                            amount_validation=validate_deposit();

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            if (amount_validation) {
                                handover.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.cancel();
                                        Userid=create_user(Fullname,Email,Address,Mobile,Identity,Deposit);
                                        disable_all();
                                        tv1.setText("BENEFICIARY REGISTRED");
                                        Toast.makeText(getContext(), "Beneficiary Created", Toast.LENGTH_SHORT).show();
                                    }

                                });
                            }
                        }
                    });

                }

            }
        });

        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //enable_all();
                blank_all();
                tv1.setText("BENEFICIARY DETAILS");
                //etFullname.requestFocus();
                Userid=null;
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getLayoutInflater();
                benefiarydialog = inflater.inflate(R.layout.beneficiary_dialog, null);
                dialogBuilder.setView(benefiarydialog);
                spbenfiary=benefiarydialog.findViewById(R.id.beneficiry_list);
                buttonback=benefiarydialog.findViewById(R.id.dbuttonBack);
                dialogBuilder.setTitle("Beneficiary List");
                final AlertDialog b = dialogBuilder.create();
                b.show();
                load_beneficiary();
                buttonback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        b.cancel();

                    }
                });
                spbenfiary.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        beneficaiary_mobile=spbenfiary.getSelectedItem().toString();

                        load_values(beneficaiary_mobile);
                        btnadd.setEnabled(false);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            }
        });
        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blank_all();
                enable_all();
                tv1.setText("NEW BENEFICIARY");
            }
        });
        return v;
    }

    private void load_values(final String beneficaiary_mobile) {
        try {
            mRef = FirebaseDatabase.getInstance().getReference().child("TEMPUSERS");
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        TempUser beneficiary = postSnapshot.getValue(TempUser.class);
                        if(beneficaiary_mobile.equals(requireNonNull(beneficiary).getMobile())){
                            set_values(beneficiary.getId(),beneficiary.getName(),beneficiary.getEmail(),beneficiary.getAddress(),beneficiary.getMobile(),beneficiary.getIdentity());
                        }



                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }catch (Exception e){

        }
    }

    private void set_values(String id, String name, String email, String address, String mobile, String identity) {
        Userid=id;
        amount_validation=false;
        Fullname=name;
        etFullname.setText(name);
        etEmail.setText(email);
        etMobile.setText(mobile);
        etAddress.setText(address);
        etIdentity.setText(identity);
    }

    private void load_beneficiary() {
        try{
            mRef = FirebaseDatabase.getInstance().getReference().child("TEMPUSERS");
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    benefiary.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        TempUser beneficiary = postSnapshot.getValue(TempUser.class);

                        assert beneficiary != null;
                        benefiary.add(beneficiary.getMobile());


                    }
                    ArrayAdapter<String> beneficiary_adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, benefiary);
                    spbenfiary.setAdapter(beneficiary_adapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }catch(Exception e){

        }
    }

    private void blank_all() {
        Userid=null;
        Deposit="";
        Fullname=null;
        etFullname.setText("");
        etEmail.setText("");
        etMobile.setText("");
        etAddress.setText("");
        etIdentity.setText("");
    }
    private void disable_all() {
        etFullname.setEnabled(false);
        etEmail.setEnabled(false);
        etMobile.setEnabled(false);
        etAddress.setEnabled(false);
        etIdentity.setEnabled(false);

    }
    private void enable_all() {
        etFullname.setEnabled(true);
        etEmail.setEnabled(true);
        etMobile.setEnabled(true);
        etAddress.setEnabled(true);
        etIdentity.setEnabled(true);
        btnadd.setEnabled(true);
        amount_validation=false;
        Userid=null;

    }
    private void get_dialog_values() {

        Deposit= requireNonNull(etDepositpaid.getText()).toString();

    }
    private void initViews(){

        spBookName =  v.findViewById(R.id.sp_Book_Name);
        btnadd=v.findViewById(R.id.btnAdd);
        btnsearch=v.findViewById(R.id.btnsearch);
        btnreset=v.findViewById(R.id.btnreset);
        tv1=v.findViewById(R.id.tv1);
        tv2=v.findViewById(R.id.tv2);
        listViewBooks =  v.findViewById(R.id.view_list);
        etFullname=v.findViewById(R.id.et_fullname);
        etEmail=v.findViewById(R.id.et_createemail);
        etAddress=v.findViewById(R.id.et_address);
        etMobile=v.findViewById(R.id.et_createmobile);
        etIdentity=v.findViewById(R.id.et_identity);
        tlname=v.findViewById(R.id.tv_createusername);
        tlemail=v.findViewById(R.id.tv_createemail);
        tladdress=v.findViewById(R.id.tv_address);
        tlmobile=v.findViewById(R.id.tv_createmobile);
        tlidentity=v.findViewById(R.id.tv_identity);


    }
    private void get_values(){
        Fullname= Objects.requireNonNull(etFullname.getText()).toString().toUpperCase();
        Email= Objects.requireNonNull(etEmail.getText()).toString();
        Mobile = Objects.requireNonNull(etMobile.getText()).toString().toUpperCase();
        Address= Objects.requireNonNull(etAddress.getText()).toString().toUpperCase();
        Identity= Objects.requireNonNull(etIdentity.getText()).toString().toUpperCase();


    }
    private boolean validate() {


        if (TextUtils.isEmpty(Fullname)) {
            tlname.setError("Enter full name!");
            return false;
        }
        if (Mobile.length ()!=10) {
            tlmobile.setError("Enter Mobile Number!");
            return false;
        }

        if (TextUtils.isEmpty(Email)) {
            tlemail.setError("Enter email address!");
            return false;
        }

        if (TextUtils.isEmpty(Address)) {
            tladdress.setError("Enter Address!");
            return false;
        }
        if (TextUtils.isEmpty(Identity)) {
            tlidentity.setError("Enter aadhar or Voter id !");
            return false;
        }

        return true;
    }
    private boolean validate_deposit(){
        if (TextUtils.isEmpty(Deposit)) {
            tldeposit.setError("Enter Valid Amount!");
            return false;
        }
        return true;
    }
    private void init_dialog_views(){

        etDepositpaid=dialogView.findViewById(R.id.et_depositpaid);
        handover = dialogView.findViewById(R.id.dbuttonissue);
        tldeposit=dialogView.findViewById(R.id.tv_depositpaid);

    }
    private void load_list() {
        try{
            if (!spBookName.getText().toString().equals("")) {
                myref.addValueEventListener(new ValueEventListener() {
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
                            .setMessage("Want to issue to " + Fullname)
                            .setNegativeBtnText("Cancel")
                            .setPositiveBtnText("OK")
                            .setAnimation(Animation.POP)
                            .isCancellable(true)
                            .setIcon(R.drawable.logo, Icon.Visible)
                            .OnPositiveClicked(new FancyAlertDialogListener() {
                                @Override
                                public void OnClick() {
                                    boolean ans = updateBook(Userid, dbookId);

                                    if (ans) {
                                        Toast.makeText(getContext(), "Book Issued Successfully", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show();
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
            }
        });
     }



    private String create_user(String Fullname,String Email,String Address,String Mobile,String Identity,String Deposit) {
        DatabaseReference temp_User = FirebaseDatabase.getInstance().getReference().child("TEMPUSERS");
        String UserId = temp_User.push().getKey();
        noofbooks=0;
        TempUser user = new TempUser(
                UserId,
                Fullname,
                Email,
                Mobile,
                Address,
                Identity,
                "0",
                Deposit);

        //Saving the Book
        temp_User.child(requireNonNull(UserId)).setValue(user);

        return UserId;
    }

    private boolean updateBook(String userid,String bookId){
        try {
            DatabaseReference issueref = FirebaseDatabase.getInstance().getReference().child("BOOKS").child(bookId);
            DatabaseReference temp_User = FirebaseDatabase.getInstance().getReference().child("TEMPUSERS").child(userid);

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