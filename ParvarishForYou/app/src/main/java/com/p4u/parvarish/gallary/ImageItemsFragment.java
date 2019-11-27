package com.p4u.parvarish.gallary;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.p4u.parvarish.R;


public class ImageItemsFragment extends Fragment implements RecyclerAdapter.OnItemClickListener {
    private static final String TAG = "ImageItemsFragment";
    private RecyclerAdapter mAdapter;
    private ProgressBar mProgressBar;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<Image_Model> mImageModels;
    private Spinner spimagelist;
    private Context context;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_image_items, container, false);
        context = container.getContext();

        RecyclerView mRecyclerView = v.findViewById(R.id.mRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        mProgressBar = v. findViewById(R.id.myDataLoaderProgressBar);
        mProgressBar.setVisibility(View.VISIBLE);
        spimagelist=v.findViewById(R.id.spimagelist);
        mImageModels = new ArrayList<> ();
        mAdapter = new RecyclerAdapter (context, mImageModels);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(ImageItemsFragment.this);

        mStorage = FirebaseStorage.getInstance();

        spimagelist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spimagelist.getSelectedItem().equals("UPLOADED_IMAGES")){
                    mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("UPLOADED_IMAGES");

                    load_list();
                }else{
                    mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("ADS");
                    load_list();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("ADS");
                load_list();
            }
        });



    return v;
    }

    private void load_list() {
        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mImageModels.clear();

                for (DataSnapshot teacherSnapshot : dataSnapshot.getChildren()) {
                    Image_Model upload = teacherSnapshot.getValue(Image_Model.class);
                    Objects.requireNonNull(upload).setKey(teacherSnapshot.getKey());
                    mImageModels.add(upload);
                }
                mAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }


    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onShowItemClick(int position) {

    }

    @Override
    public void onDeleteItemClick(int position) {
        Image_Model selectedItem = mImageModels.get(position);
        final String selectedKey = selectedItem.getKey();

        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(selectedKey).removeValue();
                Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }

}

