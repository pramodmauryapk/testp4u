package com.p4u.parvarish.user_pannel;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.p4u.parvarish.R;
import com.p4u.parvarish.fancydialog.Animation;
import com.p4u.parvarish.fancydialog.FancyAlertDialog;
import com.p4u.parvarish.fancydialog.FancyAlertDialogListener;
import com.p4u.parvarish.fancydialog.Icon;

import static android.app.Activity.RESULT_OK;
import static java.util.Objects.requireNonNull;

public class ManageUserFragment extends Fragment implements RecyclerAdapter_model.OnItemClickListener{

    private static final String TAG = "ManageUserFragment";
    private RecyclerAdapter_model mAdapter;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef,myRef;
    private ValueEventListener mDBListener,mlistner;
    private TextInputLayout l1;
    private TextInputLayout l2;
    private TextInputLayout l3;
    private TextInputLayout l4;
    private List<Teacher> mTeachers;
    private View dialogView;
    private TextInputEditText username,email, mobile,location,role;
    private ImageView imageView;
    private Button change,save,upload,choose;
    private Spinner sp;
    private String UID,userrole,id;
    private Context context;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_user_list, container, false);
        context = container.getContext();
        RecyclerView mRecyclerView = v.findViewById(R.id.mRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        mTeachers = new ArrayList<>();
        mAdapter = new RecyclerAdapter_model(context, mTeachers);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("USERS");
        myRef=FirebaseDatabase.getInstance().getReference().child("USERS");
        UID= requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        mlistner=myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                show(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

        return v;
    }

    private void openDetails(String[] data){

        UserDetailsFragment fragment = new UserDetailsFragment();
        Bundle args = new Bundle();
        args.putString("NAME_KEY", data[0]);
        args.putString("EMAIL_KEY", data[1]);
        args.putString("IMAfGE_KEY", data[2]);
        args.putString("MOBILE_KEY", data[3]);
        args.putString("ROLE_KEY",data[4]);
        args.putString("IDENTITY_KEY",data[5]);
        args.putString("ADDRESS_KEY",data[6]);
        args.putString("STATUS_KEY",data[7]);
        fragment.setArguments(args);
        switchFragment(fragment);
    }

    private void show(DataSnapshot dataSnapshot){
        try {
            for (DataSnapshot ds : dataSnapshot.getChildren ()) {
                Teacher uInfo=ds.getValue(Teacher.class);
                if(requireNonNull(uInfo).getUserId().equals(UID)) {

                    userrole=uInfo.getUserRole();

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
    private boolean validate(String name, String email, String mobile, String address) {

        if (TextUtils.isEmpty(name)) {
            l1.setError("Enter Name");
            return false;
        }
        if (TextUtils.isEmpty(email)) {
            l2.setError("Enter Email");
            return false;
        }
        if (TextUtils.isEmpty(address)) {
            l4.setError("Enter Address");
            return false;
        }
        if (mobile.length() != 10) {
            l3.setError("Enter Mobile");
            return false;
        }


        return true;
    }
    @Override
    public void onShowItemClick(int position) {

        Teacher user = mTeachers.get(position);
        showDeleteDialog(
                user.getUserId(),
                user.getUserName(),
                user.getUserEmail(),
                user.getUserRole(),
                user.getUserMobile(),
                user.getUserAddress());


    }

    @Override
    public void onDeleteItemClick(int position) {
        Teacher selectedItem = mTeachers.get(position);
        final String selectedKey = selectedItem.getKey();
        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageURL());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                new FancyAlertDialog.Builder(getActivity())
                        .setTitle("Rate us if you like the app")
                        .setMessage("Do you really want to Delete User?")
                        .setNegativeBtnText("Cancel")
                        .setPositiveBtnText("Delete")
                        .setAnimation(Animation.POP)
                        .isCancellable(true)
                        .setIcon(R.drawable.logo, Icon.Visible)
                        .OnPositiveClicked(new FancyAlertDialogListener() {
                            @Override
                            public void OnClick() {

                                mDatabaseRef.child(selectedKey).removeValue();
                                Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .OnNegativeClicked(new FancyAlertDialogListener() {
                            @Override
                            public void OnClick() {



                            }
                        })
                        .build();

            }
        });

    }
    @SuppressLint("InflateParams")
    private void showDeleteDialog(final String userId,
                                  final String userName,
                                  final String userEmail,
                                  final String userRole,
                                  final String userMobile,
                                  final String userAddress) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder (context);
        LayoutInflater inflater = getLayoutInflater ();
        dialogView = inflater.inflate (R.layout.layout_profile, null);
        dialogBuilder.setView (dialogView);
        dialogBuilder.setTitle ("User Details");
        final AlertDialog b = dialogBuilder.create ();
        b.show ();
        init_dialog_views ();
        setValues(userName,userEmail,userMobile,userAddress,userRole);

        change.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFragment(new UpdatePasswordFragment());
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               boolean ans= updatedetails(userId);
               if(ans)
                   Toast.makeText(context, "User Updated", Toast.LENGTH_LONG).show();
               else
                   Toast.makeText(context, "User Not Updated", Toast.LENGTH_LONG).show();

            }
        });
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseimage(view);
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(context, "An Upload is Still in Progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile(userId);

                }
            }
        });
    }
    private void uploadFile(final String id) {

        if (filePath != null) {
            final ProgressDialog progressDialog=new ProgressDialog(context);
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            mStorageRef = FirebaseStorage.getInstance().getReference("USERS_IMAGES");
            final StorageReference sRef = mStorageRef.child(System.currentTimeMillis()+ "." + getFileExtension(filePath));

            sRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            progressDialog.dismiss();
                            myRef.child(id).child("imageURL").setValue(uri.toString());
                            //displaying success toast
                            Toast.makeText(context, "File Uploaded ", Toast.LENGTH_LONG).show();

                        }

                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    //displaying the upload progress
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                }
            });
        } else{
            Toast.makeText(context, "You haven't Selected Any file", Toast.LENGTH_SHORT).show();
        }
    }
    private void chooseimage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(context.getContentResolver(),filePath);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = requireNonNull(context.getContentResolver());
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void setValues(String userName,String userEmail,String userMobile,String userAddress,String userRole) {
        username.setText (userName);
        email.setText (userEmail);
        mobile.setText (userMobile);
        location.setText (userAddress);
        if(userrole.equals("ADMIN")){
            role.setVisibility(View.GONE);
            sp.setVisibility(View.VISIBLE);

        }
        else {
            role.setText(userRole);
            role.setEnabled(false);
        }

    }

    private void init_dialog_views(){

        username=dialogView.findViewById(R.id.etUserName);
        email=dialogView.findViewById(R.id.etEmailID);
        mobile=dialogView.findViewById(R.id.etMobile);
        location=dialogView.findViewById(R.id.tvCenterName);
        role=dialogView.findViewById(R.id.tvRole);
        sp=dialogView.findViewById(R.id.sprole);
        imageView=dialogView.findViewById(R.id.imgView);
        change=dialogView.findViewById(R.id.dbuttonchange);
        save=  dialogView.findViewById(R.id.dbuttonsave);
        choose=  dialogView.findViewById(R.id.btnChoose);
        upload=  dialogView.findViewById(R.id.upload_button);
        l1=dialogView.findViewById(R.id.l1);
        l2=dialogView.findViewById(R.id.l2);
        l3=dialogView.findViewById(R.id.l3);
        l4=dialogView.findViewById(R.id.l4);
        change_listner(username,l1);
        change_listner(email,l2);
        change_listner(mobile,l3);
        change_listner(location,l4);
    }
    private void change_listner(final TextView v, final TextInputLayout til){



        v.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                til.setErrorEnabled(false);
            }

            public void beforeTextChanged(CharSequence s, int start,int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,int before, int count) {

            }

        });
    }
    private void switchFragment(Fragment fragment) {

        requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null).commit();

    }
    private boolean updatedetails(String Id) {
        //getting the specified artist reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("USERS").child(Id);
        String Name = requireNonNull(username.getText()).toString().trim().toUpperCase();
        String Email = requireNonNull(email.getText()).toString();
        String Role;
        if (userrole.equals("ADMIN")) {
            Role = sp.getSelectedItem().toString().toUpperCase();
        } else {
            Role = requireNonNull(role.getText()).toString().toUpperCase();
        }
        String Mobile = requireNonNull(mobile.getText()).toString().trim();
        String Location = requireNonNull(location.getText()).toString().toUpperCase();
        boolean ans = validate(Name, Email, Mobile, Location);
        if (ans) {

                dR.child("userName").setValue(Name);
                dR.child("userEmail").setValue(Email);
                dR.child("userRole").setValue(Role);
                dR.child("userMobile").setValue(Mobile);
                dR.child("userAddress").setValue(Location);
                dR.child("userIdentity").setValue("");
                dR.child("userTime").setValue(get_current_time());

            }
        else {
            return false;
        }

        return true;
    }

    private String get_current_time(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss");
        return sdf.format(new Date());
    }

}
