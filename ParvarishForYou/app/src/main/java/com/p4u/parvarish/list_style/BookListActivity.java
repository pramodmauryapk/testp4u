package com.p4u.parvarish.list_style;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.p4u.parvarish.R;
import com.p4u.parvarish.book_pannel.Book;
import com.p4u.parvarish.galary.RecyclerAdapter;

public class BookListActivity extends AppCompatActivity {

    private static final String TAG = "BookListActivity";
    private List<Book> dataList = new ArrayList<>();
    private int redColor;
    private RecyclerView.Adapter adapter;
    private Handler handler;
    private int currentPage = 0;
    private SwipeRefreshLayout refreshLayout;
    private DatabaseReference databaseBooks;
    RecyclerView recyclerView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booklist);
        databaseBooks = FirebaseDatabase.getInstance().getReference().child("BOOKS");

        Objects.requireNonNull(getSupportActionBar()).hide();
        recyclerView = findViewById(R.id.main_recycler_view);
        recyclerView.setLayoutManager(new spreadLayout());
        adapter = getAdapter();
        recyclerView.setAdapter(adapter);

       // recyclerView.setOnClickListener();

        redColor = getResources().getColor(R.color.red);
        int greenColor = getResources().getColor(R.color.green);

        appendDataList();
        adapter.notifyDataSetChanged();


        // 4. refreshLayout
        refreshLayout = findViewById(R.id.refresh_layout);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 0;
                BookListActivity.this.requestHttp();
            }
        });
    }

    private void requestHttp() {
        if (null == handler) {
            handler = new Handler();
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
                BookListActivity.this.appendDataList();
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
        final LayoutInflater inflater = LayoutInflater.from(this);
        return new RecyclerView.Adapter() {
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = inflater.inflate(R.layout.wega_recycler_item, parent, false);
                return new MyViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                MyViewHolder myHolder = (MyViewHolder) holder;
               myHolder.bindData(dataList.get(position));

          //      if (position == dataList.size() - 1) {
                  //  requestHttp();
               // }
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
            this.bookSubject= itemView.findViewById(R.id.txtsubject);

        }

       @SuppressLint("SetTextI18n")
        void bindData(final Book book) {
            bookTitle.setText(book.getBookTitle());
            bookTitle.setTextColor(redColor);
            bookCost.setText(book.getBookCost());
            sybool.setImageResource(R.drawable.index);
            //trendFlagIv.setImageResource(stockEntity.getFlag() > 0 ? R.drawable.up_red : R.drawable.down_green);
            bookSubject.setText(book.getBookSubject());
            bookAuthor.setText(book.getBookAuthor());
           // nameTv.setTextColor(((int) book.getBookAvaibility() > 0) ? redColor : greenColor);

        }

       /* public void bindData(final Book book, final OnItemClickListener listener) {
            bookTitle.setText(book.getBookTitle());
            bookTitle.setTextColor(redColor);
            bookCost.setText(book.getBookCost());
            sybool.setImageResource(R.drawable.index);
            //trendFlagIv.setImageResource(stockEntity.getFlag() > 0 ? R.drawable.up_red : R.drawable.down_green);
            bookSubject.setText(book.getBookSubject());
            bookAuthor.setText(book.getBookAuthor());
            // nameTv.setTextColor(((int) book.getBookAvaibility() > 0) ? redColor : greenColor);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                listener.onItemClick(item);
            }
        });
*/
    }
}
