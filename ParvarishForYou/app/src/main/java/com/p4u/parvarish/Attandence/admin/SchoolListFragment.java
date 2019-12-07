package com.p4u.parvarish.Attandence.admin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.p4u.parvarish.R;
import com.p4u.parvarish.book_pannel.spreadLayout;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class SchoolListFragment extends Fragment {

    private static final String TAG = "ListschookFragment";
    private List<SchoolData> dataList = new ArrayList<>();
    private int redColor,greenColor;
    private RecyclerView.Adapter adapter;

    private DatabaseReference princi;
    private RecyclerView recyclerView;
    private View v;
    private Context context;
    private LinearLayout l1;
    private String schoolname;
    //private final View.OnClickListener mOnClickListener = new MyOnClickListener();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_schoollist, container, false);
        princi = FirebaseDatabase.getInstance().getReference().child("SCHOOL").child("STUDENTS");
        context = container.getContext();
        schoolname = requireNonNull(this.getArguments()).getString("SCHOOL_NAME");
        initViews();
        l1.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new spreadLayout());
        adapter = getAdapter();
        recyclerView.setAdapter(adapter);

        redColor = getResources().getColor(R.color.red);
        greenColor = getResources().getColor(R.color.green);

        appendDataList();
        adapter.notifyDataSetChanged();

        return v;
    }
    private void initViews(){
        recyclerView =v. findViewById(R.id.main_recycler_view);
        l1=v.findViewById(R.id.l1);
        SwipeRefreshLayout refreshLayout = v.findViewById(R.id.refresh_layout);
    }


    private void appendDataList() {

        princi.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                dataList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    SchoolData school = ds.getValue(SchoolData.class);
                    dataList.add(school);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @NonNull
    private RecyclerView.Adapter getAdapter() {
        final LayoutInflater inflater = LayoutInflater.from(context);
        return new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
                View view = inflater.inflate(R.layout.layout_school_item, parent, false);
                //view.setOnClickListener(mOnClickListener);
             /*   view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context,"hi"+viewType,Toast.LENGTH_LONG).show();
                    }
                });*/
                return new MyViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                SchoolListFragment.MyViewHolder myHolder = (SchoolListFragment.MyViewHolder) holder;
                myHolder.bindData(dataList.get(position));


            }

            @Override
            public int getItemCount() {
                return dataList.size();
            }

        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView schoolTitle;
        TextView studentCount;
        TextView teacherCount;
        TextView schoolmedium;
        TextView schoolAddress;
        TextView schoolstbYear;
        TextView schoolprinciname;
        TextView schoolprincimobile;

        MyViewHolder(View itemView) {
            super(itemView);
            this.schoolTitle = itemView.findViewById(R.id.item_name_tv);
            this.studentCount = itemView.findViewById(R.id.item_current_price);
            this.teacherCount = itemView.findViewById(R.id.item_trend_flag);
            this.schoolmedium = itemView.findViewById(R.id.item_gross);
            this.schoolAddress = itemView.findViewById(R.id.txtsubject);
            this.schoolstbYear=itemView.findViewById(R.id.item_year);
            this.schoolprinciname=itemView.findViewById(R.id.item_princi_name);
            this.schoolprincimobile=itemView.findViewById(R.id.item_princi_mobile);

        }

        @SuppressLint("SetTextI18n")
        void bindData(final SchoolData schoolData) {
            schoolTitle.setText(schoolData.getSchoolName());
            schoolTitle.setTextColor(schoolData.getSchoolMedium().equals("HINDI") ? greenColor : redColor);
            studentCount.setText("Students-"+schoolData.getSchoolStudents());
            teacherCount.setText("Teachers-"+schoolData.getSchoolTeachers());
            schoolmedium.setText(schoolData.getSchoolMedium());
            schoolmedium.setTextColor(schoolData.getSchoolMedium().equals("HINDI") ? greenColor : redColor);
            schoolAddress.setText(schoolData.getSchoolAdd());
            schoolstbYear.setText(schoolData.getSchoolYear());
            schoolprinciname.setText(schoolData.getSchoolPrinicipleName());
            schoolprincimobile.setText(schoolData.getSchoolPriniciplePhone());

        }

    }

/*
    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int itemPosition = recyclerView.getChildLayoutPosition(view);
            SchoolData item =dataList.get(itemPosition);

            showDialog(
                    item.getBookId(),
                    item.getBookTitle(),
                    item.getBookCost(),
                    item.getBookAuthor(),
                    item.getBookYear(),
                    item.getBookSubject(),
                    item.getBookAvaibility(),
                    item.getBookLocation(),
                    item.getBookDonor(),
                    item.getBookDonorMobile(),
                    item.getBookDonorTime(),
                    item.getBookHandoverTo(),
                    item.getBookHandoverTime());

        }
    }
    @SuppressLint("InflateParams")
    private void showDialog(final String dbookId,
                            final String dbookTitle,
                            final String dbookCost,
                            final String dbookAuthor,
                            final String dbookYear,
                            final String dbookSubject,
                            final String dbookAvaibility,
                            final String dbookLocation,
                            final String dbookDonor,
                            final String dbookDonorMobile,
                            final String dbookDonorTime,
                            final String dbookHandoverTo,
                            final String dbookHandoverTime) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.show_book_dialog, null);
        dialogBuilder.setView(dialogView);
        TextView dBookid = dialogView.findViewById(R.id.tvBookid);
        TextView dTitle = dialogView.findViewById(R.id.tvTitle);
        TextView dCost = dialogView.findViewById(R.id.tvCost);
        TextView dAuthor = dialogView.findViewById(R.id.tvAuthor);
        TextView dYear = dialogView.findViewById(R.id.tvYear);
        TextView dSubject = dialogView.findViewById(R.id.tvSubject);
        TextView dLocation = dialogView.findViewById(R.id.tvLocation);
        TextView dDonor = dialogView.findViewById(R.id.tvDonor);
        TextView dDonorMobile = dialogView.findViewById(R.id.tvDonorMobile);
        TextView dDonorTime = dialogView.findViewById(R.id.tvDonateTime);
        TextView dIssueTo = dialogView.findViewById(R.id.tvBookIssueto);
        TextView dIssueTime = dialogView.findViewById(R.id.tvIssueTime);
        Button dBack = dialogView.findViewById(R.id.dbuttonBack);


        dialogBuilder.setTitle("Book Details");
        final AlertDialog b = dialogBuilder.create();
        b.show();
        dBookid.setText (dbookId);
        dTitle.setText(dbookTitle);
        dCost.setText(dbookCost);
        dAuthor.setText(dbookAuthor);
        dYear.setText(dbookYear);
        dSubject.setText(dbookSubject);
        dLocation.setText(dbookLocation);
        dDonor.setText(dbookDonor);
        dDonorMobile.setText(dbookDonorMobile);
        dIssueTo.setText(dbookHandoverTo);
        dIssueTime.setText(dbookHandoverTime);
        dDonorTime.setText(dbookDonorTime);
        dBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.cancel();
            }
        });


    }*/

}
