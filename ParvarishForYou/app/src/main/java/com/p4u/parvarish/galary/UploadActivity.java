package com.p4u.parvarish.galary;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import com.p4u.parvarish.R;

import java.util.Objects;


public class UploadActivity extends AppCompatActivity {
    private static final String TAG = "UplaoadActivity";
    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText nameEditText;
    private EditText descriptionEditText;
    private ImageView chosenImageView;
    private ProgressBar uploadProgressBar;

    private Uri mImageUri;
    Intent intent;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;
 //   View v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_image_fragment);
        //v = inflater.inflate(R.layout.upload_image_fragment,container,false);


        Button chooseImageBtn = findViewById(R.id.button_choose_image);
        Button uploadBtn = findViewById(R.id.uploadBtn);
        nameEditText =findViewById(R.id.nameEditText);
        descriptionEditText = findViewById ( R.id.descriptionEditText );
        chosenImageView = findViewById(R.id.chosenImageView);
        uploadProgressBar =findViewById(R.id.progress_bar);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploaded_image");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("uploaded_image");

        chooseImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadActivity.this.openFileChooser();
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(UploadActivity.this, "An Upload is Still in Progress", Toast.LENGTH_SHORT).show();
                } else {
                    UploadActivity.this.uploadFile();
                }
            }
        });
        //return v;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            //chosenImageView.setImageURI(mImageUri);

            Picasso.get().load(mImageUri).into(chosenImageView);

        }
    }


    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (mImageUri != null) {
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            uploadProgressBar.setVisibility(View.VISIBLE);
            uploadProgressBar.setIndeterminate(true);

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            uploadProgressBar.setVisibility(View.VISIBLE);
                                            uploadProgressBar.setIndeterminate(false);
                                            uploadProgressBar.setProgress(0);
                                        }
                                    }, 500);

                                    Image_Model upload = new Image_Model(nameEditText.getText().toString().trim(),
                                            uri.toString(),
                                            descriptionEditText.getText().toString());
                                    Toast.makeText(UploadActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
                                    String uploadId = mDatabaseRef.push().getKey();
                                    mDatabaseRef.child(Objects.requireNonNull(uploadId)).setValue(upload);

                                    uploadProgressBar.setVisibility(View.INVISIBLE);
                                    //openImagesActivity();
                                }

                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            uploadProgressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(UploadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            uploadProgressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "You haven't Selected Any file selected", Toast.LENGTH_SHORT).show();
        }

    }
    private void openImagesActivity(){
       // switchFragment(new ManageGalleryFragment());
        startActivity(new Intent(this, ManageGalleryFragment.class));
        finish();
    }

}
