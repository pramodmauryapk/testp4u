package com.p4u.parvarish.Attandence.Teacher;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.p4u.parvarish.R;
import com.p4u.parvarish.Attandence.UserInformation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecordAttendanceFragment extends Fragment {

    private static String TAG = "rcdattendancefragment";
    private Spinner spinnerClass,spinnerSection,spinnerSubject;
    private ListView studentListView;
    private CheckBox chkAttendance;
    private ImageButton btnsubmitAttendance;

    // String array lists
    ArrayList<String> selectedItems=new ArrayList<String>();
    List<Student> studentList = new ArrayList<>();
    List<String> classnames = new ArrayList<String>();
    List<String> sectionnames = new ArrayList<String>();
    List<String> subjectnames = new ArrayList<String>();

    // Firebase variable declartion
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private FirebaseUser user = mAuth.getCurrentUser();
    String userID = user.getUid();


    public RecordAttendanceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_record_attendance, container, false);
        Log.d(TAG,"onCreate: starting onCreate");

       // Initialize variables
        spinnerClass = (Spinner) view.findViewById(R.id.spn_class);
        spinnerSection = (Spinner) view.findViewById(R.id.spn_section);
        spinnerSubject = (Spinner) view.findViewById(R.id.spn_subject);
        studentListView = (ListView) view.findViewById(R.id.lv_studentlist);
        chkAttendance = (CheckBox) view.findViewById(R.id.checkbox_attendance);
        btnsubmitAttendance = (ImageButton) view.findViewById(R.id.btn_submitattendence);


        //Declare database reference
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();


        //Access spinner data like class, section and subject from user database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showData(dataSnapshot);
                Log.d(TAG, "Accessed spinner data like class, section and subject from user database");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        // On item click event on class, section and subject spinners
        spinnerClass.setOnItemSelectedListener(new MyOnItemSelectedListener());
        spinnerSubject.setOnItemSelectedListener(new MyOnItemSelectedListener());
        spinnerSection.setOnItemSelectedListener(new MyOnItemSelectedListener());

        // On item click event on student listview
        studentListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        studentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.d(TAG,"OnItemClick: itemclicked");
                CheckBox cb = (CheckBox) view.findViewById(R.id.checkbox_attendance);
                TextView tv = (TextView) view.findViewById(R.id.tv_studentnameforlist);
                cb.performClick();
                cb.setTag(position);
                if (cb.isChecked()) {

                    selectedItems.add(tv.getText().toString());
                } else if (!cb.isChecked()) {
                    selectedItems.remove(tv.getText().toString());
                }
            }
        });

        // Submit attendance button
        btnsubmitAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"btnsubmitAttendace: Clicked");
                // Alert dialog before attendance submission
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.custom_alertdialog_sbmtattendance, null);
                alertDialogBuilder.setView(dialogView);

                // Variable declaration
                final TextView tvTotal = (TextView) dialogView.findViewById(R.id.et_totalstudent);
                final TextView tvPresent = (TextView) dialogView.findViewById(R.id.et_presentstudent);
                final TextView tvAbsent = (TextView) dialogView.findViewById(R.id.et_absentstudent);

                // Counting setting total, present and absent student in textview
                final int presentcount = selectedItems.size();
                int totalcount = studentListView.getAdapter().getCount();
                int absent = totalcount - presentcount;
                tvPresent.setText(String.valueOf(presentcount));
                tvTotal.setText(String.valueOf(totalcount));
                tvAbsent.setText(String.valueOf(absent));

                //Alert dialog setting
                alertDialogBuilder.setTitle("Submit attendance");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String allItems = ""; //used to display in the toast
                            Log.d(TAG,"alertDialog: submit attendance");
                            for (String str : selectedItems) {
                                allItems = allItems + "\n" + str; //adds a new line between items
                            }
                            // Selected class, section and subject
                            String classselected = spinnerClass.getSelectedItem().toString();
                            String sectionselected = spinnerSection.getSelectedItem().toString();
                            String subjectselected = spinnerSubject.getSelectedItem().toString();

                            // Current date format in year, month and day
                            Date date = new Date();
                            SimpleDateFormat dateFormatyear = new SimpleDateFormat("yyyy");
                            SimpleDateFormat dateFormatmonth = new SimpleDateFormat("MMMM");
                            SimpleDateFormat dateFormatday = new SimpleDateFormat("dd");
                            String year = dateFormatyear.format(date).toString();
                            String month = dateFormatmonth.format(date).toString();
                            String day = dateFormatday.format(date).toString();
                            myRef = mFirebaseDatabase.getReference("users/attendance/" + year + "/" + month + "/" + day + "/" + classselected + "/" + sectionselected + "/" + subjectselected);
                            myRef.setValue(allItems);
                            Toast.makeText(getActivity(), "Attendance data saved successfully", Toast.LENGTH_SHORT).show();}

                    });

                    alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                }

        });
        //Fragment view return
        return view;
    }


    //Function to access data like class, section, subject and setting it into spinner array
    private void showData(DataSnapshot dataSnapshot) {
        Log.d(TAG,"showData: starting showData");
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            UserInformation uInfo = new UserInformation();
            // Set and get class names of current teacher
            uInfo.setClassname1(ds.child(userID).getValue(UserInformation.class).getClassname1());
            String classname1 = uInfo.getClassname1();
            uInfo.setClassname2(ds.child(userID).getValue(UserInformation.class).getClassname2());
            String classname2 = uInfo.getClassname2();
            uInfo.setClassname3(ds.child(userID).getValue(UserInformation.class).getClassname3());
            String classname3 = uInfo.getClassname3();
            uInfo.setClassname4(ds.child(userID).getValue(UserInformation.class).getClassname4());
            String classname4 = uInfo.getClassname4();
            uInfo.setClassname5(ds.child(userID).getValue(UserInformation.class).getClassname5());
            String classname5 = uInfo.getClassname5();
            uInfo.setClassname6(ds.child(userID).getValue(UserInformation.class).getClassname6());
            String classname6 = uInfo.getClassname6();
            uInfo.setClassname7(ds.child(userID).getValue(UserInformation.class).getClassname7());
            String classname7 = uInfo.getClassname7();
            uInfo.setClassname8(ds.child(userID).getValue(UserInformation.class).getClassname8());
            String classname8 = uInfo.getClassname8();
            uInfo.setClassname9(ds.child(userID).getValue(UserInformation.class).getClassname9());
            String classname9 = uInfo.getClassname9();
            uInfo.setClassname10(ds.child(userID).getValue(UserInformation.class).getClassname10());
            String classname10 = uInfo.getClassname10();
            uInfo.setClassname11(ds.child(userID).getValue(UserInformation.class).getClassname11());
            String classname11 = uInfo.getClassname11();
            uInfo.setClassname12(ds.child(userID).getValue(UserInformation.class).getClassname12());
            String classname12 = uInfo.getClassname12();
            uInfo.setClassnameUKG(ds.child(userID).getValue(UserInformation.class).getClassnameUKG());
            String classnameUKG = uInfo.getClassnameUKG();
            uInfo.setClassnameLKG(ds.child(userID).getValue(UserInformation.class).getClassnameLKG());
            String classnameLKG = uInfo.getClassnameLKG();


            // Set and get section names of current teacher
            uInfo.setSectionA(ds.child(userID).getValue(UserInformation.class).getSectionA());
            String sectionA = uInfo.getSectionA();
            uInfo.setSectionB(ds.child(userID).getValue(UserInformation.class).getSectionB());
            String sectionB = uInfo.getSectionB();
            uInfo.setSectionC(ds.child(userID).getValue(UserInformation.class).getSectionC());
            String sectionC = uInfo.getSectionC();
            uInfo.setSectionD(ds.child(userID).getValue(UserInformation.class).getSectionD());
            String sectionD = uInfo.getSectionD();
            uInfo.setSectionE(ds.child(userID).getValue(UserInformation.class).getSectionE());
            String sectionE = uInfo.getSectionE();
            uInfo.setSectionF(ds.child(userID).getValue(UserInformation.class).getSectionF());
            String sectionF = uInfo.getSectionF();
            uInfo.setSectionG(ds.child(userID).getValue(UserInformation.class).getSectionG());
            String sectionG = uInfo.getSectionG();
            uInfo.setSectionH(ds.child(userID).getValue(UserInformation.class).getSectionH());
            String sectionH = uInfo.getSectionH();

            // Set and get subject names of current teacher
            uInfo.setSubject1(ds.child(userID).getValue(UserInformation.class).getSubject1());
            String subject1 = uInfo.getSubject1();
            uInfo.setSubject2(ds.child(userID).getValue(UserInformation.class).getSubject2());
            String subject2 = uInfo.getSubject2();
            uInfo.setSubject2(ds.child(userID).getValue(UserInformation.class).getSubject3());
            String subject3 = uInfo.getSubject3();
            uInfo.setSubject2(ds.child(userID).getValue(UserInformation.class).getSubject4());
            String subject4 = uInfo.getSubject4();
            uInfo.setSubject2(ds.child(userID).getValue(UserInformation.class).getSubject5());
            String subject5 = uInfo.getSubject5();
            uInfo.setSubject2(ds.child(userID).getValue(UserInformation.class).getSubject6());
            String subject6 = uInfo.getSubject6();
            uInfo.setSubject2(ds.child(userID).getValue(UserInformation.class).getSubject7());
            String subject7 = uInfo.getSubject7();
            uInfo.setSubject2(ds.child(userID).getValue(UserInformation.class).getSubject8());
            String subject8 = uInfo.getSubject8();
            uInfo.setSubject2(ds.child(userID).getValue(UserInformation.class).getSubject9());
            String subject9 = uInfo.getSubject9();
            uInfo.setSubject2(ds.child(userID).getValue(UserInformation.class).getSubject10());
            String subject10 = uInfo.getSubject10();


            // Add classname in array
            if(classname1!=null){classnames.add(classname1);}
            if(classname2!=null){classnames.add(classname2);}
            if(classname3!=null){classnames.add(classname3);}
            if(classname4!=null){classnames.add(classname4);}
            if(classname5!=null){classnames.add(classname5);}
            if(classname6!=null){classnames.add(classname6);}
            if(classname7!=null){classnames.add(classname7);}
            if(classname8!=null){classnames.add(classname8);}
            if(classname9!=null){classnames.add(classname9);}
            if(classname10!=null){classnames.add(classname10);}
            if(classname11!=null){classnames.add(classname11);}
            if(classname12!=null){classnames.add(classname12);}
            if(classnameUKG!=null){classnames.add(classnameUKG);}
            if(classnameLKG!=null){classnames.add(classnameLKG);}


            // Add section name in array
            if(sectionA!=null){sectionnames.add(sectionA);}
            if(sectionB!=null){sectionnames.add(sectionB);}
            if(sectionC!=null){sectionnames.add(sectionC);}
            if(sectionD!=null){sectionnames.add(sectionD);}
            if(sectionE!=null){sectionnames.add(sectionE);}
            if(sectionF!=null){sectionnames.add(sectionF);}
            if(sectionG!=null){sectionnames.add(sectionG);}
            if(sectionH!=null){sectionnames.add(sectionH);}


            // Add subject name in array
            if(subject1!=null){subjectnames.add(subject1);}
            if(subject2!=null){subjectnames.add(subject2);}
            if(subject3!=null){subjectnames.add(subject3);}
            if(subject4!=null){subjectnames.add(subject4);}
            if(subject5!=null){subjectnames.add(subject5);}
            if(subject6!=null){subjectnames.add(subject6);}
            if(subject7!=null){subjectnames.add(subject7);}
            if(subject8!=null){subjectnames.add(subject8);}
            if(subject9!=null){subjectnames.add(subject9);}
            if(subject10!=null){subjectnames.add(subject10);}

        }
        // Creating adapter for class spinner
        ArrayAdapter<String> classnamesAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, classnames);

        // Drop down layout style - list view with radio button for class spinner
        classnamesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to class spinner
        spinnerClass.setAdapter(classnamesAdapter);

        // Creating adapter for class spinner
        ArrayAdapter<String> sectionnamesAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, sectionnames);

        // Drop down layout style - list view with radio button for class spinner
        sectionnamesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to class spinner
        spinnerSection.setAdapter(sectionnamesAdapter);

        // Creating adapter for class spinner
        ArrayAdapter<String> subjectnamesAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, subjectnames);

        // Drop down layout style - list view with radio button for class spinner
        subjectnamesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to class spinner
        spinnerSubject.setAdapter(subjectnamesAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();

    }


    // Inner class MyOnItemSelectedListener for spinner item click handling
    private class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            Log.d(TAG,"MyOnItemSelectedListener: starting");

            // Clear student and selectedItems list
            studentList.clear();
            selectedItems.clear();

            //If statement to display particular class and section student list
            if(spinnerClass.getSelectedItem().equals("12th") && spinnerSection.getSelectedItem().equals("A")){
                displayStudentList12A();
            }
            if(spinnerClass.getSelectedItem().equals("12th") && spinnerSection.getSelectedItem().equals("B")){
                displayStudentList12B();
            }
            if(spinnerClass.getSelectedItem().equals("12th") && spinnerSection.getSelectedItem().equals("C")){
                displayStudentList12C();
            }
            if(spinnerClass.getSelectedItem().equals("12th") && spinnerSection.getSelectedItem().equals("D")){
                displayStudentList12D();
            }
            if(spinnerClass.getSelectedItem().equals("12th") && spinnerSection.getSelectedItem().equals("E")){
                displayStudentList12E();
            }
            if(spinnerClass.getSelectedItem().equals("12th") && spinnerSection.getSelectedItem().equals("F")){
                displayStudentList12F();
            }
            if(spinnerClass.getSelectedItem().equals("12th") && spinnerSection.getSelectedItem().equals("G")){
                displayStudentList12G();
            }
            if(spinnerClass.getSelectedItem().equals("12th") && spinnerSection.getSelectedItem().equals("H")){
                displayStudentList12H();
            }


            if(spinnerClass.getSelectedItem().equals("11th") && spinnerSection.getSelectedItem().equals("A")){
                displayStudentList11A();
            }
            if(spinnerClass.getSelectedItem().equals("11th") && spinnerSection.getSelectedItem().equals("B")){
                displayStudentList11B();
            }
            if(spinnerClass.getSelectedItem().equals("11th") && spinnerSection.getSelectedItem().equals("C")){
                displayStudentList11C();
            }
            if(spinnerClass.getSelectedItem().equals("11th") && spinnerSection.getSelectedItem().equals("D")){
                displayStudentList11D();
            }
            if(spinnerClass.getSelectedItem().equals("11th") && spinnerSection.getSelectedItem().equals("E")){
                displayStudentList11E();
            }
            if(spinnerClass.getSelectedItem().equals("11th") && spinnerSection.getSelectedItem().equals("F")){
                displayStudentList11F();
            }
            if(spinnerClass.getSelectedItem().equals("11th") && spinnerSection.getSelectedItem().equals("G")){
                displayStudentList11G();
            }
            if(spinnerClass.getSelectedItem().equals("11th") && spinnerSection.getSelectedItem().equals("H")){
                displayStudentList11H();
            }


            if(spinnerClass.getSelectedItem().equals("10th") && spinnerSection.getSelectedItem().equals("A")){
                displayStudentList10A();
            }
            if(spinnerClass.getSelectedItem().equals("10th") && spinnerSection.getSelectedItem().equals("B")){
                displayStudentList10B();
            }
            if(spinnerClass.getSelectedItem().equals("10th") && spinnerSection.getSelectedItem().equals("C")){
                displayStudentList10C();
            }
            if(spinnerClass.getSelectedItem().equals("10th") && spinnerSection.getSelectedItem().equals("D")){
                displayStudentList10D();
            }
            if(spinnerClass.getSelectedItem().equals("10th") && spinnerSection.getSelectedItem().equals("E")){
                displayStudentList10E();
            }
            if(spinnerClass.getSelectedItem().equals("10th") && spinnerSection.getSelectedItem().equals("F")){
                displayStudentList10F();
            }
            if(spinnerClass.getSelectedItem().equals("10th") && spinnerSection.getSelectedItem().equals("G")){
                displayStudentList10G();
            }
            if(spinnerClass.getSelectedItem().equals("10th") && spinnerSection.getSelectedItem().equals("H")){
                displayStudentList10H();
            }

            if(spinnerClass.getSelectedItem().equals("9th") && spinnerSection.getSelectedItem().equals("A")){
                displayStudentList9A();
            }
            if(spinnerClass.getSelectedItem().equals("9th") && spinnerSection.getSelectedItem().equals("B")){
                displayStudentList9B();
            }
            if(spinnerClass.getSelectedItem().equals("9th") && spinnerSection.getSelectedItem().equals("C")){
                displayStudentList9C();
            }
            if(spinnerClass.getSelectedItem().equals("9th") && spinnerSection.getSelectedItem().equals("D")){
                displayStudentList9D();
            }
            if(spinnerClass.getSelectedItem().equals("9th") && spinnerSection.getSelectedItem().equals("E")){
                displayStudentList9E();
            }
            if(spinnerClass.getSelectedItem().equals("9th") && spinnerSection.getSelectedItem().equals("F")){
                displayStudentList9F();
            }
            if(spinnerClass.getSelectedItem().equals("9th") && spinnerSection.getSelectedItem().equals("G")){
                displayStudentList9G();
            }
            if(spinnerClass.getSelectedItem().equals("9th") && spinnerSection.getSelectedItem().equals("H")){
                displayStudentList9H();
            }

            if(spinnerClass.getSelectedItem().equals("8th") && spinnerSection.getSelectedItem().equals("A")){
                displayStudentList8A();
            }
            if(spinnerClass.getSelectedItem().equals("8th") && spinnerSection.getSelectedItem().equals("B")){
                displayStudentList8B();
            }
            if(spinnerClass.getSelectedItem().equals("8th") && spinnerSection.getSelectedItem().equals("C")){
                displayStudentList8C();
            }
            if(spinnerClass.getSelectedItem().equals("8th") && spinnerSection.getSelectedItem().equals("D")){
                displayStudentList8D();
            }
            if(spinnerClass.getSelectedItem().equals("8th") && spinnerSection.getSelectedItem().equals("E")){
                displayStudentList8E();
            }
            if(spinnerClass.getSelectedItem().equals("8th") && spinnerSection.getSelectedItem().equals("F")){
                displayStudentList8F();
            }
            if(spinnerClass.getSelectedItem().equals("8th") && spinnerSection.getSelectedItem().equals("G")){
                displayStudentList8G();
            }
            if(spinnerClass.getSelectedItem().equals("8th") && spinnerSection.getSelectedItem().equals("H")){
                displayStudentList8H();
            }

            if(spinnerClass.getSelectedItem().equals("7th") && spinnerSection.getSelectedItem().equals("A")){
                displayStudentList7A();
            }
            if(spinnerClass.getSelectedItem().equals("7th") && spinnerSection.getSelectedItem().equals("B")){
                displayStudentList7B();
            }
            if(spinnerClass.getSelectedItem().equals("7th") && spinnerSection.getSelectedItem().equals("C")){
                displayStudentList7C();
            }
            if(spinnerClass.getSelectedItem().equals("7th") && spinnerSection.getSelectedItem().equals("D")){
                displayStudentList7D();
            }
            if(spinnerClass.getSelectedItem().equals("7th") && spinnerSection.getSelectedItem().equals("E")){
                displayStudentList7E();
            }
            if(spinnerClass.getSelectedItem().equals("7th") && spinnerSection.getSelectedItem().equals("F")){
                displayStudentList7F();
            }
            if(spinnerClass.getSelectedItem().equals("7th") && spinnerSection.getSelectedItem().equals("G")){
                displayStudentList7G();
            }
            if(spinnerClass.getSelectedItem().equals("7th") && spinnerSection.getSelectedItem().equals("H")){
                displayStudentList7H();
            }

            if(spinnerClass.getSelectedItem().equals("6th") && spinnerSection.getSelectedItem().equals("A")){
                displayStudentList6A();
            }
            if(spinnerClass.getSelectedItem().equals("6th") && spinnerSection.getSelectedItem().equals("B")){
                displayStudentList6B();
            }
            if(spinnerClass.getSelectedItem().equals("6th") && spinnerSection.getSelectedItem().equals("C")){
                displayStudentList6C();
            }
            if(spinnerClass.getSelectedItem().equals("6th") && spinnerSection.getSelectedItem().equals("D")){
                displayStudentList6D();
            }
            if(spinnerClass.getSelectedItem().equals("6th") && spinnerSection.getSelectedItem().equals("E")){
                displayStudentList6E();
            }
            if(spinnerClass.getSelectedItem().equals("6th") && spinnerSection.getSelectedItem().equals("F")){
                displayStudentList6F();
            }
            if(spinnerClass.getSelectedItem().equals("6th") && spinnerSection.getSelectedItem().equals("G")){
                displayStudentList6G();
            }
            if(spinnerClass.getSelectedItem().equals("6th") && spinnerSection.getSelectedItem().equals("H")){
                displayStudentList6H();
            }

            if(spinnerClass.getSelectedItem().equals("5th") && spinnerSection.getSelectedItem().equals("A")){
                displayStudentList5A();
            }
            if(spinnerClass.getSelectedItem().equals("5th") && spinnerSection.getSelectedItem().equals("B")){
                displayStudentList5B();
            }
            if(spinnerClass.getSelectedItem().equals("5th") && spinnerSection.getSelectedItem().equals("C")){
                displayStudentList5C();
            }
            if(spinnerClass.getSelectedItem().equals("5th") && spinnerSection.getSelectedItem().equals("D")){
                displayStudentList5D();
            }
            if(spinnerClass.getSelectedItem().equals("5th") && spinnerSection.getSelectedItem().equals("E")){
                displayStudentList5E();
            }
            if(spinnerClass.getSelectedItem().equals("5th") && spinnerSection.getSelectedItem().equals("F")){
                displayStudentList5F();
            }
            if(spinnerClass.getSelectedItem().equals("5th") && spinnerSection.getSelectedItem().equals("G")){
                displayStudentList5G();
            }
            if(spinnerClass.getSelectedItem().equals("5th") && spinnerSection.getSelectedItem().equals("H")){
                displayStudentList5H();
            }

            if(spinnerClass.getSelectedItem().equals("4th") && spinnerSection.getSelectedItem().equals("A")){
                displayStudentList4A();
            }
            if(spinnerClass.getSelectedItem().equals("4th") && spinnerSection.getSelectedItem().equals("B")){
                displayStudentList4B();
            }
            if(spinnerClass.getSelectedItem().equals("4th") && spinnerSection.getSelectedItem().equals("C")){
                displayStudentList4C();
            }
            if(spinnerClass.getSelectedItem().equals("4th") && spinnerSection.getSelectedItem().equals("D")){
                displayStudentList4D();
            }
            if(spinnerClass.getSelectedItem().equals("4th") && spinnerSection.getSelectedItem().equals("E")){
                displayStudentList4E();
            }
            if(spinnerClass.getSelectedItem().equals("4th") && spinnerSection.getSelectedItem().equals("F")){
                displayStudentList4F();
            }
            if(spinnerClass.getSelectedItem().equals("4th") && spinnerSection.getSelectedItem().equals("G")){
                displayStudentList4G();
            }
            if(spinnerClass.getSelectedItem().equals("4th") && spinnerSection.getSelectedItem().equals("H")){
                displayStudentList4H();
            }

            if(spinnerClass.getSelectedItem().equals("3rd") && spinnerSection.getSelectedItem().equals("A")){
                displayStudentList3A();
            }
            if(spinnerClass.getSelectedItem().equals("3rd") && spinnerSection.getSelectedItem().equals("B")){
                displayStudentList3B();
            }
            if(spinnerClass.getSelectedItem().equals("3rd") && spinnerSection.getSelectedItem().equals("C")){
                displayStudentList3C();
            }
            if(spinnerClass.getSelectedItem().equals("3rd") && spinnerSection.getSelectedItem().equals("D")){
                displayStudentList3D();
            }
            if(spinnerClass.getSelectedItem().equals("3rd") && spinnerSection.getSelectedItem().equals("E")){
                displayStudentList3E();
            }
            if(spinnerClass.getSelectedItem().equals("3rd") && spinnerSection.getSelectedItem().equals("F")){
                displayStudentList3F();
            }
            if(spinnerClass.getSelectedItem().equals("3rd") && spinnerSection.getSelectedItem().equals("G")){
                displayStudentList3G();
            }
            if(spinnerClass.getSelectedItem().equals("3rd") && spinnerSection.getSelectedItem().equals("H")){
                displayStudentList3H();
            }

            if(spinnerClass.getSelectedItem().equals("2nd") && spinnerSection.getSelectedItem().equals("A")){
                displayStudentList2A();
            }
            if(spinnerClass.getSelectedItem().equals("2nd") && spinnerSection.getSelectedItem().equals("B")){
                displayStudentList2B();
            }
            if(spinnerClass.getSelectedItem().equals("2nd") && spinnerSection.getSelectedItem().equals("C")){
                displayStudentList2C();
            }
            if(spinnerClass.getSelectedItem().equals("2nd") && spinnerSection.getSelectedItem().equals("D")){
                displayStudentList2D();
            }
            if(spinnerClass.getSelectedItem().equals("2nd") && spinnerSection.getSelectedItem().equals("E")){
                displayStudentList2E();
            }
            if(spinnerClass.getSelectedItem().equals("2nd") && spinnerSection.getSelectedItem().equals("F")){
                displayStudentList2F();
            }
            if(spinnerClass.getSelectedItem().equals("2nd") && spinnerSection.getSelectedItem().equals("G")){
                displayStudentList2G();
            }
            if(spinnerClass.getSelectedItem().equals("2nd") && spinnerSection.getSelectedItem().equals("H")){
                displayStudentList2H();
            }

            if(spinnerClass.getSelectedItem().equals("1st") && spinnerSection.getSelectedItem().equals("A")){
                displayStudentList1A();
            }
            if(spinnerClass.getSelectedItem().equals("1st") && spinnerSection.getSelectedItem().equals("B")){
                displayStudentList1B();
            }
            if(spinnerClass.getSelectedItem().equals("1st") && spinnerSection.getSelectedItem().equals("C")){
                displayStudentList1C();
            }
            if(spinnerClass.getSelectedItem().equals("1st") && spinnerSection.getSelectedItem().equals("D")){
                displayStudentList1D();
            }
            if(spinnerClass.getSelectedItem().equals("1st") && spinnerSection.getSelectedItem().equals("E")){
                displayStudentList1E();
            }
            if(spinnerClass.getSelectedItem().equals("1st") && spinnerSection.getSelectedItem().equals("F")){
                displayStudentList1F();
            }
            if(spinnerClass.getSelectedItem().equals("1st") && spinnerSection.getSelectedItem().equals("G")){
                displayStudentList1G();
            }
            if(spinnerClass.getSelectedItem().equals("1st") && spinnerSection.getSelectedItem().equals("H")){
                displayStudentList1H();
            }

            if(spinnerClass.getSelectedItem().equals("U.K.G") && spinnerSection.getSelectedItem().equals("A")){
                displayStudentListUA();
            }
            if(spinnerClass.getSelectedItem().equals("U.K.G") && spinnerSection.getSelectedItem().equals("B")){
                displayStudentListUB();
            }
            if(spinnerClass.getSelectedItem().equals("U.K.G") && spinnerSection.getSelectedItem().equals("C")){
                displayStudentListUC();
            }
            if(spinnerClass.getSelectedItem().equals("U.K.G") && spinnerSection.getSelectedItem().equals("D")){
                displayStudentListUD();
            }
            if(spinnerClass.getSelectedItem().equals("U.K.G") && spinnerSection.getSelectedItem().equals("E")){
                displayStudentListUE();
            }
            if(spinnerClass.getSelectedItem().equals("U.K.G") && spinnerSection.getSelectedItem().equals("F")){
                displayStudentListUF();
            }
            if(spinnerClass.getSelectedItem().equals("U.K.G") && spinnerSection.getSelectedItem().equals("G")){
                displayStudentListUG();
            }
            if(spinnerClass.getSelectedItem().equals("U.K.G") && spinnerSection.getSelectedItem().equals("H")){
                displayStudentListUH();
            }

            if(spinnerClass.getSelectedItem().equals("L.K.G") && spinnerSection.getSelectedItem().equals("A")){
                displayStudentListLA();
            }
            if(spinnerClass.getSelectedItem().equals("L.K.G") && spinnerSection.getSelectedItem().equals("B")){
                displayStudentListLB();
            }
            if(spinnerClass.getSelectedItem().equals("L.K.G") && spinnerSection.getSelectedItem().equals("C")){
                displayStudentListLC();
            }
            if(spinnerClass.getSelectedItem().equals("L.K.G") && spinnerSection.getSelectedItem().equals("D")){
                displayStudentListLD();
            }
            if(spinnerClass.getSelectedItem().equals("L.K.G") && spinnerSection.getSelectedItem().equals("E")){
                displayStudentListLE();
            }
            if(spinnerClass.getSelectedItem().equals("L.K.G") && spinnerSection.getSelectedItem().equals("F")){
                displayStudentListLF();
            }
            if(spinnerClass.getSelectedItem().equals("L.K.G") && spinnerSection.getSelectedItem().equals("G")){
                displayStudentListLG();
            }
            if(spinnerClass.getSelectedItem().equals("L.K.G") && spinnerSection.getSelectedItem().equals("H")){
                displayStudentListLH();
            }


        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }


        private void displayStudentListLH() {
            Log.d(TAG, "displayStudentListLH: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/classL/sectionH");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentListLG() {
            Log.d(TAG, "displayStudentListLG: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/classL/sectionG");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentListLF() {
            Log.d(TAG, "displayStudentListLF: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/classL/sectionF");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentListLE() {
            Log.d(TAG, "displayStudentListLE: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/classL/sectionE");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentListLD() {
            Log.d(TAG, "displayStudentListLD: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/classL/sectionD");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentListLC() {
            Log.d(TAG, "displayStudentListLC: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/classL/sectionC");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentListLB() {
            Log.d(TAG, "displayStudentListLB: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/classL/sectionB");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentListLA() {
            Log.d(TAG, "displayStudentListLA: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/classL/sectionA");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentListUH() {
            Log.d(TAG, "displayStudentListUH: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/classU/sectionH");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentListUG() {
            Log.d(TAG, "displayStudentListUG: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/classU/sectionG");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentListUF() {
            Log.d(TAG, "displayStudentListUF: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/classU/sectionF");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentListUE() {
            Log.d(TAG, "displayStudentListUE: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/classU/sectionE");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentListUD() {
            Log.d(TAG, "displayStudentListUD: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/classU/sectionD");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentListUC() {
            Log.d(TAG, "displayStudentListUC: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/classU/sectionC");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentListUB() {
            Log.d(TAG, "displayStudentListUB: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/classU/sectionB");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentListUA() {
            Log.d(TAG, "displayStudentListUA: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/classU/sectionA");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList1H() {
            Log.d(TAG, "displayStudentList1H: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class1/sectionH");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList1G() {
            Log.d(TAG, "displayStudentList1G: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class1/sectionG");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList1F() {
            Log.d(TAG, "displayStudentList1F: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class1/sectionF");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList1E() {
            Log.d(TAG, "displayStudentList1E: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class1/sectionE");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList1D() {
            Log.d(TAG, "displayStudentList1D: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class1/sectionD");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList1C() {
            Log.d(TAG, "displayStudentList1C: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class1/sectionC");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList1B() {
            Log.d(TAG, "displayStudentList1B: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class1/sectionB");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList1A() {
            Log.d(TAG, "displayStudentList1A: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class1/sectionA");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList2H() {
            Log.d(TAG, "displayStudentList2H: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class2/sectionH");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList2G() {
            Log.d(TAG, "displayStudentList2G: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class2/sectionG");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList2F() {
            Log.d(TAG, "displayStudentList2F: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class2/sectionF");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList2E() {
            Log.d(TAG, "displayStudentList2E: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class2/sectionE");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList2D() {
            Log.d(TAG, "displayStudentList2D: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class2/sectionD");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList2C() {
            Log.d(TAG, "displayStudentList2C: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class2/sectionC");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList2B() {
            Log.d(TAG, "displayStudentList2B: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class2/sectionB");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList2A() {
            Log.d(TAG, "displayStudentList2A: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class2/sectionA");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList3H() {
            Log.d(TAG, "displayStudentList3H: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class3/sectionH");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList3G() {
            Log.d(TAG, "displayStudentList3G: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class3/sectionG");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList3F() {
            Log.d(TAG, "displayStudentList3F: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class3/sectionF");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList3E() {
            Log.d(TAG, "displayStudentList3E: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class3/sectionE");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList3D() {
            Log.d(TAG, "displayStudentList3D: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class3/sectionD");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList3C() {
            Log.d(TAG, "displayStudentList3C: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class3/sectionC");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList3B() {
            Log.d(TAG, "displayStudentList3B: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class3/sectionB");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList3A() {
            Log.d(TAG, "displayStudentList3A: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class3/sectionA");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList4H() {
            Log.d(TAG, "displayStudentList4H: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class4/sectionH");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList4G() {
            Log.d(TAG, "displayStudentList4G: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class4/sectionG");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList4F() {
            Log.d(TAG, "displayStudentList4F: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class4/sectionF");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList4E() {
            Log.d(TAG, "displayStudentList4E: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class4/sectionE");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList4D() {
            Log.d(TAG, "displayStudentList4D: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class4/sectionD");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList4C() {
            Log.d(TAG, "displayStudentList4C: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class4/sectionC");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList4B() {
            Log.d(TAG, "displayStudentList4B: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class4/sectionB");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList4A() {
            Log.d(TAG, "displayStudentList4A: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class4/sectionA");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList5H() {
            Log.d(TAG, "displayStudentList5H: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class5/sectionH");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList5G() {
            Log.d(TAG, "displayStudentList5G: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class5/sectionG");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList5F() {
            Log.d(TAG, "displayStudentList5F: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class5/sectionF");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList5E() {
            Log.d(TAG, "displayStudentList5E: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class5/sectionE");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList5D() {
            Log.d(TAG, "displayStudentList5D: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class5/sectionD");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList5C() {
            Log.d(TAG, "displayStudentList5C: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class5/sectionC");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList5B() {
            Log.d(TAG, "displayStudentList5B: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class5/sectionB");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList5A() {
            Log.d(TAG, "displayStudentList5A: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class5/sectionA");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList6H() {
            Log.d(TAG, "displayStudentList6H: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class6/sectionH");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList6G() {
            Log.d(TAG, "displayStudentList6G: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class6/sectionG");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList6F() {
            Log.d(TAG, "displayStudentList6F: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class6/sectionF");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList6E() {
            Log.d(TAG, "displayStudentList6E: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class6/sectionE");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList6D() {
            Log.d(TAG, "displayStudentList6D: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class6/sectionD");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList6C() {
            Log.d(TAG, "displayStudentList6C: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class6/sectionC");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList6B() {
            Log.d(TAG, "displayStudentList6B: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class6/sectionB");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList6A() {
            Log.d(TAG, "displayStudentList6A: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class6/sectionA");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

        private void displayStudentList7H() {
            Log.d(TAG, "displayStudentList7H: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class7/sectionH");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

        private void displayStudentList7G() {
            Log.d(TAG, "displayStudentList7G: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class7/sectionG");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

        private void displayStudentList7F() {
            Log.d(TAG, "displayStudentList7F: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class7/sectionF");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

        private void displayStudentList7E() {
            Log.d(TAG, "displayStudentList7E: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class7/sectionE");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

        private void displayStudentList7D() {
            Log.d(TAG, "displayStudentList7D: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class7/sectionD");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

        private void displayStudentList7C() {
            Log.d(TAG, "displayStudentList7C: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class7/sectionC");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

        private void displayStudentList7B() {
            Log.d(TAG, "displayStudentList7B: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class7/sectionB");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

        private void displayStudentList7A() {
            Log.d(TAG, "displayStudentList7A: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class7/sectionA");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList8H() {
            Log.d(TAG, "displayStudentList8H: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class8/sectionH");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList8G() {
            Log.d(TAG, "displayStudentList8G: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class8/sectionG");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList8F() {
            Log.d(TAG, "displayStudentList8F: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class8/sectionF");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList8E() {
            Log.d(TAG, "displayStudentList8E: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class8/sectionE");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList8D() {
            Log.d(TAG, "displayStudentList8D: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class8/sectionD");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList8C() {
            Log.d(TAG, "displayStudentList8C: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class8/sectionC");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList8B() {
            Log.d(TAG, "displayStudentList8B: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class8/sectionB");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList8A() {
            Log.d(TAG, "displayStudentList8A: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class8/sectionA");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList9H() {
            Log.d(TAG, "displayStudentList9H: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class9/sectionH");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList9G() {
            Log.d(TAG, "displayStudentList9G: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class9/sectionG");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList9F() {
            Log.d(TAG, "displayStudentList9F: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class9/sectionF");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList9E() {
            Log.d(TAG, "displayStudentList9E: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class9/sectionE");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList9D() {
            Log.d(TAG, "displayStudentList9D: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class9/sectionD");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList9C() {
            Log.d(TAG, "displayStudentList9C: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class9/sectionC");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        private void displayStudentList9B() {
            Log.d(TAG, "displayStudentList9B: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class9/sectionB");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList9A() {
            Log.d(TAG, "displayStudentList9A: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class9/sectionA");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList10H() {
            Log.d(TAG, "displayStudentList10H: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class10/sectionH");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList10G() {
            Log.d(TAG, "displayStudentList10G: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class10/sectionG");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList10F() {
            Log.d(TAG, "displayStudentList10F: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class10/sectionF");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList10E() {
            Log.d(TAG, "displayStudentList10E: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class10/sectionE");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList10D() {
            Log.d(TAG, "displayStudentList10D: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class10/sectionD");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList10C() {
            Log.d(TAG, "displayStudentList10C: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class10/sectionC");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList10B() {
            Log.d(TAG, "displayStudentList10D: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class10/sectionB");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList10A() {
            Log.d(TAG, "displayStudentList10A: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class10/sectionA");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList11H() {
            Log.d(TAG, "displayStudentList11H: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class11/sectionH");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList11G() {
            Log.d(TAG, "displayStudentList11G: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class11/sectionG");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList11F() {
            Log.d(TAG, "displayStudentList11F: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class11/sectionF");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList11D() {
            Log.d(TAG, "displayStudentList11D: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class11/sectionD");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList11E() {
            Log.d(TAG, "displayStudentList11E: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class11/sectionD");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList11C() {
            Log.d(TAG, "displayStudentList11C: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class11/sectionC");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList11B() {
            Log.d(TAG, "displayStudentList11B: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class11/sectionB");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList11A() {
            Log.d(TAG, "displayStudentList11A: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class11/sectionA");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

        private void displayStudentList12H() {
            Log.d(TAG, "displayStudentList12H: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class12/sectionH");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList12G() {
            Log.d(TAG, "displayStudentList12G: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class12/sectionG");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList12F() {
            Log.d(TAG, "displayStudentList12F: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class12/sectionF");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList12E() {
            Log.d(TAG, "displayStudentList12E: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class12/sectionE");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList12D() {
            Log.d(TAG, "displayStudentList12D: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class12/sectionD");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void displayStudentList12C() {
            Log.d(TAG, "displayStudentList12C: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class12/sectionC");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



        }

        private void displayStudentList12B() {
            Log.d(TAG, "displayStudentList12B: starting");
            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class12/sectionB");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

        private void displayStudentList12A() {
            Log.d(TAG, "displayStudentList12A: starting");

            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class12/sectionA");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){
                        Student student = studentSnapshot.getValue(Student.class);
                        studentList.add(student);
                    }

                    StudentList adapter = new StudentList(getActivity(),studentList);
                    studentListView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }



    }


}
