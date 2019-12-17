package com.p4u.parvarish.Attandence.admin;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.p4u.parvarish.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;
import static java.util.Objects.requireNonNull;


public class AddTeacherFragment extends Fragment {
    private TextInputEditText tname,tfather,tmother,tmobile,tempcode,tdob,tload,tsalary,taddress,tpin;
    private Spinner tclass,tsection,tyear,tgender;
    private View v;
    private String UserID;
    private Context context;
    private FirebaseStorage mStorage;
    private StorageReference mStorageRef;
    private DatabaseReference techerref,princiref;
    private TextInputLayout tf1,tf2,tf3,tf4,tf5,tf6,tf7,tf8,tf9,tf10;
    private Button btnAddteacher,btnclear;
    private ImageView logo;
    private ImageButton selectimg;
    private Uri filePath,imageUri;
    private StorageTask mUploadTask;
    private String schoolname,schooId;
    private Bundle bundle;
    private int mYear,mMonth,mDay,count;
    public AddTeacherFragment() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_addteacher, container, false);
        // Inflate the layout for this fragment
        bundle=new Bundle();
        schoolname = requireNonNull(this.getArguments()).getString("SCHOOL_NAME");
        //schoolname="RD public school";
        UserID = requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        techerref = FirebaseDatabase.getInstance().getReference().child("SCHOOL").child("TEACHERS");
        princiref = FirebaseDatabase.getInstance().getReference().child("SCHOOL").child("PRINCI");
        mStorageRef = FirebaseStorage.getInstance().getReference("TEACHERS");
        mStorage  = FirebaseStorage.getInstance().getReference("TEACHERS").getStorage();
        context = container.getContext();
        init();

        get_count();
        selectimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
        btnAddteacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(context, "An Upload is Still in Progress", Toast.LENGTH_SHORT).show();
                } else {
                    if(filePath==null){
                        Toast.makeText(context, "Select Logo", Toast.LENGTH_SHORT).show();
                    }else {

                        addStudent();

                    }



                }
            }
        });
        btnclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_blank();
            }
        });
        tdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @SuppressLint("DefaultLocale")
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                tdob.setText(String.format("%d/%d/%d", dayOfMonth, monthOfYear + 1, year));

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        change_listner(tname,tf1);
        change_listner(tfather,tf2);
        change_listner(tmother,tf3);
        change_listner(tmobile,tf4);
        change_listner(tempcode,tf5);
        change_listner(tdob,tf6);
        change_listner(tload,tf7);
        change_listner(tsalary,tf8);
        change_listner(taddress,tf9);
        change_listner(tpin,tf10);
        return v;
    }

    private void init() {
        tname=v.findViewById(R.id.etStuName);
        tfather=v.findViewById(R.id.etStufather);
        tmother=v.findViewById(R.id.etMotherName);
        tmobile=v.findViewById(R.id.etHomeMobile);
        tempcode=v.findViewById(R.id.etAdmissionNo);
        tdob=v.findViewById(R.id.etDOB);
        tload=v.findViewById(R.id.etFeePaid);
        tsalary=v.findViewById(R.id.etFeePending);
        taddress=v.findViewById(R.id.etstuAddress);
        tpin=v.findViewById(R.id.etpin);
        tclass=v.findViewById(R.id.spclass);
        tsection=v.findViewById(R.id.spsection);
        tyear=v.findViewById(R.id.spYear);
        tgender=v.findViewById(R.id.spgender);
        tf1=v.findViewById(R.id.tf1);
        tf2=v.findViewById(R.id.tf2);
        tf3=v.findViewById(R.id.tf3);
        tf4=v.findViewById(R.id.tf4);
        tf5=v.findViewById(R.id.tf5);
        tf6=v.findViewById(R.id.tf6);
        tf7=v.findViewById(R.id.tf7);
        tf8=v.findViewById(R.id.tf8);
        tf9=v.findViewById(R.id.tf9);
        tf10=v.findViewById(R.id.tf10);
        btnAddteacher = v.findViewById(R.id.btnAddStudent);
        btnclear=v.findViewById(R.id.btnclear);
        selectimg=v.findViewById(R.id.selectimg);
        logo=v.findViewById(R.id.logo);
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

    private void addStudent() {

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
                            String teacherId = techerref.push().getKey();
                            //creating an Book Object
                            assert teacherId != null;
                            TeacherData teacher = new TeacherData(
                                    teacherId,
                                    requireNonNull(tname.getText()).toString(),
                                    requireNonNull(tempcode.getText()).toString(),
                                    requireNonNull(tfather.getText()).toString(),
                                    requireNonNull(tdob.getText()).toString(),
                                    requireNonNull(tmother.getText()).toString(),
                                    requireNonNull(tmobile.getText()).toString(),
                                    tclass.getSelectedItem().toString(),
                                    tsection.getSelectedItem().toString(),
                                    tyear.getSelectedItem().toString(),
                                    requireNonNull(taddress.getText()).toString(),
                                    tgender.getSelectedItem().toString(),
                                    requireNonNull(tload.getText()).toString(),
                                    requireNonNull(tsalary.getText()).toString(),
                                    uri.toString(),
                                    requireNonNull(tpin.getText()).toString(),
                                    "",
                                    "TEACHER",
                                    schoolname,
                                    teacherId.substring(0, 5));

                            //Saving the Book
                            techerref.child(requireNonNull(teacherId)).setValue(teacher);


                            Toast.makeText(context, "Teacher added", Toast.LENGTH_LONG).show();
                            princiref.child(schooId).child("schoolTeachers").setValue(count+1);
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

    private void get_count() {

        princiref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    SchoolData school = ds.getValue(SchoolData.class);
                    assert school != null;
                    if(school.getSchoolName().equals(schoolname)) {
                        count = school.getSchoolTeachers();
                        schooId=school.getSchoolId();


                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



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

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

            }
        }
    }
    private boolean validate() {

        if (TextUtils.isEmpty(tname.getText())) {
            tf1.setError("Name");

            return false;
        }
        if (TextUtils.isEmpty(tfather.getText())) {
            tf2.setError("Father Name");
            return false;
        }
        if (TextUtils.isEmpty(tmother.getText())) {
            tf3.setError("Mother Name");
            return false;
        }
        if (tmobile.length() != 10) {
            tf4.setError("Mobile No.");
            return false;
        }

        if (TextUtils.isEmpty(tempcode.getText())) {
            tf5.setError("EmployeeNo");
            return false;
        }
        if (TextUtils.isEmpty(tdob.getText())) {
            tf6.setError("Enter DOB");
            return false;
        }
        if (TextUtils.isEmpty(tload.getText())) {
            tf7.setError("Load/Hours");
            return false;
        }
        if (TextUtils.isEmpty(tsalary.getText())) {
            tf8.setError("Salary");
            return false;
        }
        if (TextUtils.isEmpty(taddress.getText())) {
            tf9.setError("Address");
            return false;
        }
        if (TextUtils.isEmpty(tpin.getText())) {
            tf10.setError("PIN");
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

        tname.setText("");
        tfather.setText("");
        tmother.setText("");
        tmobile.setText("");
        tempcode.setText("");
        tdob.setText("");
        tload.setText("");
        tsalary.setText("");
        taddress.setText("");
        tpin.setText("");



    }

    // switching fragment
    private void switchFragment(Fragment fragment) {
        bundle.putString("SCHOOL_NAME",schoolname);
        fragment.setArguments(bundle);
        if (getActivity() != null) {
            requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .addToBackStack(null).commit();
        }
    }


}
