package com.p4u.parvarish.menu_data;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.p4u.parvarish.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


public class upload_article extends AppCompatActivity {
    private static final String TAG = "Uplaoad_article";
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText nameEditText;
    private EditText descriptionEditText;
    private ImageView chosenImageView;
    private Uri filePath;
    private StorageReference mStorageRef ;
    private DatabaseReference mDatabaseRef;
    private Spinner sppagelist;
    private StorageTask mUploadTask;
    private Button uploadBtn,chooseImageBtn;
    String s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_page_fragment);
        init();
        sppagelist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(sppagelist.getSelectedItem().equals("TELENT SUPPORT")){
                    s="TELENT_SUPPORT";
                }else if(sppagelist.getSelectedItem().equals("TECHNICAL PARTNERSHIP")){
                    s="TECHNICAL_PARTNERSHIP";
                }else if(sppagelist.getSelectedItem().equals("CAPACITY BUILDING")){
                    s="CAPACITY_BUILDING";
                }else if(sppagelist.getSelectedItem().equals("INTERNSHIP PROGRAM")){
                    s="INTERNSHIP_PROGRAM";
                }else if(sppagelist.getSelectedItem().equals("ACADEMIC PARTNER")){
                    s="ACADEMIC_PARTNER";
                }
                mStorageRef  = FirebaseStorage.getInstance().getReference(s);
                mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("PAGES").child(s);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mStorageRef  = FirebaseStorage.getInstance().getReference("TELENT_SUPPORT");
                mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("PAGES").child("TELENT_SUPPORT");
            }
        });


        chooseImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openFileChooser();
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(upload_article.this, "An Upload is Still in Progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();
                }
            }
        });



    }

    private void init(){
        chooseImageBtn = findViewById(R.id.button_choose_image);
        uploadBtn = findViewById(R.id.uploadBtn);

        descriptionEditText = findViewById ( R.id.descriptionEditText );
        chosenImageView = findViewById(R.id.chosenImageView);
        nameEditText =findViewById(R.id.nameEditText);
        sppagelist=findViewById(R.id.sppagelist);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                chosenImageView.setImageBitmap(bitmap);
                //Picasso.get().load(filePath).into(chosenImageView);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }


    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_REQUEST);

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (filePath != null) {
           final ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            final StorageReference sRef = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(filePath));

            sRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            progressDialog.dismiss();
                            //displaying success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                            String uploadId = mDatabaseRef.push().getKey();
                            Article_Model upload = new Article_Model(
                                    uploadId,
                                    nameEditText.getText().toString().trim(),
                                    uri.toString(),
                                    descriptionEditText.getText().toString(),
                                    get_current_time(),
                                    "1",
                                    Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()
                            );

                            mDatabaseRef.child(Objects.requireNonNull(uploadId)).setValue(upload);
                            Toast.makeText(upload_article.this, "Upload successful", Toast.LENGTH_LONG).show();


                        }

                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                        //uploadProgressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(upload_article.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
             @Override
             public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
             //displaying the upload progress
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                }
             });
        } else{
            String uploadId = mDatabaseRef.push().getKey();
            Article_Model upload = new Article_Model(
                    uploadId,
                    nameEditText.getText().toString().trim(),
                    null,
                    descriptionEditText.getText().toString(),
                    get_current_time(),
                    "1",
                    Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()
            );

            mDatabaseRef.child(Objects.requireNonNull(uploadId)).setValue(upload);
            Toast.makeText(upload_article.this, "Details saved", Toast.LENGTH_LONG).show();
        }
    }

    private String get_current_time(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss");
        return sdf.format(new Date());
    }

}
