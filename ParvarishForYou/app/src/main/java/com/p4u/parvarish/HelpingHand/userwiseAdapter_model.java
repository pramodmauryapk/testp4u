package com.p4u.parvarish.HelpingHand;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.p4u.parvarish.R;
import com.p4u.parvarish.user_pannel.Teacher;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public  class userwiseAdapter_model extends RecyclerView.Adapter<userwiseAdapter_model.RecyclerViewHolder>{
    private static final String TAG = "userwiseAdapter_model";
    private Context mContext;
    private List<Teacher> teachers;
    private OnItemClickListener mListener;

    public userwiseAdapter_model(Context context, List<Teacher> uploads) {
        mContext = context;
        teachers = uploads;
    }
    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_list, parent, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Teacher currentTeacher = teachers.get(position);
        // Capture position and set to the TextViews
        holder.txtname.setText(currentTeacher.getUserName());

        holder.txtmessage.setText(currentTeacher.getUserTime());

        Picasso.get()
                .load(currentTeacher.getImageURL())
                .placeholder(R.drawable.userpic)
                .fit()
                .centerCrop()
                .into(holder.imagename);

    }

    @Override
    public int getItemCount() {
        return teachers.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        TextView txtname,txtmessage;
        CircleImageView imagename;

        RecyclerViewHolder(View itemView) {
            super(itemView);

            // Locate the TextViews in listview_item.xml
            txtname = itemView.findViewById(R.id.txt_name);

            txtmessage = itemView.findViewById(R.id.txt_msg);

            // Locate the ImageView in listview_item.xml
            imagename =  itemView.findViewById(R.id.image_data);
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
            menu.setHeaderTitle("Select Action");
            MenuItem showItem = menu.add( Menu.NONE, 1, 1, "Show");
            MenuItem deleteItem = menu.add(Menu.NONE, 2, 2, "Delete");

            showItem.setOnMenuItemClickListener(this);
            deleteItem.setOnMenuItemClickListener(this);
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
