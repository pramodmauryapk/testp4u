package com.p4u.parvarish.Attandence.admin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.p4u.parvarish.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class StudentList_model extends ArrayAdapter<StudentData> {
    private static final String TAG = "UserList_model";
    private Activity context;
    private List<StudentData> students;
    private View listViewItem;
    private StudentData student;
    private StudentList_model.OnItemClickListener mListener;
    private CheckBox cb;
    private TextView textViewName,textViewRole;
    private DatabaseReference dR;
    private MarkAttendanceFragment mk;
    private boolean[] itemChecked;
   StudentList_model(Activity context, List<StudentData> students) {
        super(context, R.layout.layout_student_attandence, students);
        this.context = context;
        this.students = students;
        itemChecked = new boolean[students.size()];
    }

    @SuppressLint("ViewHolder")
    @Override
    @NonNull
    public View  getView(final int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        student = students.get(position);
        listViewItem = inflater.inflate(R.layout.layout_student_attandence, null, true);


      try {
          textViewName = listViewItem.findViewById(R.id.tv_username);
          textViewRole = listViewItem.findViewById(R.id.tv_userrole);
          cb=listViewItem.findViewById(R.id.checkbox1);
          textViewName.setText(student.getStudentName());
          textViewRole.setText(student.getStudentadmissionno());
          //cb.setOnCheckedChangeListener(this);
          //dR= FirebaseDatabase.getInstance().getReference().child("SCHOOL").child("ATTANDENCE").child(get_current_time()).child(String.valueOf(position));
          cb.setChecked(false);

          if (itemChecked[position])
              cb.setChecked(true);
          else
              cb.setChecked(false);

          cb.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  // TODO Auto-generated method stub
                  if (cb.isChecked()) {
                      itemChecked[position] = true;

                  }
                  else
                      itemChecked[position] = false;
              }
          });
      }catch (Exception e){
          Log.d(TAG,"Cannot fetch data");
      }




        return listViewItem;

    }
/*
//Toast.makeText(getContext(),"hello",Toast.LENGTH_LONG).show();
        CheckBox cb = (CheckBox) v.findViewById(R.id.checkbox1);
        TextView tv = (TextView) v.findViewById(R.id.textView1);
        cb.performClick();
        if (cb.isChecked()) {
            Toast.makeText(getContext(),"" + checkedValue,Toast.LENGTH_LONG).show();
            //checkedValue.add(tv.getText().toString());
        } else if (!cb.isChecked()) {
            checkedValue.remove(tv.getText().toString());
        }




 */
 /*   @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
       // Toast.makeText(getContext(),"hello "+mk.date,Toast.LENGTH_LONG).show();
        dR= FirebaseDatabase.getInstance().getReference().child("SCHOOL").child("ATTANDENCE").child(get_current_time()).child(this.textViewRole.getText().toString());
        if (b) {
            //selectedItems.add(textViewRole.getText().toString());
          dR.setValue("P");

        } else {
            //selectedItems.remove(textViewRole.getText().toString());
            dR.removeValue();

        }
        /*final CheckBox cb = (CheckBox) view.findViewById(R.id.checkbox1);
        final TextView tv = (TextView) view.findViewById(R.id.tv_userrole);
        cb.performClick();
        cb.setTag(i);

        if (cb.isChecked()) {

            //selectedItems.add(tv.getText().toString());
            //Toast.makeText(getContext(),"hello "+selectedItems.size(),Toast.LENGTH_LONG).show();
            cb.setChecked(false);
        } else if (!cb.isChecked()) {
            //selectedItems.remove(tv.getText().toString());
            //Toast.makeText(getContext(),"hello "+selectedItems.size(),Toast.LENGTH_LONG).show();
            cb.setChecked(true);
        }
    }
*/

    public interface OnItemClickListener {
        void onItemClick(int position);

    }
    private String get_current_time(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        return sdf.format(new Date());
    }
}