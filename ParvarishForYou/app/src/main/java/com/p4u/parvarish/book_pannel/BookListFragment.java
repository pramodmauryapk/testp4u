package com.p4u.parvarish.book_pannel;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class BookListFragment extends Fragment {

    private static final String TAG = "ListBookFragment";
    private List<Book> dataList = new ArrayList<>();
    private int redColor,greenColor;
    private RecyclerView.Adapter adapter;
    private Handler handler;
    private SwipeRefreshLayout refreshLayout;
    private DatabaseReference databaseBooks;
    private RecyclerView recyclerView;
    private View v;
    private final View.OnClickListener mOnClickListener = new MyOnClickListener();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_booklist, container, false);
        databaseBooks = FirebaseDatabase.getInstance().getReference().child("BOOKS");

        initViews();
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

        databaseBooks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                dataList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Book book = ds.getValue(Book.class);
                    dataList.add(book);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    /*
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            if (item.getItemId() == android.R.id.home) {
                dataList.remove(0);
                adapter.notifyItemRemoved(0);
            }
            return super.onOptionsItemSelected(item);
        }
    */
    @NonNull
    private RecyclerView.Adapter getAdapter() {
        final LayoutInflater inflater = LayoutInflater.from(getContext());
        return new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
                View view = inflater.inflate(R.layout.layout_wega_item, parent, false);
                view.setOnClickListener(mOnClickListener);
             /*   view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(),"hi"+viewType,Toast.LENGTH_LONG).show();
                    }
                });*/
                return new MyViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                BookListFragment.MyViewHolder myHolder = (BookListFragment.MyViewHolder) holder;
                myHolder.bindData(dataList.get(position));


            }

            @Override
            public int getItemCount() {
                return dataList.size();
            }

        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView bookTitle;
        TextView bookCost;
        ImageView sybool;
        TextView bookAuthor;
        TextView bookSubject;

        MyViewHolder(View itemView) {
            super(itemView);
            this.bookTitle = itemView.findViewById(R.id.item_name_tv);
            this.bookCost = itemView.findViewById(R.id.item_current_price);
            this.sybool = itemView.findViewById(R.id.item_trend_flag);
            this.bookAuthor = itemView.findViewById(R.id.item_gross);
            this.bookSubject = itemView.findViewById(R.id.txtsubject);

        }

        @SuppressLint("SetTextI18n")
        void bindData(final Book book) {
            bookTitle.setText(book.getBookTitle());
            bookTitle.setTextColor((Integer.parseInt(book.getBookAvaibility()) > 0) ? greenColor : redColor);

            bookCost.setText(book.getBookCost());
            sybool.setImageResource(R.drawable.index);

            bookSubject.setText(book.getBookSubject());

            bookAuthor.setText(book.getBookAuthor());
            bookAuthor.setTextColor((Integer.parseInt(book.getBookAvaibility()) > 0) ? greenColor : redColor);


        }

    }


    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int itemPosition = recyclerView.getChildLayoutPosition(view);
            Book item =dataList.get(itemPosition);

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

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
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


    }

}
