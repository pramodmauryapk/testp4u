package com.p4u.parvarish.Collection;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.p4u.parvarish.R;
import com.p4u.parvarish.user_pannel.Teacher;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static java.util.Objects.requireNonNull;


public  class RecyclerCollection_model extends RecyclerView.Adapter<RecyclerCollection_model.RecyclerViewHolder>{
    private static final String TAG = RecyclerCollection_model.class.getSimpleName();

    private Context mContext;
    private List<Collection_data> teachers;
    private OnItemClickListener mListener;
    private FirebaseAuth auth;
    private String UID,userrole;
    private DatabaseReference mref;

    public RecyclerCollection_model(Context context, List<Collection_data> uploads) {
        mContext = context;
        teachers = uploads;
    }
    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.row_model, parent, false);
        mref = FirebaseDatabase.getInstance().getReference().child("USERS");
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            mref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    UID = requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                    show(dataSnapshot);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else{
            userrole="USER";
        }
            return new RecyclerViewHolder(v);
    }


    private void show(DataSnapshot dataSnapshot){
        try {

            for (DataSnapshot ds : dataSnapshot.getChildren ()) {
                Teacher uInfo=ds.getValue(Teacher.class);
                if(requireNonNull(uInfo).getUserId().equals(UID)) {

                    userrole=uInfo.getUserRole();

                }
            }
        }catch (Exception e){
            Log.d(TAG, "Failed to retrive values"+e);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Collection_data currentTeacher = teachers.get(position);
        holder.nameTextView.setText(currentTeacher.getName());
        holder.descriptionTextView.setText(currentTeacher.getMobile());
        holder.dateTextView.setText(currentTeacher.getAddress());
        Picasso.get()
                .load(currentTeacher.getUrl())
                .placeholder(R.drawable.userpic)
                .fit()
                .centerCrop()
                .into(holder.teacherImageView);
    }

    @Override
    public int getItemCount() {
        return teachers.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        TextView nameTextView,descriptionTextView,dateTextView;
        ImageView teacherImageView;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            nameTextView =itemView.findViewById ( R.id.nameTextView );
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            teacherImageView = itemView.findViewById(R.id.teacherImageView);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            if(userrole.equals("ADMIN")) {
                menu.setHeaderTitle("Select Action");
                MenuItem showItem = menu.add( Menu.NONE, 1, 1, "Show");
                showItem.setOnMenuItemClickListener(this);
                MenuItem deleteItem = menu.add(Menu.NONE, 2, 2, "Delete");
                deleteItem.setOnMenuItemClickListener(this);
            }




        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {

                    switch (item.getItemId()) {
                        case 1:
                            mListener.onShowItemClick(position);
                            return true;
                        case 2:
                            mListener.onDeleteItemClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onShowItemClick(int position);
        void onDeleteItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    private String getDateToday(){
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd");
        Date date=new Date();
        return dateFormat.format(date);
    }
}
