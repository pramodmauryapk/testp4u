package com.p4u.parvarish.MenuPages;

import android.annotation.SuppressLint;
import android.content.Context;
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

import com.p4u.parvarish.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public  class PageRecyclerAdapter_model extends RecyclerView.Adapter<PageRecyclerAdapter_model.RecyclerViewHolder>{
    private static final String TAG = "userwiseAdapter_model";
    private Context mContext;
    private List<Page_data_Model> articles;
    private OnItemClickListener mListener;

    PageRecyclerAdapter_model(Context context, List<Page_data_Model> uploads) {
        mContext = context;
        articles = uploads;
    }
    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.layout_page, parent, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Page_data_Model article = articles.get(position);
        holder.Title.setText(article.getTitle());
        holder.Article.setText(article.getDescription());
        Picasso.get()
                .load(article.getImageUrl())
                .placeholder(R.drawable.logo)
                .fit()
                .into(holder.img);

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        TextView Title,Article;
        ImageView img;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            Title = itemView.findViewById(R.id.headingTv);
            Article = itemView.findViewById(R.id.articleTv);
            img = itemView.findViewById(R.id.rImageView);

            img.setOnClickListener(this);
            Title.setOnCreateContextMenuListener(this);
            img.setOnCreateContextMenuListener(this);
            Article.setOnCreateContextMenuListener(this);
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
