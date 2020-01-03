package com.p4u.parvarish.HelpingHand;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.p4u.parvarish.R;
import com.p4u.parvarish.user_pannel.Teacher;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class UserwiseFragment extends Fragment implements userwiseAdapter_model.OnItemClickListener{

    private static final String TAG = UserwiseFragment.class.getSimpleName();

    private userwiseAdapter_model mAdapter;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef,myRef;
    private ValueEventListener mDBListener,mlistner;

    private List<Teacher> mTeachers;


    private String UID;
    private Context context;

    // newInstance constructor for creating fragment with arguments
    public static UserwiseFragment newInstance(int page) {
        UserwiseFragment fragment = new UserwiseFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        //args.putString("someTitle", title);
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.user_chatfragment, container, false);
        context = container.getContext();
        RecyclerView mRecyclerView = v.findViewById(R.id.mRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        mTeachers = new ArrayList<>();
        mAdapter = new userwiseAdapter_model(context, mTeachers);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("USERS");
      //  myRef=FirebaseDatabase.getInstance().getReference().child("USERS");

       // UID= requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
       /* mlistner=myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                show(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
*/

        return v;
    }

    private void openDetails(String[] data){

        UserwiseDetailsFragment fragment = new UserwiseDetailsFragment();
        Bundle args = new Bundle();
        args.putString("NAME_KEY", data[0]);
        args.putString("EMAIL_KEY", data[1]);
        args.putString("IMAGE_KEY", data[2]);
        args.putString("MOBILE_KEY", data[3]);
        args.putString("ROLE_KEY",data[4]);
        args.putString("IDENTITY_KEY",data[5]);
        args.putString("ADDRESS_KEY",data[6]);
        args.putString("STATUS_KEY",data[7]);
        fragment.setArguments(args);
        switchFragment(fragment);
    }

  /*  private void show(DataSnapshot dataSnapshot){
        try {
            for (DataSnapshot ds : dataSnapshot.getChildren ()) {
                Teacher uInfo=ds.getValue(Teacher.class);
                if(requireNonNull(uInfo).getUserId().equals(UID)) {

                }
            }
        }catch (Exception e){
            Log.d(TAG, "Failed to retrive values"+e);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }*/
    @Override
    public void onStart(){
        super.onStart();
        loadlist();
    }

    private void loadlist() {
        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mTeachers.clear();

                for (DataSnapshot teacherSnapshot : dataSnapshot.getChildren()) {
                    Teacher upload = teacherSnapshot.getValue(Teacher.class);
                    Objects.requireNonNull(upload).setKey(teacherSnapshot.getKey());
                    mTeachers.add(upload);
                }
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Teacher clickedTeacher=mTeachers.get(position);
        String[] teacherData={
                clickedTeacher.getUserName(),
                clickedTeacher.getUserEmail(),
                clickedTeacher.getImageURL(),
                clickedTeacher.getUserMobile(),
                clickedTeacher.getUserRole(),
                clickedTeacher.getUserIdentity(),
                clickedTeacher.getUserAddress(),
                clickedTeacher.getUserStatus()};
        openDetails(teacherData);
    }
       @Override
    public void onShowItemClick(int position) {


    }

    @Override
    public void onDeleteItemClick(int position) {

    }





    private void switchFragment(Fragment fragment) {

        requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .add(R.id.content_frame, fragment)
                .addToBackStack(null).commit();

    }



}
