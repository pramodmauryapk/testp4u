package com.p4u.parvarish.Attandence.admin;


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.p4u.parvarish.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;
import static java.util.Objects.requireNonNull;


public class AddSchoolFragment extends Fragment {
    private TextInputEditText schname,schadd,schprinciname,schprinciphone;
    private Spinner schmedium,schstbyear;
    private View v;
    private String UserID;
    private Context context;
    private FirebaseStorage mStorage;
    private StorageReference mStorageRef;
    private DatabaseReference Princi;
    private TextInputLayout tf1,tf2,tf3,tf4,tf5,tf6,tf7;
    private Button btnAddSchool,btnlistSchool;
    private ImageView logo;
    private ImageButton selectimg;
    private Uri filePath,imageUri;
    private StorageTask mUploadTask;
    public AddSchoolFragment() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_addschool, container, false);
        // Inflate the layout for this fragment
        UserID = requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        Princi = FirebaseDatabase.getInstance().getReference().child("SCHOOL").child("PRINCI");
        mStorageRef = FirebaseStorage.getInstance().getReference("PRINCI");
        mStorage  = FirebaseStorage.getInstance().getReference("PRINCI").getStorage();
        context = container.getContext();
        init();
        selectimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
        btnAddSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(context, "An Upload is Still in Progress", Toast.LENGTH_SHORT).show();
                } else {
                    if(filePath==null){
                        Toast.makeText(context, "Select Logo", Toast.LENGTH_SHORT).show();
                    }else {

                        addSchool();

                    }



                }
            }
        });
        btnlistSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFragment(new SchoolListFragment());
            }
        });

        return v;
    }

    private void init() {
        schname=v.findViewById(R.id.etSchName);
        schadd=v.findViewById(R.id.etSchAdd);
        schprinciname=v.findViewById(R.id.etPrinciple);
        schprinciphone=v.findViewById(R.id.etPrinciMobile);
        schmedium=v.findViewById(R.id.spMedium);
        schstbyear=v.findViewById(R.id.spYear);
        tf2=v.findViewById(R.id.tf2);
        tf3=v.findViewById(R.id.tf3);
        tf6=v.findViewById(R.id.tf6);
        tf7=v.findViewById(R.id.tf7);
        btnAddSchool = v.findViewById(R.id.btnAddSchool);
        btnlistSchool=v.findViewById(R.id.btnlistschools);
        selectimg=v.findViewById(R.id.selectimg);
        logo=v.findViewById(R.id.logo);
    }

    private void addSchool() {

        //validating
        boolean ans=validate();
        if(ans && filePath!=null) {
            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            final StorageReference sRef = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(filePath));

            Bitmap bmp = null;
            try {
                bmp = MediaStore.Images.Media.getBitmap(requireNonNull(context).getContentResolver(), filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            assert bmp != null;
            bmp.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();
            //uploading the image
            UploadTask uploadTask2 = sRef.putBytes(data);

            uploadTask2.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            progressDialog.dismiss();

                            //getting a unique id using push().getKey() method
                            //it will create a unique id and we will use it as the Primary Key for our Book
                            String schoolId = Princi.push().getKey();
                            //creating an Book Object
                            SchoolData school = new SchoolData(
                                    schoolId,
                                    schname.getText().toString(),
                                    schadd.getText().toString(),
                                    schmedium.getSelectedItem().toString(),
                                    schstbyear.getSelectedItem().toString(),
                                    0,
                                    0,
                                    schprinciname.getText().toString(),
                                    schprinciphone.getText().toString(),
                                    uri.toString(),
                                    "PRINCI",
                                    schoolId.substring(0, 5));

                            //Saving the Book
                            Princi.child(requireNonNull(schoolId)).setValue(school);
                            Toast.makeText(context, "School added", Toast.LENGTH_LONG).show();
                            //setting edittext to blank again
                            set_blank();

                        }

                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    set_blank();
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

        else {
            Toast.makeText(context, "fill All required fields ", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 989) {

                if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                    filePath = data.getData();
                    try {
                        Bitmap bitmap= MediaStore.Images.Media.getBitmap(requireNonNull(getActivity()).getContentResolver(),filePath);
                        logo.setImageBitmap(bitmap);
                        //Picasso.get().load(filePath).into(chosenImageView);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

            }
        }
    }
    private boolean validate() {

        if (TextUtils.isEmpty(schname.getText())) {
            tf2.setError("Enter School Name");

            return false;
        }
        if (TextUtils.isEmpty(schadd.getText())) {
            tf3.setError("Enter School Address");
            return false;
        }
        if (TextUtils.isEmpty(schprinciname.getText())) {
            tf4.setError("Enter Princi Name");
            return false;
        }

        if (schprinciphone.length() != 10) {
            tf7.setError("Enter Princi Mobile");
            return false;
        }

        return true;
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), 989);

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = requireNonNull(context).getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void set_blank(){

        schname.setText("");
        schadd.setText("");
        schprinciphone.setText("");
        schprinciname.setText("");
        //schteachercount.setText("");

    }

    // switching fragment
    private void switchFragment(Fragment fragment) {
        if (getActivity() != null) {
            requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .addToBackStack(null).commit();
        }
    }


}
