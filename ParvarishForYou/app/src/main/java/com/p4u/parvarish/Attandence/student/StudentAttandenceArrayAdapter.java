package com.p4u.parvarish.Attandence.student;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.p4u.parvarish.Attandence.admin.StudentData;
import com.p4u.parvarish.R;

import java.util.ArrayList;
import java.util.List;

public class StudentAttandenceArrayAdapter extends ArrayAdapter<StudentData>
{

    private LayoutInflater inflater;
    private String TAG="Adopter";

    public   ArrayList<String> val;
    private TextView Name,AdmissionNo,No,Status;
    private int i=0;
    private View v;

    public StudentAttandenceArrayAdapter(Context context, List<StudentData> studentList)
    {
        super(context, R.layout.showattandencerow, R.id.rowTextView, studentList);


        // Cache the LayoutInflate to avoid asking for a new one each time.
        inflater = LayoutInflater.from(context);
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent)
    {
        // Planet to display
        StudentData student = this.getItem(position);

        //val.add("A");

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.showattandencerow, null);
            // Find the child views.
            Name = convertView.findViewById(R.id.stuname);
            AdmissionNo= convertView.findViewById(R.id.stuadmno);
            Status = convertView.findViewById(R.id.stustatus);
            No = convertView.findViewById(R.id.tv_rollnumber);

            convertView.setTag(new AttandenceViewHolder(Name,AdmissionNo,Status));
            No.setText(String.valueOf(position + 1));



        }
        // Reuse existing row view
        else
        {
            AttandenceViewHolder viewHolder = (AttandenceViewHolder) convertView.getTag();
            Status = viewHolder.getStatus();
            Name = viewHolder.getName();
            AdmissionNo = viewHolder.getAdmission();
        }
        assert student != null;
        Name.setText(student.getStudentName());
        AdmissionNo.setText(student.getStudentId());

        return convertView;
    }

}
