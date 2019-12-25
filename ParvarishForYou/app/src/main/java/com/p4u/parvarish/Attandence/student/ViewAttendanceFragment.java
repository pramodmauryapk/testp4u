package com.p4u.parvarish.Attandence.student;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.p4u.parvarish.Attandence.admin.StudentData;
import com.p4u.parvarish.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;


public class ViewAttendanceFragment extends Fragment {

    private DatabaseReference studentref;
    private ListView mainListView;
    private List<StudentData> students;
    private ArrayList<String> attandence;
    private ArrayList<String> checkedValue;
    private StudentAttandenceArrayAdapter Adapter;
    private int mYear,mMonth,mDay,count;
    private Spinner sp_class,sp_section;
    private String date,schoolname,classname,section;
    private DatabaseReference dR;
    private Bundle bundle;
    private EditText etdate;
    private Context context;
    private String TAG="view attandence";
    private View v;

    public ViewAttendanceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_view_attendance, container, false);
        // Inflate the layout for this fragment
        init();
        bundle=new Bundle();
        schoolname = (Objects.requireNonNull(this.getArguments())).getString("SCHOOL_NAME");

        context = container.getContext();
        students = new ArrayList<>();
        attandence= new ArrayList<>();
        etdate.setText(get_current_time());
        etdate.setOnClickListener(new View.OnClickListener() {
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

                                etdate.setText(String.format("%d/%d/%d", year, monthOfYear + 1,dayOfMonth ));

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        date=etdate.getText().toString();
        load_class(schoolname);
        sp_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //load_list();
                load_section(schoolname,sp_class.getSelectedItem().toString());
                classname=sp_class.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                load_section(schoolname,sp_class.getItemAtPosition(0).toString());
                classname=sp_class.getItemAtPosition(0).toString();
            }
        });
        sp_section.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                section=sp_section.getSelectedItem().toString();
                load_list(schoolname,classname,sp_section.getSelectedItem().toString(),date);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                section=sp_section.getItemAtPosition(0).toString();
                load_list(schoolname,classname,sp_section.getItemAtPosition(0).toString(),date);
            }
        });
        etdate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                load_list(schoolname,classname,section,date);
            }
        });

        return v;
    }
    private void load_section(String Name,String classname) {
        FirebaseDatabase.getInstance().getReference().child("SCHOOL").child(Name).child("STUDENTS").child("PERSONAL_DATA").child(classname).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> sectionlist = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    //here is your every post
                    sectionlist.add(snapshot.getKey());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireNonNull(context), android.R.layout.simple_spinner_item, sectionlist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_section.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    public void load_list(final String schoolname, final String classname, final String section,final String date) {

        dR= FirebaseDatabase.getInstance().getReference()
                .child("SCHOOL")
                .child(schoolname)
                .child("STUDENTS")
                .child("PERSONAL_DATA")
                .child(classname)
                .child(section);

        dR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                students.clear();


                Adapter = new StudentAttandenceArrayAdapter(getActivity(), students,attandence);
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    StudentData student = postSnapshot.getValue(StudentData.class);

                    students.add(student);
                   // keylist.add(postSnapshot.getKey());
                    studentref = FirebaseDatabase.getInstance().getReference()
                            .child("SCHOOL")
                            .child(schoolname)
                            .child("STUDENTS")
                            .child("ATTANDENCE")
                            .child(classname)
                            .child(section)
                            .child(date).child(requireNonNull(postSnapshot.getKey()));
                    studentref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           attandence.add((String)dataSnapshot.getValue());


                      }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    mainListView.setAdapter(Adapter);

                }



            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void load_class(String Name) {

        FirebaseDatabase.getInstance().getReference().child("SCHOOL").child(Name).child("STUDENTS").child("PERSONAL_DATA").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> classlist = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    //here is your every post
                    classlist.add(snapshot.getKey());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireNonNull(context), android.R.layout.simple_spinner_item, classlist);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp_class.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                classname=classlist.get(0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    private void init() {

        mainListView = v. findViewById(R.id.mainListView);
        etdate=v.findViewById(R.id.et_date);
        sp_class=v.findViewById(R.id.spn_class);
        sp_section=v.findViewById(R.id.spn_section);
    }
    private String get_current_time(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return sdf.format(new Date());
    }

}
