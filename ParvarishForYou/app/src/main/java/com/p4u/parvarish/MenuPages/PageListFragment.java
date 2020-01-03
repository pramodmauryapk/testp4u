package com.p4u.parvarish.MenuPages;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.p4u.parvarish.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class PageListFragment extends Fragment {
    private Context context;
    private List<Page_data_Model> article_array;
    private DatabaseReference myRef;
    private ListView listViewArticles;
    private View v;


    private static final String TAG = PageListFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_page_list, container, false);
        //Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        assert this.getArguments() != null;
        String s = this.getArguments().getString("position");
        switch(Integer.parseInt(Objects.requireNonNull(s))){
            case 0: myRef = FirebaseDatabase.getInstance().getReference().child("PAGES").child("TELENT_SUPPORT");
                break;
            case 2: myRef = FirebaseDatabase.getInstance().getReference().child("PAGES").child("TECHNICAL_PARTNERSHIP");
                break;
            case 3: myRef = FirebaseDatabase.getInstance().getReference().child("PAGES").child("CAPACITY_BUILDING");
                break;
            case 4: myRef = FirebaseDatabase.getInstance().getReference().child("PAGES").child("INTERNSHIP_PROGRAM");
                break;
          //  case 5: myRef = FirebaseDatabase.getInstance().getReference().child("PAGES").child("ARTICLES");
            //    break;
        }

        context = container.getContext();
        initViews();

        article_array = new ArrayList<>();

        return v;
    }
    private void initViews(){
        listViewArticles =  v.findViewById(R.id.view_list);

    }
    public void onStart() {

        super.onStart();
        load_list();

    }
    @SuppressLint("LongLogTag")
    private void load_list() {
        try {

                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        article_array.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Page_data_Model article = postSnapshot.getValue(Page_data_Model.class);
                            article_array.add(article);

                        }

                            LayoutPage articleAdapter = new LayoutPage(getActivity(), article_array);
                            listViewArticles.setAdapter(articleAdapter);
                            articleAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        }catch (Exception e){
            Log.d(TAG, "Exception in Adding List "+e);
        }
    }

}
