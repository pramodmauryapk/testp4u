package com.p4u.parvarish.Collection;


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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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


public class AddCollectioncenterFragment extends Fragment {
    private DatabaseReference databaseUsers;
    private Context context;
    private TextInputEditText tv1,tv2,tv3;
    private TextInputLayout l1,l2,l3;
    private ImageButton btnupload;
    private FirebaseStorage mStorage;
    private StorageReference mStorageRef;
    private ImageView img;
    private Button btnpost;
    private Uri filePath,imageUri;
    private StorageTask mUploadTask;
    private static final String TAG = AddCollectioncenterFragment.class.getSimpleName();

    private View v;
    public AddCollectioncenterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.layout_addcollectioncenter, container, false);
        context = container.getContext();
        databaseUsers = FirebaseDatabase.getInstance().getReference().child("COLLECTION_CENTER");
        mStorageRef = FirebaseStorage.getInstance().getReference("COLLECTION_CENTER");
        mStorage  = FirebaseStorage.getInstance().getReference("COLLECTION_CENTER").getStorage();
        context = container.getContext();
        init();
        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
        btnpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(context, "An Upload is Still in Progress", Toast.LENGTH_SHORT).show();
                } else {
                    if(filePath==null){
                        Toast.makeText(context, "Select Image", Toast.LENGTH_SHORT).show();
                    }else {

                        adddetails();

                    }



                }
            }
        });
        // Inflate the layout for this fragment
        return v;
    }

    private void init() {
        img=v.findViewById(R.id.img);
        tv1=v.findViewById(R.id.et_service);
        tv2=v.findViewById(R.id.et_mobile);
        tv3=v.findViewById(R.id.et_address);
        l1=v.findViewById(R.id.tv_service);
        l2=v.findViewById(R.id.tv_mobile);
        l3=v.findViewById(R.id.tv_address);
        btnpost=v.findViewById(R.id.btnpost);
        btnupload=v.findViewById(R.id.btnupload);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 989) {

            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                filePath = data.getData();
                try {
                    Bitmap bitmap= MediaStore.Images.Media.getBitmap(requireNonNull(getActivity()).getContentResolver(),filePath);
                    img.setImageBitmap(bitmap);
                    //Picasso.get().load(filePath).into(chosenImageView);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
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
    private void adddetails() {

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
                            String Id = databaseUsers.push().getKey();
                            //creating an Book Object
                           Collection_data row = new Collection_data (
                                   Id,
                                   requireNonNull(tv1.getText()).toString(),
                                   requireNonNull(tv2.getText()).toString(),
                                   uri.toString(),
                                   requireNonNull(tv3.getText()).toString()
                                   );

                            //Saving the Book
                            databaseUsers.child(requireNonNull(Id)).setValue(row);
                            Toast.makeText(context, "No added", Toast.LENGTH_LONG).show();
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

    private boolean validate() {

        if (TextUtils.isEmpty(tv1.getText())) {
            l1.setError("Enter Collection Center Name");

            return false;
        }
        if (TextUtils.isEmpty(tv2.getText())) {
            l2.setError("Enter Mobile");
            return false;
        }
        if (TextUtils.isEmpty(tv3.getText())) {
            l3.setError("Enter Address");
            return false;
        }

        return true;
    }

    private void set_blank(){

        tv1.setText("");
        tv2.setText("");
        img.setImageResource(R.drawable.placeholder);


    }

}
