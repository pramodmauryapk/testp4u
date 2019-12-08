package com.p4u.parvarish.Attandence.admin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TeacherListFragment extends Fragment {

    private static final String TAG = "ListschookFragment";
    private List<TeacherData> dataList = new ArrayList<>();
    private int redColor,greenColor;
    private RecyclerView.Adapter adapter;
    private TextView tv1,tv2;
    private Handler handler;
    private DatabaseReference teacherref;
    private RecyclerView recyclerView;
    private View v;
    private SwipeRefreshLayout refreshLayout;
    private Context context;
    //private final View.OnClickListener mOnClickListener = new MyOnClickListener();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_schoollist, container, false);
        teacherref = FirebaseDatabase.getInstance().getReference().child("SCHOOL").child("TEACHERS");
        context = container.getContext();
        initViews();
        tv1.setText("TEACHERS LIST");
        tv2.setText("Choose to find teacher");
        recyclerView.setLayoutManager(new spreadLayout());
        adapter = getAdapter();
        recyclerView.setAdapter(adapter);

        redColor = getResources().getColor(R.color.red);
        greenColor = getResources().getColor(R.color.green);

        appendDataList();
        adapter.notifyDataSetChanged();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestHttp();
            }
        });
        return v;
    }
    private void initViews(){
        recyclerView =v. findViewById(R.id.main_recycler_view);
        tv1=v.findViewById(R.id.tv1);
        tv2=v.findViewById(R.id.tv2);
        refreshLayout = v.findViewById(R.id.refresh_layout);
    }
    private void requestHttp() {
        if (null == handler) {
            handler = new Handler();
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
                appendDataList();
                adapter.notifyDataSetChanged();
            }
        }, 900);
    }

    private void appendDataList() {

        teacherref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                dataList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    TeacherData teacher = ds.getValue(TeacherData.class);
                    dataList.add(teacher);
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
                View view = inflater.inflate(R.layout.layout_teacher_item, parent, false);
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
                TeacherListFragment.MyViewHolder myHolder = (TeacherListFragment.MyViewHolder) holder;
                myHolder.bindData(dataList.get(position));


            }

            @Override
            public int getItemCount() {
                return dataList.size();
            }

        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView Name;
        TextView Father;
        TextView Empcode;
        TextView Class;
        TextView Mobile;
        TextView Address;
        ImageView img;

        MyViewHolder(View itemView) {
            super(itemView);
            this.Name = itemView.findViewById(R.id.item_name);
            this.Empcode = itemView.findViewById(R.id.item_admissionno);
            this.Father = itemView.findViewById(R.id.item_fathername);
            this.Class = itemView.findViewById(R.id.item_class);
            this.Mobile = itemView.findViewById(R.id.item_homemobile);
            this.Address=itemView.findViewById(R.id.item_Address);
            this.img=itemView.findViewById(R.id.img);



        }

        @SuppressLint("SetTextI18n")
        void bindData(final TeacherData teacherData) {
            Name.setText(teacherData.getTeacherName());
            Name.setTextColor(teacherData.getTeachergender().equals("MALE") ? greenColor : redColor);
            Empcode.setText("EMPCODE-"+teacherData.getTeacherEmployeecode());
            Father.setText("FATHER NAME-"+teacherData.getTeacherfathername());
            Class.setText("CLASS-"+teacherData.getTeacherclass()+"-"+teacherData.getTeachersection());
            Mobile.setText("MOBILE-"+teacherData.getTeachermobile());
            Address.setText(teacherData.getTeacheraddress()+" PIN- "+teacherData.getTeacherpin());
            Address.setTextColor(teacherData.getTeachergender().equals("MALE") ? redColor : greenColor);
            Picasso.get()
                    .load(teacherData.getTeacherimgurl())
                    .placeholder(R.drawable.userpic)
                    .centerCrop()
                    .fit()
                    .into(img);
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
