package com.p4u.parvarish.grid;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.p4u.parvarish.R;


public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.GridItemViewHolder> {
    private Context context;
    private List<Menu> mItemList;
    private AdapterView.OnItemClickListener mOnItemClickListener;


    public RecycleAdapter(Context context, List<Menu> mItemList) {
        this.context = context;
        this.mItemList = mItemList;
    }


@NonNull
    @Override
    public GridItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
        return new GridItemViewHolder(itemView, this);
    }



    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(GridItemViewHolder holder, int position) {
        Menu items = mItemList.get(position);
        holder.mimg.setImageDrawable(items.getmImg());
        holder.mTitle.setText(items.getName());
        holder.mPosition.setText("" + items.getPosition());
    }


    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    private void onItemHolderClick(GridItemViewHolder itemHolder) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(null, itemHolder.itemView,
                    itemHolder.getAdapterPosition(), itemHolder.getItemId());
        }
    }


    public class GridItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mimg;
        TextView mTitle, mPosition;
        RecycleAdapter mAdapter;

        GridItemViewHolder(View itemView, RecycleAdapter mAdapter) {
            super(itemView);
            this.mAdapter = mAdapter;

            mimg= itemView.findViewById(R.id.img);
            mTitle = itemView.findViewById(R.id.item_title);
            mPosition = itemView.findViewById(R.id.item_position);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mAdapter.onItemHolderClick(this);
        }
    }

}
