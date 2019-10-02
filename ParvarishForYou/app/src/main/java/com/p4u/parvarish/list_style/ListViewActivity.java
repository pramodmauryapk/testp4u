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
import com.p4u.parvarish.admin_pannel.Book;

public class ListViewActivity extends AppCompatActivity {


    private List<Book> dataList = new ArrayList<>();
    private int redColor, greenColor;
    private RecyclerView.Adapter adapter;
    private Handler handler;
    private int currentPage = 0;
    private SwipeRefreshLayout refreshLayout;
    private DatabaseReference databaseBooks;
    RecyclerView recyclerView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        databaseBooks = FirebaseDatabase.getInstance().getReference().child("books");

      // 2. toolbar
      //  Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
       Objects.requireNonNull(getSupportActionBar()).hide();
       // setSupportActionBar(toolbar);
       // Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        recyclerView = findViewById(R.id.main_recycler_view);
        recyclerView.setLayoutManager(new spreadLayoutManager());
        adapter = getAdapter();
        recyclerView.setAdapter(adapter);

        redColor = getResources().getColor(R.color.red);
        greenColor = getResources().getColor(R.color.green);

        appendDataList();
        adapter.notifyDataSetChanged();

        // 4. refreshLayout
        refreshLayout = findViewById(R.id.refresh_layout);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 0;
                ListViewActivity.this.requestHttp();
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
                ListViewActivity.this.appendDataList();
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
    private RecyclerView.Adapter getAdapter() {
        final LayoutInflater inflater = LayoutInflater.from(this);
        RecyclerView.Adapter adapter = new RecyclerView.Adapter() {
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
        return adapter;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView bookTitle;
        TextView bookCost;
        ImageView sybool;
        TextView bookAuthor;
        TextView bookSubject;

        MyViewHolder(View itemView) {
            super(itemView);
            this.bookTitle = (TextView) itemView.findViewById(R.id.item_name_tv);
            this.bookCost = (TextView) itemView.findViewById(R.id.item_current_price);
            this.sybool = (ImageView) itemView.findViewById(R.id.item_trend_flag);
            this.bookAuthor = (TextView) itemView.findViewById(R.id.item_gross);
            this.bookSubject=(TextView)itemView.findViewById(R.id.txtsubject);

        }

        @SuppressLint("SetTextI18n")
        void bindData(Book book) {
            bookTitle.setText(book.getBookTitle());
            bookTitle.setTextColor(redColor);
            bookCost.setText(book.getBookCost());
            sybool.setImageResource(R.drawable.index);
            //trendFlagIv.setImageResource(stockEntity.getFlag() > 0 ? R.drawable.up_red : R.drawable.down_green);
            bookSubject.setText(book.getBookSubject());
            bookAuthor.setText(book.getBookAuthor());
           // nameTv.setTextColor(((int) book.getBookAvaibility() > 0) ? redColor : greenColor);
        }

    }
}
