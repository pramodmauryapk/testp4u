package com.p4u.parvarish.gallary;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.p4u.parvarish.R;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static java.util.Objects.requireNonNull;

public class AddGallaryItemFragment extends Fragment {

    private View v;
    private Context context;
    private static final String TAG = "UplaoadActivity";
    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText nameEditText;
    private EditText descriptionEditText;
    private ImageView chosenImageView;


    private Uri mImageUri;
    Intent intent;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private Spinner spimagelist;
    private StorageTask mUploadTask;
    private Button chooseImageBtn,uploadBtn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

       v = inflater.inflate(R.layout.upload_image_fragment, container, false);
        context = container.getContext();
        initViews();


        spimagelist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spimagelist.getSelectedItem().equals("UPLOADED_IMAGES")){
                    mStorageRef = FirebaseStorage.getInstance().getReference("UPLOADED_IMAGES");
                    mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("UPLOADED_IMAGES");
                }else{
                    mStorageRef = FirebaseStorage.getInstance().getReference("ADS");
                    mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("ADS");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mStorageRef = FirebaseStorage.getInstance().getReference("UPLOADED_IMAGES");
                mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("UPLOADED_IMAGES");
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
                    Toast.makeText(context, "An Upload is Still in Progress", Toast.LENGTH_SHORT).show();
                } else {
                   uploadFile();
                }
            }
        });
        return v;
    }



    private void initViews(){

        chooseImageBtn =v. findViewById(R.id.button_choose_image);
        uploadBtn = v.findViewById(R.id.uploadBtn);
        nameEditText =v.findViewById(R.id.nameEditText);
        descriptionEditText =v. findViewById ( R.id.descriptionEditText );
        chosenImageView = v.findViewById(R.id.chosenImageView);
        spimagelist=v.findViewById(R.id.spimagelist);
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
        ContentResolver cR = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (mImageUri != null) {
            final ProgressDialog progressDialog=new ProgressDialog(context);
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    progressDialog.dismiss();
                                    Toast.makeText(context, "Image Uploaded", Toast.LENGTH_LONG).show();
                                    Image_Model upload = new Image_Model(nameEditText.getText().toString().trim(),
                                            uri.toString(),
                                            descriptionEditText.getText().toString());

                                    String uploadId = mDatabaseRef.push().getKey();
                                    mDatabaseRef.child(Objects.requireNonNull(uploadId)).setValue(upload);
                                    Toast.makeText(context, "Upload successful", Toast.LENGTH_LONG).show();

                                }

                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // uploadProgressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        } else {
            Toast.makeText(context, "You haven't Selected Any file selected", Toast.LENGTH_SHORT).show();
        }

    }
    private void openImagesActivity(){
         switchFragment(new ManagegalaryFragment());

    }

    private void switchFragment(Fragment fragment) {
        if(getActivity()!=null) {
            requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .addToBackStack(null).commit();
        }
    }




}
