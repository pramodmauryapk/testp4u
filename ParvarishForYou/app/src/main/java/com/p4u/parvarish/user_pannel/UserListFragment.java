package com.p4u.parvarish.user_pannel;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;
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

import static java.util.Objects.requireNonNull;

public class UserListFragment extends Fragment implements RecyclerAdapter.OnItemClickListener{


    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private ProgressBar mProgressBar;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<Teacher> mTeachers;
    private View dialogView;
    private TextInputEditText dtvUsername,dtvEmail, dtvMobile , dtvCenterName, dtvIdentity;
    private TextView dtvRole;
    private Button dbuttonBack,dbuttonDelete,dbuttonUpdate;
    private View v;
    private FirebaseUser user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_user_list,container,false);

        mRecyclerView = v.findViewById(R.id.mRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        mProgressBar = v.findViewById(R.id.myDataLoaderProgressBar);
        mProgressBar.setVisibility(View.VISIBLE);

        mTeachers = new ArrayList<>();
        mAdapter = new RecyclerAdapter (getContext(), mTeachers);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");
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
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });

        return v;
    }

    private void openDetailActivity(String[] data){

        UserDetailsFragment fragment = new UserDetailsFragment();
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


    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }

    @Override
    public void onItemClick(int position) {
        Teacher clickedTeacher=mTeachers.get(position);
        String[] teacherData={clickedTeacher.getUserName(),
                clickedTeacher.getUserEmail(),
                clickedTeacher.getImageURL(),
                clickedTeacher.getUserMobile(),
        clickedTeacher.getUserRole(),
        clickedTeacher.getUserIdentity(),
        clickedTeacher.getUserAddress(),
        clickedTeacher.getUserStatus()};
        openDetailActivity(teacherData);
    }

    @Override
    public void onShowItemClick(int position) {

        Teacher user = mTeachers.get(position);
        showDeleteDialog(
                user.getUserId(),
                user.getUserName(),
                user.getUserEmail(),
                user.getUserPassword(),
                user.getUserRole(),
                user.getUserMobile(),
                user.getUserAddress(),
                user.getUserIdentity(),
                user.getUserStatus(),
                user.getImageURL());




    }

    @Override
    public void onDeleteItemClick(int position) {
        Teacher selectedItem = mTeachers.get(position);
        final String selectedKey = selectedItem.getKey();

        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageURL());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(selectedKey).removeValue();
                Toast.makeText(UserListFragment.this.getContext(), "Item deleted", Toast.LENGTH_SHORT).show();
            }
        });

    }
    @SuppressLint("InflateParams")
    private void showDeleteDialog(final String userId,
                                  final String userName,
                                  final String userEmail,
                                  final String userPassword,
                                  final String userRole,
                                  final String userMobile,
                                  final String userAddress,
                                  final String userIdentity,
                                  final String userStatus,
                                  final String userImageURL) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder (getContext ());
        LayoutInflater inflater = getLayoutInflater ();
        dialogView = inflater.inflate (R.layout.manage_user_info, null);
        dialogBuilder.setView (dialogView);

        init_dialog_views ();

        dialogBuilder.setTitle ("User Details");
        final AlertDialog b = dialogBuilder.create ();
        b.show ();
       dtvUsername.setText (userName);
        dtvEmail.setText (userEmail);
        dtvMobile.setText (userMobile);
        dtvCenterName.setText (userAddress);
        dtvIdentity.setText (userIdentity);
        dtvRole.setText (userRole);
        dbuttonDelete.setVisibility (View.GONE);
        dbuttonBack.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.cancel();
            }
        });
        dbuttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//Toast.makeText(getContext(),"hello",Toast.LENGTH_LONG).show();
                UserListFragment.this.updatedetails(userId,
                        requireNonNull(dtvUsername.getText()).toString().trim().toUpperCase(),
                        requireNonNull(dtvEmail.getText()).toString().trim().toLowerCase(),
                        userPassword,
                        dtvRole.getText().toString().toUpperCase(),
                        requireNonNull(dtvMobile.getText()).toString().trim(),
                        requireNonNull(dtvCenterName.getText()).toString().toUpperCase(),
                        requireNonNull(dtvIdentity.getText()).toString().trim(),
                        userStatus,
                        userImageURL
                );
            }
        });
    }
    private void init_dialog_views(){

        dtvUsername =  dialogView.findViewById(R.id.etUserName);
        dtvEmail =  dialogView.findViewById(R.id.etEmailID);
        dtvMobile = dialogView.findViewById(R.id.etMobile);
        dtvCenterName = dialogView.findViewById(R.id.tvCenterName);
        dtvIdentity =  dialogView.findViewById(R.id.etIdentity);
        dtvRole =  dialogView.findViewById(R.id.tvRole);
        dbuttonBack = dialogView.findViewById(R.id.dbuttonBack);
        dbuttonUpdate=dialogView.findViewById(R.id.dbuttonUpdate);
        dbuttonDelete =  dialogView.findViewById(R.id.dbuttonDelete);
    }
    private void switchFragment(Fragment fragment) {

        requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null).commit();

    }
    private void updatedetails(String Id,
                               String Name,
                               String Email,
                               String Password,
                               String Role,
                               String Mobile,
                               String address,
                               String Identity,
                               String Status,
                               String ImageURL) {
        //getting the specified artist reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Users").child(Id);

        //updating artist
        Teacher user = new Teacher (
                Id,
                Name,
                Email,
                Password,
                Role,
                Mobile,
                address,
                Identity,
                Status,
                ImageURL);

        dR.setValue(user);
        Toast.makeText(getContext(), "User Updated", Toast.LENGTH_LONG).show();
    }
}
