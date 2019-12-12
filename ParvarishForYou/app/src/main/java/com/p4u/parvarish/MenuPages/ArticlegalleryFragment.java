package com.p4u.parvarish.MenuPages;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.p4u.parvarish.R;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class ArticlegalleryFragment extends Fragment{

    private static final String TAG = "youtubegalleryFragment";

    private Context context;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private ArticleRecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private List<Page_data_Model> mGallaryList;
    private TextView textView;
   private Bundle bundle;
    private View v;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.newgallery, container, false);
        context = container.getContext();
        initViews();
        textView.setText("Article Gallery");


        GridLayoutManager mGridLayoutManager = new GridLayoutManager(context, 1);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setHasFixedSize(false);
        mGallaryList = new ArrayList<>();
        mAdapter = new ArticleRecyclerAdapter(getContext(), mGallaryList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ArticleRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Page_data_Model imgdetails = mGallaryList.get(position);
                String[] teacherData={
                        imgdetails.getTitle(),
                        imgdetails.getImageUrl(),
                        imgdetails.getDescription()};
                        openDetails(teacherData);

            }

            @Override
            public void onShowItemClick(int position) {

            }

            @Override
            public void onDeleteItemClick(int position) {

            }
        });

       mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("PAGES").child("ARTICLES");

        return v;
    }
    private void initViews(){
        mRecyclerView = v.findViewById(R.id.recyclerview);
        textView=v.findViewById(R.id.textView);
    }
    public void onStart() {

        super.onStart();
        load_list();

    }

    private void load_list() {
        try {
            mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    mGallaryList.clear();

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                       Page_data_Model article = postSnapshot.getValue(Page_data_Model.class);

                        mGallaryList.add(0,article);

                    }
                    mRecyclerView.smoothScrollToPosition(0);
                    mAdapter.notifyDataSetChanged();




                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });


        }catch (Exception e){

        }

    }

    private void openDetails(String[] data){


        bundle = new Bundle();
        bundle.putString("Title", data[0]);
        bundle.putString("Image", data[1]);
        bundle.putString("Description", data[2]);
        switchFragment(new ArticledetailFragment());


    }
    private void switchFragment(Fragment fragment) {

        if(getActivity()!=null) {
            fragment.setArguments(bundle);
            requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .addToBackStack(null).commit();
        }

    }
}


