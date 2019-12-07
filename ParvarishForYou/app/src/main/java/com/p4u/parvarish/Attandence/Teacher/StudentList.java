package com.p4u.parvarish.Attandence.Teacher;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.p4u.parvarish.R;

import java.util.List;


/**
 * Created by eatarsi on 7/9/2017.
 */

public class StudentList extends ArrayAdapter<Student> {

    private Activity context;
    private List<Student> studentList;

    public StudentList(Activity context, List<Student> studentList){
        super(context, R.layout.studentlist_row, studentList);
        this.context = context;
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listviewItem = inflater.inflate(R.layout.studentlist_row, null, true);

        TextView txtstudentName = (TextView) listviewItem.findViewById(R.id.tv_studentnameforlist);
        TextView txtRollnumber = (TextView) listviewItem.findViewById(R.id.tv_rollnumber);

        //set position of row in roll number textview
        txtRollnumber.setText(String.valueOf(position+1));
        Student student = studentList.get(position);
        txtstudentName.setText(student.getStudentname());

        return listviewItem;
    }
}
