package com.p4u.parvarish.Collection;

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
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.p4u.parvarish.R;
import com.p4u.parvarish.fancydialog.Animation;
import com.p4u.parvarish.fancydialog.FancyAlertDialog;
import com.p4u.parvarish.fancydialog.FancyAlertDialogListener;
import com.p4u.parvarish.fancydialog.Icon;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static java.util.Objects.requireNonNull;

public class CollectionCenterFragment extends Fragment implements RecyclerCollection_model.OnItemClickListener{

    private static final String TAG = CollectionCenterFragment.class.getSimpleName();

    private RecyclerCollection_model mAdapter;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef,myRef;
    private ValueEventListener mDBListener,mlistner;
    private TextInputLayout l1;
    private TextInputLayout l2;
    private TextInputLayout l3;
    private TextInputLayout l4;
    private List<Collection_data> mTeachers;
    private View dialogView;
    private DatabaseReference databaseUsers;
    private Context context;
    private TextInputEditText tv1,tv2,tv3;
    private ImageButton btnupload;

    private StorageReference mStorageRef;
    private ImageView img;
    private Button btnpost;
    private Uri filePath,imageUri;
    private StorageTask mUploadTask;
    private View v;
    private TextView textView;

    private final int PICK_IMAGE_REQUEST = 71;

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_user_list, container, false);
        context = container.getContext();
        textView=v.findViewById(R.id.textView);
        textView.setText("Collection Centers");
        RecyclerView mRecyclerView = v.findViewById(R.id.mRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        mTeachers = new ArrayList<>();
        mAdapter = new RecyclerCollection_model(context, mTeachers);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);

        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("COLLECTION_CENTER");
        myRef=FirebaseDatabase.getInstance().getReference().child("COLLECTION_CENTER");



        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mTeachers.clear();

                for (DataSnapshot teacherSnapshot : dataSnapshot.getChildren()) {
                    Collection_data upload = teacherSnapshot.getValue(Collection_data.class);
                    Objects.requireNonNull(upload).setId(teacherSnapshot.getKey());
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

        CenterDetailsFragment fragment = new CenterDetailsFragment();
        Bundle args = new Bundle();
        args.putString("ID_KEY", data[0]);
        args.putString("NAME_KEY", data[1]);
        args.putString("MOBILE_KEY", data[2]);
        args.putString("IMAGE_KEY", data[3]);
        args.putString("ADDRESS_KEY",data[4]);
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
        Collection_data clickedTeacher=mTeachers.get(position);
        String[] teacherData={
                clickedTeacher.getId(),
                clickedTeacher.getName(),
                clickedTeacher.getMobile(),
                clickedTeacher.getUrl(),
        clickedTeacher.getAddress()};
        openDetails(teacherData);
    }
    private boolean validate(String name, String mobile,String address) {

        if (TextUtils.isEmpty(name)) {
            l1.setError("Enter Name");
            return false;
        }

        if (mobile.length() != 10) {
            l2.setError("Enter Mobile");
            return false;
        }
        if (TextUtils.isEmpty(address)) {
            l3.setError("Enter Address");
            return false;
        }


        return true;
    }
    @Override
    public void onShowItemClick(int position) {

        Collection_data user = mTeachers.get(position);
        showDeleteDialog(
                user.getId(),
                user.getName(),
                user.getMobile(),
                user.getUrl(),
                user.getAddress());


    }

    @Override
    public void onDeleteItemClick(int position) {
        Collection_data selectedItem = mTeachers.get(position);
        final String selectedKey = selectedItem.getId();
        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                new FancyAlertDialog.Builder(getActivity())
                        .setTitle("Rate us if you like the app")
                        .setMessage("Do you really want to Delete Center?")
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
                                  final String userMobile,
                                  final String userimg,
                                  final String useraddress) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder (context);
        LayoutInflater inflater = getLayoutInflater ();
        dialogView = inflater.inflate (R.layout.layout_addcollectioncenter, null);
        dialogBuilder.setView (dialogView);
        dialogBuilder.setTitle ("Center Details");
        final AlertDialog b = dialogBuilder.create ();
        b.show ();
        init_dialog_views ();
        setValues(userName,userMobile,useraddress);


        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseimage(view);
            }
        });
        btnpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(context, "An Upload is Still in Progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile(userId,userimg);

                }
            }
        });
    }
    private void uploadFile(final String id,final String img) {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            mStorageRef = FirebaseStorage.getInstance().getReference("COLLECTION_CENTER");
            ////////////////

            try {
                StorageReference delimageRef = FirebaseStorage.getInstance().getReference("COLLECTION_CENTER").getStorage().getReferenceFromUrl(img);
                delimageRef.delete();
            } catch (Exception e) {
                Log.d(TAG, "Failed to retrive values" + e);
            } finally {



                final StorageReference sRef = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(filePath));


                Bitmap bmp = null;
                try {
                    bmp = MediaStore.Images.Media.getBitmap(requireNonNull(context).getContentResolver(), filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                assert bmp != null;
                bmp.compress(Bitmap.CompressFormat.JPEG, 10, baos);
                byte[] data = baos.toByteArray();
                //uploading the image
                UploadTask uploadTask2 = sRef.putBytes(data);

                uploadTask2.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    //   sRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                progressDialog.dismiss();
                                myRef.child(id).child("imageURL").setValue(uri.toString());
                                //displaying success toast
                                Toast.makeText(context, "File Uploaded ", Toast.LENGTH_LONG).show();
                                updatedetails(id);


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
            }
        }else{
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
                img.setImageBitmap(bitmap);

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
    private void setValues(String userName,String userMobile,String useraddress) {
        tv1.setText (userName);
       // email.setText (userEmail);
        tv2.setText (userMobile);
        tv3.setText (useraddress);
    }

    private void init_dialog_views(){

        img=dialogView.findViewById(R.id.img);
        tv1=dialogView.findViewById(R.id.et_service);
        tv2=dialogView.findViewById(R.id.et_mobile);
        tv3=dialogView.findViewById(R.id.et_address);
        l1=dialogView.findViewById(R.id.tv_service);
        l2=dialogView.findViewById(R.id.tv_mobile);
        l3=dialogView.findViewById(R.id.tv_address);
        btnpost=dialogView.findViewById(R.id.btnpost);
        btnupload=dialogView.findViewById(R.id.btnupload);
        change_listner(tv1,l1);
        change_listner(tv2,l2);
        change_listner(tv3,l3);

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
    private void updatedetails(String Id) {
        //getting the specified artist reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("COLLECTION_CENTER").child(Id);
        String Name = requireNonNull(tv1.getText()).toString().trim().toUpperCase();
        String Mobile = requireNonNull(tv2.getText()).toString().trim();
        String Address= requireNonNull(tv3.getText()).toString();
        boolean ans = validate(Name, Mobile,Address);
        if (ans) {

                dR.child("name").setValue(Name);
                dR.child("mobile").setValue(Mobile);
                dR.child("address").setValue(Address);

            }


    }



}
