package com.p4u.parvarish.Attandence.student;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.p4u.parvarish.Attandence.admin.StudentData;
import com.p4u.parvarish.R;

import java.util.ArrayList;
import java.util.List;

public class StudentAttandenceArrayAdapter extends ArrayAdapter<StudentData>
{

    private LayoutInflater inflater;
    private String TAG="Adopter";
    private DatabaseReference dR,studentref;
    private String admissionno;
   private TextView textView1,textView2,textView3,status;

    private View v;

    public StudentAttandenceArrayAdapter(Context context, List<StudentData> studentList, ArrayList<String> value)
    {
        super(context, R.layout.showattandencerow, R.id.rowTextView, studentList);
        status.setText(value.toString());
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

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.showattandencerow, null);

            // Find the child views.
            textView1 = convertView.findViewById(R.id.rowTextView);
            textView2 = convertView.findViewById(R.id.admTextView);
            textView3 = convertView.findViewById(R.id.tv_rollnumber);
            status = convertView.findViewById(R.id.Status);
            convertView.setTag(new AttandenceViewHolder(textView1, textView2, status));
            textView3.setText(String.valueOf(position + 1));



        }
        // Reuse existing row view
        else
        {
            AttandenceViewHolder viewHolder = (AttandenceViewHolder) convertView.getTag();
            status = viewHolder.getTextView3();
            textView1 = viewHolder.getTextView1();
            textView2 = viewHolder.getTextView2();
        }


        assert student != null;
        textView1.setText(student.getStudentName());
        textView2.setText(student.getStudentId());

       // status.setText(admissionno);
        return convertView;
    }


    public void add(String value) {
        //status.setText(value);
    }
}
