package com.p4u.parvarish.Attandence.Teacher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.p4u.parvarish.R;

import java.util.ArrayList;


/**
 * Created by eatarsi on 7/1/2017.
 */

public class CustomAdapter extends ArrayAdapter<StudentdataModel> implements View.OnClickListener {

    private ArrayList<StudentdataModel> studentList;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        CheckBox checkBox;
    }

    public CustomAdapter(ArrayList<StudentdataModel> data, Context context){
        super(context, R.layout.studentlist_row, data);
        this.studentList = data;
        this.mContext=context;
    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        StudentdataModel studentdataModel=(StudentdataModel)object;

        switch (v.getId())
        {
            case R.id.checkbox_attendance:
                Snackbar.make(v, "Student Name: " +studentdataModel.getName(), Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
                break;
        }

    }
    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        StudentdataModel studentdataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.studentlist_row, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.tv_studentnameforlist);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox_attendance);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

       /* Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position; */

        viewHolder.txtName.setText(studentdataModel.getName());
        viewHolder.checkBox.setOnClickListener(this);
        viewHolder.checkBox.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}