package com.p4u.parvarish.video;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.p4u.parvarish.R;

import java.util.Objects;


public class RecyclerViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_example);
        Objects.requireNonNull(getSupportActionBar()).hide();
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        String[] videoIds = {"umUTvb5sGBE", "ltmlWH3V320", "P6Pq7T9uMWU", "2VTamBUXg7c", "isRwSpNJleg", "WFKNUpD2ryA", "g0ksoZ-WC1Q", "B5Fn7qaNSX8", "NERIn6YkpgM", "JGGto2YkXaY"};

        RecyclerView.Adapter recyclerViewAdapter = new RecyclerViewAdapter(videoIds, this.getLifecycle());
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }

}
