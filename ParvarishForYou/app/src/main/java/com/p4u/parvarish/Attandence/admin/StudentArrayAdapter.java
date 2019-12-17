package com.p4u.parvarish.Attandence.admin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.p4u.parvarish.R;

import java.util.List;

class StudentArrayAdapter extends ArrayAdapter<StudentData>
{

    private LayoutInflater inflater;

    public StudentArrayAdapter(Context context, List<StudentData> studentList)
    {
        super(context, R.layout.simplerow, R.id.rowTextView, studentList);
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

        // The child views in each row.
        CheckBox checkBox;
        final TextView textView1,textView2,textView3;

        // Create a new row view
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.simplerow, null);

            // Find the child views.
            textView1 = convertView.findViewById(R.id.rowTextView);
            textView2 = convertView.findViewById(R.id.admTextView);
            textView3= convertView.findViewById(R.id.tv_rollnumber);
            checkBox = convertView.findViewById(R.id.CheckBox01);

            // Optimization: Tag the row with it's child views, so we don't
            // have to
            // call findViewById() later when we reuse the row.
            convertView.setTag(new StudentViewHolder(textView1,textView2, checkBox));

            // If CheckBox is toggled, update the planet it is tagged with.
            checkBox.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    CheckBox cb = (CheckBox) v;
                    StudentData planet = (StudentData) cb.getTag();
                    planet.setChecked(cb.isChecked());

                }
            });
            textView3.setText(String.valueOf(position+1));
        }
        // Reuse existing row view
        else
        {
            // Because we use a ViewHolder, we avoid having to call
            // findViewById().
            StudentViewHolder viewHolder = (StudentViewHolder) convertView
                    .getTag();
            checkBox = viewHolder.getCheckBox();
            textView1 = viewHolder.getTextView1();
            textView2 = viewHolder.getTextView2();
        }

        // Tag the CheckBox with the Planet it is displaying, so that we can
        // access the planet in onClick() when the CheckBox is toggled.
        checkBox.setTag(student);

        // Display planet data
        assert student != null;
        checkBox.setChecked(student.isChecked());
        textView1.setText(student.getStudentName());
        textView2.setText(student.getStudentadmissionno());

        return convertView;
    }

}