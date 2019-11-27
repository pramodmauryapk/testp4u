package com.p4u.parvarish.menu_data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.p4u.parvarish.R;
import com.p4u.parvarish.user_pannel.Teacher;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.util.Objects.requireNonNull;


public  class TimelineRecyclerAdapter_model extends RecyclerView.Adapter<TimelineRecyclerAdapter_model.RecyclerViewHolder>{
    private static final String TAG = "TimelineRecyclerAdapter_model";
    private Context mContext;
    private List<Article_Model> articles;
    private OnItemClickListener mListener;
    private String uid="Unknown";
    TimelineRecyclerAdapter_model(Context context, List<Article_Model> uploads) {
        mContext = context;
        articles = uploads;
    }
    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_timeline_item, parent, false);
        return new RecyclerViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        final Article_Model article = articles.get(position);


      /*  if(requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid().equals(uid)){

            //CardView.LayoutParams params = new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //params.gravity = Gravity.END;
            //holder.cardView.setLayoutParams(params);

        }*/
        holder.Title.setText(article.getTitle()+":");
        if(article.getDescription().equals("")) {
            holder.Article.setVisibility(View.GONE);
        }else{
            holder.Article.setVisibility(View.VISIBLE);
            holder.Article.setText(article.getDescription());
        }
        if(article.getImageUrl()!=null) {
            holder.img.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(article.getImageUrl())
                    .placeholder(R.drawable.logo)
                    .fit()
                    .centerInside()
                    .into(holder.img);
        }else{
            holder.img.setVisibility(View.GONE);
        }

        DatabaseReference myref = FirebaseDatabase.getInstance().getReference().child("USERS");
        myref.addValueEventListener(new ValueEventListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uid=article.getUserid();

                if(!uid.equals("Unknown")) {

                    Picasso.get()
                            .load(getting_userpic(dataSnapshot, uid))
                            .placeholder(R.drawable.userpic)
                            .fit()
                            .centerInside()
                            .into(holder.userpic);
                }else{
                    Picasso.get()
                            .load(R.drawable.userpic)
                            .placeholder(R.drawable.userpic)
                            .fit()
                            .centerInside()
                            .into(holder.userpic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                 Log.d(TAG, "failed to read values", databaseError.toException());
            }
        });
        holder.datetime.setText(article.getTime());



    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        TextView Title,Article,datetime;
        ImageView img;
        CircleImageView userpic;
        CardView cardView;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            Title = itemView.findViewById(R.id.headingTv);
            Article = itemView.findViewById(R.id.articleTv);
            img = itemView.findViewById(R.id.rImageView);
            userpic=itemView.findViewById(R.id.userpic);
            datetime=itemView.findViewById(R.id.datetimetv);
            cardView=itemView.findViewById(R.id.recyclercard);
            cardView.setOnClickListener(this);
            cardView.setOnCreateContextMenuListener(this);

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
            MenuItem showItem = menu.add( Menu.NONE, 1, 1, "Share");
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
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private String getting_userpic(DataSnapshot dataSnapshot,String user_id) {
        String s=null;
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Teacher uInfo=ds.getValue(Teacher.class);
            if(requireNonNull(uInfo).getUserId().equals(user_id)) {
                s=  requireNonNull(uInfo).getImageURL();

           }

        }
        return s;
    }

}
