package com.p4u.parvarish.Attandence.Teacher;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.p4u.parvarish.Attandence.UserInformation;
import com.p4u.parvarish.R;

import java.util.ArrayList;
import java.util.List;

//import com.github.mikephil.charting.charts.PieChart;
//import com.github.mikephil.charting.components.Legend;
//import com.github.mikephil.charting.data.PieData;
//import com.github.mikephil.charting.data.PieDataSet;
//import com.github.mikephil.charting.data.PieEntry;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewattendanceTeacherFragment extends Fragment {

    private static String TAG = "ViewattendanceFragment";
    private Spinner spinnerClass,spinnerSection,spinnerSubject,spinnerYear,spinnerMonth,spinnerDay,spinnerStudent;
    //private PieChart pieChart;

    // Firebase variable declartion
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private FirebaseUser user = mAuth.getCurrentUser();
    String userID = user.getUid();

    // String lists
    List<String> attendanceList = new ArrayList<String>();
    List<String> classnames = new ArrayList<String>();
    List<String> sectionnames = new ArrayList<String>();
    List<String> subjectnames = new ArrayList<String>();
    List<String> studentList = new ArrayList<String>();

    // Pie chart x and y data string array
    private int [] yData = {25,15,10};
    private String[] xData = {"Total","Present","Absent"};


    public ViewattendanceTeacherFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_viewattendance_teacher, container, false);
        Log.d(TAG,"onCreate: starting to create view");


        // Initialising spinner views
        spinnerClass = (Spinner) view.findViewById(R.id.spn_classview);
        spinnerSection = (Spinner) view.findViewById(R.id.spn_sectionview);
        spinnerSubject = (Spinner) view.findViewById(R.id.spn_subjectview);
        spinnerYear = (Spinner) view.findViewById(R.id.spn_year);
        spinnerMonth = (Spinner) view.findViewById(R.id.spn_month);
        spinnerDay = (Spinner) view.findViewById(R.id.spn_day);
        spinnerStudent = (Spinner) view.findViewById(R.id.spn_student);
        // initialize piechart
       // pieChart = (PieChart) view.findViewById(R.id.piechart_attendace);

        //Access classname,sectionname and subject of teahcer from database
        myRef = mFirebaseDatabase.getReference();
    /*    myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "Accessed spinner data like class, section and subject from user database");
                accessspinnerData(dataSnapshot);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //If statement to display particular class and section student list
        if(spinnerClass.getSelectedItem().equals("12th") && spinnerSection.getSelectedItem().equals("A")){
            // Toast.makeText(getActivity(),"Hello",Toast.LENGTH_SHORT).show();
            // displayStudentList12A();
            Log.d(TAG, "displayStudentList12A: starting");

            // Query query = myRef.child("studentdata").orderByChild("classname").equalTo(12);
            myRef = mFirebaseDatabase.getReference("users/studentdata/class12/sectionA");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    studentList.clear();
                    for(DataSnapshot studentSnapshot : dataSnapshot.getChildren()){

                        UserInformation uInfo = new UserInformation();
                        // Set and get class names of current teacher
                        uInfo.setStudentname(studentSnapshot.getValue(UserInformation.class).getStudentname());
                        studentList.add(uInfo.getStudentname());
                    }

                    // Creating adapter for class spinner
                    ArrayAdapter<String> studentlistAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, studentList);

                    // Drop down layout style - list view with radio button for class spinner
                    studentlistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    // attaching data adapter to class spinner
                 //   spinnerStudent.setAdapter(studentlistAdapter);

                    Toast.makeText(getActivity(),"Hello student" +studentList,Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }



        // On item click event on class, section,subject,year,month and day spinners
        spinnerClass.setOnItemSelectedListener(new MyOnItemSelectedListener());
        spinnerSubject.setOnItemSelectedListener(new MyOnItemSelectedListener());
        spinnerSection.setOnItemSelectedListener(new MyOnItemSelectedListener());
        spinnerYear.setOnItemSelectedListener(new MyOnItemSelectedListener());
        spinnerMonth.setOnItemSelectedListener(new MyOnItemSelectedListener());
        spinnerDay.setOnItemSelectedListener(new MyOnItemSelectedListener());
        spinnerStudent.setOnItemSelectedListener(new MyOnItemSelectedListener()); */

        //Fragment return view
        return view;
    }

    private void accessspinnerData(DataSnapshot dataSnapshot) {
        Log.d(TAG,"accessspinnerData:");
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
            uInfo.setSubject3(ds.child(userID).getValue(UserInformation.class).getSubject3());
            String subject3 = uInfo.getSubject3();
            uInfo.setSubject4(ds.child(userID).getValue(UserInformation.class).getSubject4());
            String subject4 = uInfo.getSubject4();
            uInfo.setSubject5(ds.child(userID).getValue(UserInformation.class).getSubject5());
            String subject5 = uInfo.getSubject5();
            uInfo.setSubject6(ds.child(userID).getValue(UserInformation.class).getSubject6());
            String subject6 = uInfo.getSubject6();
            uInfo.setSubject7(ds.child(userID).getValue(UserInformation.class).getSubject7());
            String subject7 = uInfo.getSubject7();
            uInfo.setSubject8(ds.child(userID).getValue(UserInformation.class).getSubject8());
            String subject8 = uInfo.getSubject8();
            uInfo.setSubject9(ds.child(userID).getValue(UserInformation.class).getSubject9());
            String subject9 = uInfo.getSubject9();
            uInfo.setSubject10(ds.child(userID).getValue(UserInformation.class).getSubject10());
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



    private class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, final View view, int i, long l) {

            String selectedclass = spinnerClass.getSelectedItem().toString();
            String selectedsection = spinnerSection.getSelectedItem().toString();
            String selectedsubject = spinnerSubject.getSelectedItem().toString();
            String selectedyear = spinnerYear.getSelectedItem().toString();
            String selectedmonth = spinnerMonth.getSelectedItem().toString();
            String selectedday = spinnerDay.getSelectedItem().toString();
         //   String selectedStudent = spinnerStudent.getSelectedItem().toString();

            myRef = mFirebaseDatabase.getReference("users/attendance/" + selectedyear
                    + "/" + selectedmonth + "/" + selectedday + "/" + selectedclass
                    + "/" + selectedsection + "/" + selectedsubject);
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    attendanceList.clear();
                    for(DataSnapshot attendaceSnapshot : dataSnapshot.getChildren()){
                        attendanceList.add(attendaceSnapshot.getValue().toString());
                    }
                 //   Toast.makeText(getActivity(),"Present" +attendanceList,Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

          //  showPieChart();


            // Set pie chart functions
         /*   pieChart.setDescription("Student attendance");
            pieChart.setRotationEnabled(true);
            pieChart.setHoleRadius(25f);
            pieChart.setTransparentCircleAlpha(0);
            pieChart.setCenterText("Attendance");
            pieChart.setCenterTextSize(10);


            List<PieEntry> entries = new ArrayList<>();

            entries.add(new PieEntry(18.5f, "Total"));
            entries.add(new PieEntry(26.7f, "Present"));
            entries.add(new PieEntry(24.0f, "Absent"));

            PieDataSet set = new PieDataSet(entries, "Student attendance");
            set.setSliceSpace(2);
            set.setValueTextSize(12);

            // add colors to data set
            ArrayList<Integer> colors = new ArrayList<>();
            colors.add(Color.YELLOW);
            colors.add(Color.GREEN);
            colors.add(Color.RED);

            set.setColors(colors);
            PieData data = new PieData(set);
            pieChart.setData(data);
            pieChart.invalidate(); // refresh*/

            // add data to pie chart
         //   addDataSet();

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }



    private void showPieChart() {

    }
/*
    private void addDataSet() {
        Log.d(TAG,"addDataSet: started");
        ArrayList<PieEntry> yEntries = new ArrayList<>();
        ArrayList<String> xEntries = new ArrayList<>();

        for(int i = 0; i < yData.length; i++){
            yEntries.add(new PieEntry(yData[i] , i));
        }

        for(int i = 1; i < xData.length; i++){
            xEntries.add(xData[i]);
        }

        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntries, "Student attendance");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        // add colors to data set
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.YELLOW);
        colors.add(Color.GREEN);
        colors.add(Color.RED);

        pieDataSet.setColors(colors);


        // Add legends to chart
        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        //Create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }
*/
}
