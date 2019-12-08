package com.p4u.parvarish.Attandence.admin;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class MarkAttendanceFragment extends Fragment implements StudentList_model.OnItemClickListener {

    private static String TAG = "rcdattendancefragment";
    private ListView listViewstudent;
    private List<StudentData> students;
    private ArrayList <String> checkedValue;
    private EditText etdate;
    private View dialogView;
    private DatabaseReference studentref;
    private View v;
    private Context context;
    private int mYear,mMonth,mDay,count;
    private Spinner sp_class,sp_section;
    private ImageButton btnImg;
    public String date;
    public MarkAttendanceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_mark_attendance, container, false);
        Log.d(TAG,"onCreate: starting onCreate");
        studentref= FirebaseDatabase.getInstance().getReference().child("SCHOOL").child("STUDENTS");
        context = container.getContext();
        initViews();
        students = new ArrayList<>();
        listViewstudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                StudentData student = students.get(i);
                Toast.makeText(getContext(),"hello "+i,Toast.LENGTH_LONG).show();
                /*showDeleteDialog(
                        //  user.getUserId (),
                        user.getUserName(),
                        user.getUserEmail(),
                        user.getUserRole(),
                        user.getUserMobile(),
                        user.getUserAddress(),
                        user.getUserIdentity());*/
            }
        });

        btnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"" + checkedValue,Toast.LENGTH_LONG).show();
            }
        });
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

                                etdate.setText(String.format("%d/%d/%d", dayOfMonth, monthOfYear + 1, year));

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
        //Fragment view return
        return v;
    }
    private void initViews(){

        listViewstudent = v.findViewById(R.id.lv_studentlist);
        etdate=v.findViewById(R.id.et_date);
        sp_class=v.findViewById(R.id.spn_class);
        sp_section=v.findViewById(R.id.spn_section);
        btnImg=v.findViewById(R.id.btn_submitattendence);


    }
    public void load_list() {

        //super.onStart();
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
                    StudentList_model Adapter = new StudentList_model(getActivity(), students);
                    listViewstudent.setAdapter(Adapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private String get_current_time(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(new Date());
    }

    @Override
    public void onItemClick(int position) {
        //Toast.makeText(getContext(),"hello",Toast.LENGTH_LONG).show();
        CheckBox cb = (CheckBox) v.findViewById(R.id.checkbox1);
        TextView tv = (TextView) v.findViewById(R.id.textView1);
        cb.performClick();
        if (cb.isChecked()) {

            checkedValue.add(tv.getText().toString());
        } else if (!cb.isChecked()) {
            checkedValue.remove(tv.getText().toString());
        }
    }
}
