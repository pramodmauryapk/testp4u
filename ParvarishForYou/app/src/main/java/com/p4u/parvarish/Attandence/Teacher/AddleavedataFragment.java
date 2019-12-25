package com.p4u.parvarish.Attandence.Teacher;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.p4u.parvarish.R;

import java.util.Calendar;

import static java.util.Objects.requireNonNull;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddleavedataFragment extends Fragment {

    private View v;
    private DatabaseReference databaseUsers;
    private Context context;
    private TextInputEditText tv1,tv2;
    private TextInputLayout l1,l2;
    private Button btnpost;
    private Bundle bundle;
    private int mYear,mMonth,mDay;
    private String schoolname,date;
    public AddleavedataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_addleavedata, container, false);
        bundle=new Bundle();
        schoolname = requireNonNull(this.getArguments()).getString("SCHOOL_NAME");
        // Inflate the layout for this fragment
        init();
        databaseUsers = FirebaseDatabase.getInstance().getReference().child("SCHOOL").child(schoolname).child("LEAVES");
        context = container.getContext();
        btnpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addno();
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @SuppressLint("DefaultLocale")
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                tv2.setText(String.format("%d/%d/%d", year, monthOfYear + 1,dayOfMonth ));

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        //date= requireNonNull(tv2.getText()).toString();
        return v;
    }

    private void init() {
        tv1=v.findViewById(R.id.et_festival);
        tv2=v.findViewById(R.id.et_date);
        l1=v.findViewById(R.id.tv_service);
        l2=v.findViewById(R.id.tv_mobile);
        btnpost=v.findViewById(R.id.btnpost);
    }
    private void addno() {


        //validating
        boolean ans=validate();
        if(ans)
        {

            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our Book
            String Id = databaseUsers.push().getKey();
            //creating an Book Object
            LeaveData row = new LeaveData (
                    requireNonNull(tv1.getText()).toString(),
                    requireNonNull(tv2.getText()).toString());

            //Saving the Book
            databaseUsers.child(requireNonNull(Id)).setValue(row);
            Toast.makeText(getActivity(), "Leave added", Toast.LENGTH_LONG).show();
            //setting edittext to blank again
            set_blank();

        } else {
            Toast.makeText(getActivity(), "fill All required fields ", Toast.LENGTH_LONG).show();
        }
    }
    private boolean validate() {

        if (TextUtils.isEmpty(tv1.getText())) {
            l1.setError("Enter Leave Name");

            return false;
        }
        if (TextUtils.isEmpty(tv2.getText())) {
            l2.setError("Select date");
            return false;
        }

        return true;
    }

    private void set_blank(){

        tv1.setText("");
        tv2.setText("");


    }
}
