package com.p4u.parvarish.video;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class youtubegalleryFragment extends Fragment{

    private static final String TAG = youtubegalleryFragment.class.getSimpleName();


    private Context context;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private youtubeRecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private List<YoutubeVideo_Model> mGallaryList;

   private Bundle bundle;
    private View v;
    public youtubegalleryFragment(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.newgallery, container, false);
        context = container.getContext();
        initViews();



        GridLayoutManager mGridLayoutManager = new GridLayoutManager(context, 1);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setHasFixedSize(false);
        mGallaryList = new ArrayList<>();
        mAdapter = new youtubeRecyclerAdapter(getContext(), mGallaryList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new youtubeRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                YoutubeVideo_Model imgdetails = mGallaryList.get(position);
                String[] teacherData={

                        imgdetails.getVideoURL(),
                        imgdetails.getName()};
                 openDetails(teacherData);

            }

            @Override
            public void onShowItemClick(int position) {

            }

            @Override
            public void onDeleteItemClick(int position) {

            }
        });

       mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("YOUTUBE_VIDEOS");

        return v;
    }
    private void initViews(){
        mRecyclerView = v.findViewById(R.id.recyclerview);
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
                       YoutubeVideo_Model article = postSnapshot.getValue(YoutubeVideo_Model.class);

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
        bundle.putString("Video", data[0]);
        bundle.putString("Description", data[1]);
        switchFragment(new videodetailFragment());

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


