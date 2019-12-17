package com.p4u.parvarish.Attandence.admin;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.p4u.parvarish.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MarkAttandenceFragment extends Fragment {
    private DatabaseReference studentref;
    private ListView mainListView;
    private List<StudentData> students;
    private ArrayList <String> checkedValue;
    private StudentArrayAdapter Adapter;
    private ImageButton check;
    private int mYear,mMonth,mDay,count;
    private Spinner sp_class,sp_section;
    private String date,schoolname;
    private DatabaseReference dR;
    private Bundle bundle;
    private EditText etdate;
    private Context context;
    private String TAG="new";
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_markattandence, container, false);
        initViews();
        bundle=new Bundle();
        schoolname = (Objects.requireNonNull(this.getArguments())).getString("SCHOOL_NAME");
        //schoolname="RD public school";
        context = container.getContext();
        students = new ArrayList<>();

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
        sp_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                load_list();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sp_section.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                load_list();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View item,
        int position, long id) {

            StudentData stu=Adapter.getItem(position);
            assert stu != null;
            stu.toggleChecked();
            StudentViewHolder viewHolder = (StudentViewHolder) item.getTag();
            viewHolder.getCheckBox().setChecked(stu.isChecked());
        }
    });

        check.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dR= FirebaseDatabase.getInstance().getReference().child("SCHOOL")
                        .child("ATTANDENCE")
                        .child(schoolname)
                        .child(sp_class.getSelectedItem().toString())
                        .child(sp_section.getSelectedItem().toString())
                        .child(etdate.getText().toString());
               // String attandenceId = dR.push().getKey();
               // dR.child("ID").setValue(attandenceId);
                for (int i = 0; i < Adapter.getCount(); i++) {
                    StudentData student = Adapter.getItem(i);
                    assert student != null;
                    if (student.isChecked()) {

                       // Log.d(TAG,"hello"+student.getStudentName());
                        dR.child("STU"+i).setValue(student.getStudentadmissionno());

                    }else{
                        dR.child("STU"+i).removeValue();
                    }
                }
                Toast.makeText(context, "Attandence Submitted", Toast.LENGTH_LONG).show();

           }
        });
        return v;
    }



    public void load_list() {

        super.onStart();
        studentref= FirebaseDatabase.getInstance().getReference().child("SCHOOL").child("STUDENTS");
        studentref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                students.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    StudentData student = postSnapshot.getValue(StudentData.class);
                    if(sp_class.getSelectedItem().equals(Objects.requireNonNull(student).getStudentclass())&& sp_section.getSelectedItem().equals(student.getStudentsection())) {
                        students.add(student);
                    }

                }

                if(getActivity()!=null) {
                    Adapter = new StudentArrayAdapter(getActivity(), students);
                    mainListView.setAdapter(Adapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void initViews() {
        check = v.findViewById(R.id.check);
        mainListView = v. findViewById(R.id.mainListView);
        etdate=v.findViewById(R.id.et_date);
        sp_class=v.findViewById(R.id.spn_class);
        sp_section=v.findViewById(R.id.spn_section);
    }


    public Object onRetainNonConfigurationInstance(){
        return students;
    }
    private String get_current_time(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return sdf.format(new Date());
    }
}