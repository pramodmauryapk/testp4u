package com.p4u.parvarish.Attandence.raw;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.p4u.parvarish.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class EnterstudentdataFragment extends Fragment {

    private DatabaseReference mDatabase;
    private EditText etStudentname, etClassname, etSectionname;
    private Button btnAddstudent, btnDeletestudent;


    public EnterstudentdataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_enterstudentdata, container, false);

        etClassname = (EditText) view.findViewById(R.id.et_classname);
        etStudentname = (EditText) view.findViewById(R.id.et_studentname);
        etSectionname = (EditText) view.findViewById(R.id.et_sectionname);

        btnAddstudent = (Button) view.findViewById(R.id.btn_addstudent);
        btnDeletestudent = (Button) view.findViewById(R.id.btn_deletestudent);

       // Firebase object
        mDatabase = FirebaseDatabase.getInstance().getReference();


        btnAddstudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String studentname = etStudentname.getText().toString();
                String classname = etClassname.getText().toString();
                String sectionname = etSectionname.getText().toString();

                if(TextUtils.isEmpty(studentname)){
                    Toast.makeText(getActivity(), "Enter student name!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(classname)){
                    Toast.makeText(getActivity(), "Enter class name!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(sectionname)){
                    Toast.makeText(getActivity(), "Enter section name!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // if case for class 12th

                if(classname.equals("12") && sectionname.equals("A")){
                    // Write student data into firebase database
                    writeStudentdata12A(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("12") && sectionname.equals("B")){
                    // Write student data into firebase database
                    writeStudentdata12B(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("12") && sectionname.equals("C")){
                    // Write student data into firebase database
                    writeStudentdata12C(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("12") && sectionname.equals("D")){
                    // Write student data into firebase database
                    writeStudentdata12D(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("12") && sectionname.equals("E")){
                    // Write student data into firebase database
                    writeStudentdata12E(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("12") && sectionname.equals("F")){
                    // Write student data into firebase database
                    writeStudentdata12F(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("12") && sectionname.equals("G")){
                    // Write student data into firebase database
                    writeStudentdata12H(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("12") && sectionname.equals("H")){
                    // Write student data into firebase database
                    writeStudentdata12H(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }


                // if case for class 11th

                if(classname.equals("11") && sectionname.equals("A")){
                    // Write student data into firebase database
                    writeStudentdata11A(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("11") && sectionname.equals("B")){
                    // Write student data into firebase database
                    writeStudentdata11B(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("11") && sectionname.equals("C")){
                    // Write student data into firebase database
                    writeStudentdata11C(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("11") && sectionname.equals("D")){
                    // Write student data into firebase database
                    writeStudentdata11D(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("11") && sectionname.equals("E")){
                    // Write student data into firebase database
                    writeStudentdata11E(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("11") && sectionname.equals("F")){
                    // Write student data into firebase database
                    writeStudentdata11F(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("11") && sectionname.equals("G")){
                    // Write student data into firebase database
                    writeStudentdata11H(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("11") && sectionname.equals("H")){
                    // Write student data into firebase database
                    writeStudentdata11H(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                // if case for class 10th

                if(classname.equals("10") && sectionname.equals("A")){
                    // Write student data into firebase database
                    writeStudentdata10A(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("10") && sectionname.equals("B")){
                    // Write student data into firebase database
                    writeStudentdata10B(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("10") && sectionname.equals("C")){
                    // Write student data into firebase database
                    writeStudentdata10C(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("10") && sectionname.equals("D")){
                    // Write student data into firebase database
                    writeStudentdata10D(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("10") && sectionname.equals("E")){
                    // Write student data into firebase database
                    writeStudentdata10E(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("10") && sectionname.equals("F")){
                    // Write student data into firebase database
                    writeStudentdata10F(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("10") && sectionname.equals("G")){
                    // Write student data into firebase database
                    writeStudentdata10H(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("10") && sectionname.equals("H")){
                    // Write student data into firebase database
                    writeStudentdata10H(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                // if case for class 9th

                if(classname.equals("9") && sectionname.equals("A")){
                    // Write student data into firebase database
                    writeStudentdata9A(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("9") && sectionname.equals("B")){
                    // Write student data into firebase database
                    writeStudentdata9B(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("9") && sectionname.equals("C")){
                    // Write student data into firebase database
                    writeStudentdata9C(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("9") && sectionname.equals("D")){
                    // Write student data into firebase database
                    writeStudentdata9D(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("9") && sectionname.equals("E")){
                    // Write student data into firebase database
                    writeStudentdata9E(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("9") && sectionname.equals("F")){
                    // Write student data into firebase database
                    writeStudentdata9F(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("9") && sectionname.equals("G")){
                    // Write student data into firebase database
                    writeStudentdata9H(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("9") && sectionname.equals("H")){
                    // Write student data into firebase database
                    writeStudentdata9H(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                // if case for class 8th

                if(classname.equals("8") && sectionname.equals("A")){
                    // Write student data into firebase database
                    writeStudentdata8A(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("8") && sectionname.equals("B")){
                    // Write student data into firebase database
                    writeStudentdata8B(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("8") && sectionname.equals("C")){
                    // Write student data into firebase database
                    writeStudentdata8C(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("8") && sectionname.equals("D")){
                    // Write student data into firebase database
                    writeStudentdata8D(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("8") && sectionname.equals("E")){
                    // Write student data into firebase database
                    writeStudentdata8E(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("8") && sectionname.equals("F")){
                    // Write student data into firebase database
                    writeStudentdata8F(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("8") && sectionname.equals("G")){
                    // Write student data into firebase database
                    writeStudentdata8H(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("8") && sectionname.equals("H")){
                    // Write student data into firebase database
                    writeStudentdata8H(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                // if case for class 7th

                if(classname.equals("7") && sectionname.equals("A")){
                    // Write student data into firebase database
                    writeStudentdata7A(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("7") && sectionname.equals("B")){
                    // Write student data into firebase database
                    writeStudentdata7B(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("7") && sectionname.equals("C")){
                    // Write student data into firebase database
                    writeStudentdata7C(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("7") && sectionname.equals("D")){
                    // Write student data into firebase database
                    writeStudentdata7D(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("7") && sectionname.equals("E")){
                    // Write student data into firebase database
                    writeStudentdata7E(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("7") && sectionname.equals("F")){
                    // Write student data into firebase database
                    writeStudentdata7F(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("7") && sectionname.equals("G")){
                    // Write student data into firebase database
                    writeStudentdata9H(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("7") && sectionname.equals("H")){
                    // Write student data into firebase database
                    writeStudentdata7H(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                // if case for class 6th

                if(classname.equals("6") && sectionname.equals("A")){
                    // Write student data into firebase database
                    writeStudentdata6A(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("6") && sectionname.equals("B")){
                    // Write student data into firebase database
                    writeStudentdata6B(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("6") && sectionname.equals("C")){
                    // Write student data into firebase database
                    writeStudentdata6C(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("6") && sectionname.equals("D")){
                    // Write student data into firebase database
                    writeStudentdata6D(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("6") && sectionname.equals("E")){
                    // Write student data into firebase database
                    writeStudentdata6E(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("6") && sectionname.equals("F")){
                    // Write student data into firebase database
                    writeStudentdata6F(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("6") && sectionname.equals("G")){
                    // Write student data into firebase database
                    writeStudentdata6H(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("6") && sectionname.equals("H")){
                    // Write student data into firebase database
                    writeStudentdata6H(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                // if case for class 5th

                if(classname.equals("5") && sectionname.equals("A")){
                    // Write student data into firebase database
                    writeStudentdata5A(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("5") && sectionname.equals("B")){
                    // Write student data into firebase database
                    writeStudentdata5B(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("5") && sectionname.equals("C")){
                    // Write student data into firebase database
                    writeStudentdata5C(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("5") && sectionname.equals("D")){
                    // Write student data into firebase database
                    writeStudentdata5D(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("5") && sectionname.equals("E")){
                    // Write student data into firebase database
                    writeStudentdata5E(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("5") && sectionname.equals("F")){
                    // Write student data into firebase database
                    writeStudentdata5F(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("5") && sectionname.equals("G")){
                    // Write student data into firebase database
                    writeStudentdata5H(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("5") && sectionname.equals("H")){
                    // Write student data into firebase database
                    writeStudentdata5H(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }


                // if case for class 4th

                if(classname.equals("4") && sectionname.equals("A")){
                    // Write student data into firebase database
                    writeStudentdata4A(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("4") && sectionname.equals("B")){
                    // Write student data into firebase database
                    writeStudentdata4B(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("4") && sectionname.equals("C")){
                    // Write student data into firebase database
                    writeStudentdata4C(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("4") && sectionname.equals("D")){
                    // Write student data into firebase database
                    writeStudentdata4D(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("4") && sectionname.equals("E")){
                    // Write student data into firebase database
                    writeStudentdata4E(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("4") && sectionname.equals("F")){
                    // Write student data into firebase database
                    writeStudentdata4F(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("4") && sectionname.equals("G")){
                    // Write student data into firebase database
                    writeStudentdata4H(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("4") && sectionname.equals("H")){
                    // Write student data into firebase database
                    writeStudentdata4H(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }


                // if case for class 3th

                if(classname.equals("3") && sectionname.equals("A")){
                    // Write student data into firebase database
                    writeStudentdata3A(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("3") && sectionname.equals("B")){
                    // Write student data into firebase database
                    writeStudentdata3B(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("3") && sectionname.equals("C")){
                    // Write student data into firebase database
                    writeStudentdata3C(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("3") && sectionname.equals("D")){
                    // Write student data into firebase database
                    writeStudentdata3D(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("3") && sectionname.equals("E")){
                    // Write student data into firebase database
                    writeStudentdata3E(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("3") && sectionname.equals("F")){
                    // Write student data into firebase database
                    writeStudentdata3F(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("3") && sectionname.equals("G")){
                    // Write student data into firebase database
                    writeStudentdata3H(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("3") && sectionname.equals("H")){
                    // Write student data into firebase database
                    writeStudentdata3H(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }


                // if case for class 2nd

                if(classname.equals("2") && sectionname.equals("A")){
                    // Write student data into firebase database
                    writeStudentdata2A(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("2") && sectionname.equals("B")){
                    // Write student data into firebase database
                    writeStudentdata2B(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("2") && sectionname.equals("C")){
                    // Write student data into firebase database
                    writeStudentdata2C(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("2") && sectionname.equals("D")){
                    // Write student data into firebase database
                    writeStudentdata2D(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("2") && sectionname.equals("E")){
                    // Write student data into firebase database
                    writeStudentdata2E(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("2") && sectionname.equals("F")){
                    // Write student data into firebase database
                    writeStudentdata2F(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("2") && sectionname.equals("G")){
                    // Write student data into firebase database
                    writeStudentdata2H(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("2") && sectionname.equals("H")){
                    // Write student data into firebase database
                    writeStudentdata2H(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                // if case for class 1st

                if(classname.equals("1") && sectionname.equals("A")){
                    // Write student data into firebase database
                    writeStudentdata1A(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("1") && sectionname.equals("B")){
                    // Write student data into firebase database
                    writeStudentdata1B(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("1") && sectionname.equals("C")){
                    // Write student data into firebase database
                    writeStudentdata1C(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("1") && sectionname.equals("D")){
                    // Write student data into firebase database
                    writeStudentdata1D(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("1") && sectionname.equals("E")){
                    // Write student data into firebase database
                    writeStudentdata1E(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("1") && sectionname.equals("F")){
                    // Write student data into firebase database
                    writeStudentdata1F(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("1") && sectionname.equals("G")){
                    // Write student data into firebase database
                    writeStudentdata1H(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("1") && sectionname.equals("H")){
                    // Write student data into firebase database
                    writeStudentdata1H(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                // if case for class UKG

                if(classname.equals("UKG") && sectionname.equals("A")){
                    // Write student data into firebase database
                    writeStudentdataUKGA(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("UKG") && sectionname.equals("B")){
                    // Write student data into firebase database
                    writeStudentdataUKGB(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("UKG") && sectionname.equals("C")){
                    // Write student data into firebase database
                    writeStudentdataUKGC(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("UKG") && sectionname.equals("D")){
                    // Write student data into firebase database
                    writeStudentdataUKGD(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("UKG") && sectionname.equals("E")){
                    // Write student data into firebase database
                    writeStudentdataUKGE(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("UKG") && sectionname.equals("F")){
                    // Write student data into firebase database
                    writeStudentdataUKGF(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("UKG") && sectionname.equals("G")){
                    // Write student data into firebase database
                    writeStudentdataUKGH(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("UKG") && sectionname.equals("H")){
                    // Write student data into firebase database
                    writeStudentdataUKGH(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                // if case for class LKG

                if(classname.equals("LKG") && sectionname.equals("A")){
                    // Write student data into firebase database
                    writeStudentdataLKGA(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("LKG") && sectionname.equals("B")){
                    // Write student data into firebase database
                    writeStudentdataLKGB(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("LKG") && sectionname.equals("C")){
                    // Write student data into firebase database
                    writeStudentdataLKGC(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("LKG") && sectionname.equals("D")){
                    // Write student data into firebase database
                    writeStudentdataLKGD(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("LKG") && sectionname.equals("E")){
                    // Write student data into firebase database
                    writeStudentdataLKGE(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("LKG") && sectionname.equals("F")){
                    // Write student data into firebase database
                    writeStudentdataLKGF(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("LKG") && sectionname.equals("G")){
                    // Write student data into firebase database
                    writeStudentdataLKGH(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

                if(classname.equals("LKG") && sectionname.equals("H")){
                    // Write student data into firebase database
                    writeStudentdataLKGH(studentname, classname, sectionname);
                    Toast.makeText(getActivity(), "Student data added successfully", Toast.LENGTH_LONG).show();

                }

            }
        });

// return view of fragement
        return view;
    }

    private void writeStudentdata12A(String studentname, String classname, String sectionname) {
      //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class12/sectionA").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata12B(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class12/sectionB").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata12C(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class12/sectionC").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata12D(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class12/sectionD").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata12E(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class12/sectionE").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata12F(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class12/sectionF").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata12G(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class12/sectionG").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata12H(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class12/sectionH").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }


    // 11th class starts here

    private void writeStudentdata11A(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class11/sectionA").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata11B(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class11/sectionB").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata11C(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class11/sectionC").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata11D(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class11/sectionD").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata11E(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class11/sectionE").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata11F(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class11/sectionF").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata11G(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class11/sectionG").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata11H(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class11/sectionH").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    // 10th Class starts here

    private void writeStudentdata10A(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class10/sectionA").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata10B(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class10/sectionB").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata10C(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class10/sectionC").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata10D(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class10/sectionD").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata10E(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class10/sectionE").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata10F(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class10/sectionF").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata10G(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class10/sectionG").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata10H(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class10/sectionH").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    // class 9th starts here

    private void writeStudentdata9A(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class9/sectionA").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata9B(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class9/sectionB").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata9C(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class9/sectionC").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata9D(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class9/sectionD").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata9E(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class9/sectionE").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata9F(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class9/sectionF").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata9G(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class9/sectionG").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata9H(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class9/sectionH").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    // class 8th starts here

    private void writeStudentdata8A(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class8/sectionA").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata8B(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class8/sectionB").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata8C(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class8/sectionC").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata8D(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class8/sectionD").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata8E(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class8/sectionE").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata8F(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class8/sectionF").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata8G(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class8/sectionG").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata8H(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class8/sectionH").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    // class 7th starts here

    private void writeStudentdata7A(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class7/sectionA").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata7B(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class7/sectionB").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata7C(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class7/sectionC").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata7D(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class7/sectionD").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata7E(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class7/sectionE").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata7F(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class7/sectionF").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata7G(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class7/sectionG").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata7H(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class7/sectionH").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    // Class 6th starts here

    private void writeStudentdata6A(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class6/sectionA").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata6B(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class6/sectionB").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata6C(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class6/sectionC").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata6D(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class6/sectionD").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata6E(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class6/sectionE").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata6F(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class6/sectionF").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata6G(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class6/sectionG").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata6H(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class6/sectionH").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    // Class 5th starts here

    private void writeStudentdata5A(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class5/sectionA").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata5B(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class5/sectionB").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata5C(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class5/sectionC").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata5D(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class5/sectionD").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata5E(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class5/sectionE").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata5F(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class5/sectionF").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata5G(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class5/sectionG").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata5H(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class5/sectionH").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    // Class 4th starts here

    private void writeStudentdata4A(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class4/sectionA").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata4B(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class4/sectionB").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata4C(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class4/sectionC").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata4D(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class4/sectionD").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata4E(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class4/sectionE").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata4F(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class4/sectionF").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata4G(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class4/sectionG").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata4H(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class4/sectionH").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    // Class 3rd starts here

    private void writeStudentdata3A(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class3/sectionA").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata3B(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class3/sectionB").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata3C(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class3/sectionC").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata3D(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class3/sectionD").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata3E(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class3/sectionE").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata3F(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class3/sectionF").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata3G(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class3/sectionG").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata3H(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class3/sectionH").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    // Class 2nd starts here

    private void writeStudentdata2A(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class2/sectionA").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata2B(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class2/sectionB").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata2C(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class2/sectionC").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata2D(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class2/sectionD").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata2E(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class2/sectionE").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata2F(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class2/sectionF").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata2G(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class2/sectionG").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata2H(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class2/sectionH").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    // Class 1st starts here

    private void writeStudentdata1A(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class1/sectionA").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata1B(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class1/sectionB").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata1C(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class1/sectionC").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata1D(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class1/sectionD").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata1E(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class1/sectionE").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata1F(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class1/sectionF").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata1G(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class1/sectionG").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdata1H(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/class1/sectionH").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    // Class UKG starts here

    private void writeStudentdataUKGA(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/classUKG/sectionA").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdataUKGB(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/classUKG/sectionB").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdataUKGC(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/classUKG/sectionC").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdataUKGD(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/classUKG/sectionD").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdataUKGE(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/classUKG/sectionE").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdataUKGF(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/classUKG/sectionF").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdataUKGG(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/classUKG/sectionG").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdataUKGH(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/classUKG/sectionH").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    // Class LKG starts here

    private void writeStudentdataLKGA(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/classLKG/sectionA").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdataLKGB(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/classLKG/sectionB").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdataLKGC(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/classLKG/sectionC").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdataLKGD(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/classLKG/sectionD").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdataLKGE(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/classLKG/sectionE").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdataLKGF(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/classLKG/sectionF").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdataLKGG(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/classLKG/sectionG").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

    private void writeStudentdataLKGH(String studentname, String classname, String sectionname) {
        //  StudentInfo studentInfo = new StudentInfo(studentname, classname, sectionname);
        mDatabase.child("users/studentdata/classLKG/sectionH").push().setValue(new StudentInfo(studentname, classname, sectionname));
    }

}
