package com.p4u.parvarish.Attandence;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.p4u.parvarish.Attandence.Teacher.AddleavedataFragment;
import com.p4u.parvarish.Attandence.Teacher.TeacherListFragment;
import com.p4u.parvarish.Attandence.admin.AddStudentFragment;
import com.p4u.parvarish.Attandence.admin.AddTeacherFragment;
import com.p4u.parvarish.Attandence.admin.MarkAttandenceFragment;
import com.p4u.parvarish.Attandence.admin.StudentlListFragment;
import com.p4u.parvarish.Attandence.admin.arkAttandenceFragment;
import com.p4u.parvarish.Attandence.student.ViewAttendanceFragment;
import com.p4u.parvarish.Attandence.student.ViewleaveFragment;
import com.p4u.parvarish.HomeFragment;
import com.p4u.parvarish.R;
import com.p4u.parvarish.menu_items.LayoutGridView;

import static java.util.Objects.requireNonNull;

public class AttandenceMenuFragment extends HomeFragment {

    private static final String TAG = "AttandenceMenuFragment";
    private String schoolname,role;
    private Bundle bundle;
    private Context context;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_menu, container, false);
        initViews();
        context = container.getContext();
        bundle=new Bundle();
        assert this.getArguments() != null;
        schoolname = this.getArguments().getString("SCHOOL_NAME");
        //schoolname="PARVARISH FOR U";
        role = this.getArguments().getString("ROLE");
        role="PRINCI";
        getWidthAndHeight();
        final GridView androidGridView = v.findViewById(R.id.grid_view_image_text);
        String[] gridViewString = new String[0];
        int[] gridViewImageId = new int[0];
        assert schoolname != null;
        switch (role) {
            case "STUDENT":
            case "PARENTS":
                gridViewString = new String[]{
                        "Attandence",
                        "Student Data",
                        "Teacher Data",

                };
                gridViewImageId = new int[]{
                        R.drawable.ic_youtube_searched_for_black_24dp,
                        R.drawable.ic_child_friendly_black_24dp,
                        R.drawable.ic_transfer_within_a_station_black_24dp,
                };

                break;
            case "TEACHER":
                gridViewString = new String[]{
                        "Attandence",
                        "Student Data",
                        "Teacher Data",
                        "Mark Attandence",
                        "Teacher List",
                        "Student List",
                        "Add Student",


                };
                gridViewImageId = new int[]{
                        R.drawable.ic_youtube_searched_for_black_24dp,
                        R.drawable.ic_child_friendly_black_24dp,
                        R.drawable.ic_transfer_within_a_station_black_24dp,
                        R.drawable.ic_library_books_black_24dp,
                        R.drawable.ic_image_black_24dp,
                        R.drawable.ic_burst_mode_black_24dp,
                        R.drawable.ic_add_book_24dp

                };
                break;
            case "PRINCI":
                gridViewString = new String[]{
                        "Attandence",
                        "Student Data",
                        "Teacher Data",
                        "Mark Attandence",
                        "Teacher List",
                        "Student List",
                        "Add Student",
                        "Add Teacher",

                        //   "WorkBook",
                        //   "Leave",
                        //   "Notice Board",
                        //   "Assignment",
                        //   "Reports",
                        //    "Modify Attandence",
                        //    "Teacher Attandence",

                };

                gridViewImageId = new int[]{
                        R.drawable.ic_youtube_searched_for_black_24dp,
                        R.drawable.ic_child_friendly_black_24dp,
                        R.drawable.ic_transfer_within_a_station_black_24dp,
                        R.drawable.ic_library_books_black_24dp,
                        R.drawable.ic_image_black_24dp,
                        R.drawable.ic_burst_mode_black_24dp,
                        R.drawable.ic_add_book_24dp,
                        R.drawable.ic_report_black_24dp,

                        //   R.drawable.ic_list_black_24dp,
                        //   R.drawable.ic_card_giftcard_black_24dp,
                        //   R.drawable.ic_book_black_24dp,
                        //   R.drawable.ic_add_book_24dp,
                        // R.drawable.ic_phone_in_talk_black_24dp,
                        // R.drawable.ic_verified_user_black_24dp,
                        // R.drawable.ic_update_black_24dp,

                };
                break;
            case "ADMIN":
                gridViewString = new String[]{
                        "Attandence",
                        "Student Data",
                        "Teacher Data",
                        "Mark Attandence",
                        "Teacher List",
                        "Student List",
                        "Add Student",
                        "Add Teacher",
                        "Add Leave",
                        "View Leave"

                        //   "WorkBook",
                        //   "Leave",
                        //   "Notice Board",
                        //   "Assignment",
                        //   "Reports",
                        //    "Modify Attandence",
                        //    "Teacher Attandence",


                };

                gridViewImageId = new int[]{

                        R.drawable.ic_youtube_searched_for_black_24dp,
                        R.drawable.ic_child_friendly_black_24dp,
                        R.drawable.ic_transfer_within_a_station_black_24dp,
                        R.drawable.ic_library_books_black_24dp,
                        R.drawable.ic_image_black_24dp,
                        R.drawable.ic_burst_mode_black_24dp,
                        R.drawable.ic_add_book_24dp,
                        R.drawable.ic_report_black_24dp,
                        R.drawable.ic_report_black_24dp,
                        R.drawable.ic_report_black_24dp,

                        //   R.drawable.ic_list_black_24dp,
                        //   R.drawable.ic_card_giftcard_black_24dp,
                        //   R.drawable.ic_book_black_24dp,
                        //   R.drawable.ic_add_book_24dp,
                        // R.drawable.ic_phone_in_talk_black_24dp,
                        // R.drawable.ic_verified_user_black_24dp,
                        // R.drawable.ic_update_black_24dp,



                };

                break;
        }
        LayoutGridView adapterViewAndroid = new LayoutGridView(context, gridViewString, gridViewImageId);
        androidGridView.setAdapter(adapterViewAndroid);
        androidGridView.setColumnWidth(getWidthAndHeight()/5);
        androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int i, long id) {
                switch (i) {

                    case 0:
                        switchFragment(new ViewAttendanceFragment());
                        //switchFragment(new StudentassignmentFragment());
                        break;
                    case 1:
                        switchFragment(new arkAttandenceFragment());
                        break;
                    case 2:
                        //switchFragment(new ViewleaveFragment());
                        break;
                    case 3:switchFragment(new MarkAttandenceFragment());
                        //switchFragment(new ViewnoticeFragment());
                        break;
                    case 4:switchFragment(new TeacherListFragment());
                        //switchFragment(new AssignmentbyteacherFragment());
                        break;
                    case 5:switchFragment(new StudentlListFragment());
                        //switchFragment(new EnterstudentdataFragment());
                        break;
                    case 6:switchFragment(new AddStudentFragment());
                        //switchFragment(new AddleavedataFragment());
                        break;
                    case 7:switchFragment(new AddTeacherFragment());
                        //switchFragment(new GeneratereportFragment());
                        break;
                    case 8:switchFragment(new AddleavedataFragment());
                       // switchFragment(new ModifyattendanceFragment());
                        break;
                    case 9:switchFragment(new ViewleaveFragment());
                        //switchFragment(new ModifyattendanceTeacherFragment());
                        break;
                    case 10:
                        break;
                    case 11:
                        //switchFragment(new StudentleaverecordFragment());
                        break;
                    case 12:
                        //switchFragment(new ViewAttendanceavgFragment());
                        break;
                    case 13:
                        //switchFragment(new ViewattendanceTeacherFragment());
                        break;
                    case 14:
                        break;




                }

            }
        });





        return v;
    }
    private void initViews(){

    }

   private int getWidthAndHeight() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenHeight = displaymetrics.heightPixels;
        return displaymetrics.widthPixels;
    }
    // switching fragment
    private void switchFragment(Fragment fragment) {
        bundle.putString("SCHOOL_NAME",schoolname);
        bundle.putString("ROLE",role);
        fragment.setArguments(bundle);
        if(getActivity()!=null) {
            requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_frame, fragment)
                    .addToBackStack(null).commit();
        }

    }







}
