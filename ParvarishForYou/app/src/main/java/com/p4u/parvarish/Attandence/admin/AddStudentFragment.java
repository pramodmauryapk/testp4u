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
import android.widget.AdapterView;
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


public class AddStudentFragment extends Fragment {
    private TextInputEditText stuname,stufather,stumother,stuhomemobile,stuadmissionno,studob,stufeepaid,stufeepending,stuaddress,stupin;
    private Spinner stuclass,stusection,stuyear,stugender;
    private View v;
    private String UserID;
    private Context context;
    private FirebaseStorage mStorage;
    private StorageReference mStorageRef;
    private DatabaseReference studentref,princiref;
    private TextInputLayout tf1,tf2,tf3,tf4,tf5,tf6,tf7,tf8,tf9,tf10;
    private Button btnAddstudent,btnclear;
    private ImageView logo;
    private ImageButton selectimg;
    private Uri filePath,imageUri;
    private StorageTask mUploadTask;
    private String schoolname,schooId,stclass,stsection,role;
    private Bundle bundle;
    private int mYear,mMonth,mDay,count;
    public AddStudentFragment() {

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_addstudent, container, false);
        // Inflate the layout for this fragment
        init();
        bundle=new Bundle();
        schoolname = requireNonNull(this.getArguments()).getString("SCHOOL_NAME");
        role = requireNonNull(this.getArguments()).getString("ROLE");
        UserID = requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        stuclass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                stclass=stuclass.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                stclass=stuclass.getItemAtPosition(0).toString();
            }

        });
        stusection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                stsection=stusection.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                stsection=stusection.getItemAtPosition(0).toString();
            }

        });
        princiref = FirebaseDatabase.getInstance().getReference().child("SCHOOL").child(schoolname).child("PRINCI");
        mStorageRef = FirebaseStorage.getInstance().getReference("STUDENTS");
        mStorage  = FirebaseStorage.getInstance().getReference("STUDENTS").getStorage();
        context = container.getContext();


        get_count();
        selectimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
        btnAddstudent.setOnClickListener(new View.OnClickListener() {
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
        studob.setOnClickListener(new View.OnClickListener() {
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

                                studob.setText(String.format("%d/%d/%d", dayOfMonth, monthOfYear + 1, year));

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        change_listner(stuname,tf1);
        change_listner(stufather,tf2);
        change_listner(stumother,tf3);
        change_listner(stuhomemobile,tf4);
        change_listner(stuadmissionno,tf5);
        change_listner(studob,tf6);
        change_listner(stufeepaid,tf7);
        change_listner(stufeepending,tf8);
        change_listner(stuaddress,tf9);
        change_listner(stupin,tf10);
        return v;
    }

    private void init() {
        stuname=v.findViewById(R.id.etStuName);
        stufather=v.findViewById(R.id.etStufather);
        stumother=v.findViewById(R.id.etMotherName);
        stuhomemobile=v.findViewById(R.id.etHomeMobile);
        stuadmissionno=v.findViewById(R.id.etAdmissionNo);
        studob=v.findViewById(R.id.etDOB);
        stufeepaid=v.findViewById(R.id.etFeePaid);
        stufeepending=v.findViewById(R.id.etFeePending);
        stuaddress=v.findViewById(R.id.etstuAddress);
        stupin=v.findViewById(R.id.etpin);
        stuclass=v.findViewById(R.id.spclass);
        stusection=v.findViewById(R.id.spsection);
        stuyear=v.findViewById(R.id.spYear);
        stugender=v.findViewById(R.id.spgender);
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
        btnAddstudent = v.findViewById(R.id.btnAddStudent);
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
                            studentref = FirebaseDatabase.getInstance().getReference()
                                    .child("SCHOOL")
                                    .child(schoolname)
                                    .child("STUDENTS")
                                    .child("PERSONAL_DATA")
                                    .child(stclass)
                                    .child(stsection);
                            //getting a unique id using push().getKey() method
                            //it will create a unique id and we will use it as the Primary Key for our Book
                            String studentId = studentref.push().getKey();
                            //creating an Book Object
                            assert studentId != null;
                            StudentData student = new StudentData(
                                    requireNonNull(stuadmissionno.getText()).toString().toUpperCase(),
                                    requireNonNull(stuname.getText()).toString().toUpperCase(),
                                    requireNonNull(stufather.getText()).toString().toUpperCase(),
                                    requireNonNull(studob.getText()).toString().trim(),
                                    requireNonNull(stumother.getText()).toString().toUpperCase(),
                                    requireNonNull(stuhomemobile.getText()).toString().trim(),
                                    stuyear.getSelectedItem().toString(),
                                    requireNonNull(stuaddress.getText()).toString().toUpperCase(),
                                    stugender.getSelectedItem().toString(),
                                    requireNonNull(stufeepaid.getText()).toString().trim(),
                                    requireNonNull(stufeepending.getText()).toString().trim(),
                                    uri.toString(),
                                    requireNonNull(stupin.getText()).toString().trim(),
                                    "",
                                    "STUDENT",
                                    studentId.substring(0, 5),false);

                            //Saving the Book
                            studentref.child(stuadmissionno.getText().toString().toUpperCase()).setValue(student);
                            Toast.makeText(context, "Student added", Toast.LENGTH_LONG).show();
                            princiref.child("schoolStudents").setValue(count+1);
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
        try {
            princiref.child("schoolStudents").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    count = Integer.parseInt(String.valueOf(dataSnapshot.getValue()));


                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {

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

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

            }
        }
    }
    private boolean validate() {

        if (TextUtils.isEmpty(stuname.getText())) {
            tf1.setError("Enter Student Name");

            return false;
        }
        if (TextUtils.isEmpty(stufather.getText())) {
            tf2.setError("Enter Father Name");
            return false;
        }
        if (TextUtils.isEmpty(stumother.getText())) {
            tf3.setError("Enter Mother Name");
            return false;
        }
        if (stuhomemobile.length() != 10) {
            tf4.setError("Enter Home Mobile");
            return false;
        }

        if (TextUtils.isEmpty(stuadmissionno.getText())) {
            tf5.setError("Enter Admission No");
            return false;
        }
        if (TextUtils.isEmpty(studob.getText())) {
            tf6.setError("Enter DOB");
            return false;
        }
        if (TextUtils.isEmpty(stufeepaid.getText())) {
            tf7.setError("Enter Fee Paid");
            return false;
        }
        if (TextUtils.isEmpty(stufeepending.getText())) {
            tf8.setError("Enter Fee Pending");
            return false;
        }
        if (TextUtils.isEmpty(stuaddress.getText())) {
            tf9.setError("Enter Address");
            return false;
        }
        if (TextUtils.isEmpty(stupin.getText())) {
            tf10.setError("Enter PIN");
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

        stuname.setText("");
        stufather.setText("");
        stumother.setText("");
        stuhomemobile.setText("");
        stuadmissionno.setText("");
        studob.setText("");
        stufeepaid.setText("");
        stufeepending.setText("");
        stuaddress.setText("");
        stupin.setText("");
        logo.setImageBitmap(null);


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
