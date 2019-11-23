package com.p4u.parvarish.user_pannel;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static java.util.Objects.requireNonNull;

public class UserProfileFragment extends Fragment {
    private static final String TAG = "UserProfileFragment";
    private String userID;
    private String userPassword,userAgainPassword,userRole,userAddress,userStatus,userFeedback,userNews,userTime,userRating,userImg;
    private View v;
    private EditText username;
    private EditText email;
    private EditText mobile;
    private TextInputLayout l1;
    private TextInputLayout l2;
    private TextInputLayout l3;
    private TextInputLayout l4;
    private TextView location,role;
    private Button save;
    private Button change,choose,upload;
    private Spinner sp;
    private String userrole,id;
    private Context context;
    private ImageView imageView;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;
    private DatabaseReference myRef;
    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate (R.layout.layout_profile, container, false);
        context = container.getContext();
        initViews ();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        //Declare database reference
        myRef = mFirebaseDatabase.getReference().child("USERS");

        final FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null) {
            userID = (requireNonNull(user)).getUid();

            change_listner(username, l1);
            change_listner(email, l2);
            change_listner(mobile, l3);
            change_listner(location, l4);

        myRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "Accessing database");
                show(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "failed to read values", databaseError.toException());
            }
        });
        save.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View view) {   updatedetails(userID,userPassword,userImg);           }        });
        change.setOnClickListener (new View.OnClickListener() {
           @Override
           public void onClick(View view) { switchFragment(new UpdatePasswordFragment());           }       });
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseimage(view);
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { if (mUploadTask != null && mUploadTask.isInProgress()) {
                     Toast.makeText(context, "An Upload is Still in Progress", Toast.LENGTH_SHORT).show();
                } else {
                        uploadFile();

                    }
                }
            });
        }else {
            Toast.makeText(context, "Please Register then update", Toast.LENGTH_SHORT).show();
        }
        return v;
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

        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null).commit();
    }
        private void initViews(){
        username=v.findViewById(R.id.etUserName);
        email=v.findViewById(R.id.etEmailID);
        mobile=v.findViewById(R.id.etMobile);
        location=v.findViewById(R.id.tvCenterName);
        role=v.findViewById(R.id.tvRole);
        sp=v.findViewById(R.id.sprole);
        imageView=v.findViewById(R.id.imgView);
        change=v.findViewById(R.id.dbuttonchange);
        save=  v.findViewById(R.id.dbuttonsave);
        choose=  v.findViewById(R.id.btnChoose);
        upload=  v.findViewById(R.id.upload_button);
        l1=v.findViewById(R.id.l1);
        l2=v.findViewById(R.id.l2);
        l3=v.findViewById(R.id.l3);
        l4=v.findViewById(R.id.l4);
    }
        private void uploadFile() {

        if (filePath != null) {
            final ProgressDialog progressDialog=new ProgressDialog(context);
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            mStorageRef = FirebaseStorage.getInstance().getReference("USERS_IMAGES");
         ////////////////user already contains image then delete it from storage

            StorageReference delimageRef = FirebaseStorage.getInstance().getReference("USERS_IMAGES").getStorage().getReferenceFromUrl(userImg);
            delimageRef.delete();
///////////////////////////////
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

            uploadTask2.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
          /*  sRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {*/
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
    private void show(DataSnapshot dataSnapshot){
        try {
            for (DataSnapshot ds : dataSnapshot.getChildren ()) {
                    Teacher uInfo=ds.getValue(Teacher.class);
                    if(requireNonNull(uInfo).getUserId().equals(userID)) {
                        id=uInfo.getUserId();
                        username.setText(requireNonNull(uInfo).getUserName());
                        email.setText(uInfo.getUserEmail());
                        mobile.setText(uInfo.getUserMobile());
                        location.setText(uInfo.getUserAddress());
                        userrole=uInfo.getUserRole();
                        if(userrole.equals("ADMIN")){
                            role.setVisibility(View.GONE);
                            sp.setVisibility(View.VISIBLE);
                        }
                        else {
                            role.setText(uInfo.getUserRole());
                            role.setEnabled(false);
                        }

                        userImg = uInfo.getImageURL();
                    }
            }
        }catch (Exception e){
            Log.d(TAG, "Failed to retrive values"+e);
        }
    }
    private void updatedetails(String ID,String Password,String Img) {

        String Name = username.getText().toString().trim().toUpperCase();
        String Email = email.getText().toString().trim().toLowerCase();
        String Mobile = mobile.getText().toString().trim();
        String Identity = "";
        String Role;
        if(userrole.equals("ADMIN")){
            Role =sp.getSelectedItem().toString().toUpperCase();
        }else {
            Role = role.getText().toString().toUpperCase();
        }
        String Address = location.getText().toString().toUpperCase();
        String Status = "1";
        String Feedback = "";
        String News = "";
        String Rating = "";
        String Time = get_current_time();
        //updating artist
        boolean ans=validate(Name, Email, Mobile, Address);
        if(ans) {
            //getting the specified artist reference
            DatabaseReference dR = FirebaseDatabase.getInstance().getReference().child("USERS").child(ID);
            Teacher user = new Teacher(
                    ID,
                    Name,
                    Email,
                    Password,
                    Role,
                    Mobile,
                    Address,
                    Identity,
                    Status,
                    Feedback,
                    News,
                    Time,
                    Rating,
                    Img);
            dR.setValue(user);
            Toast.makeText(context, "User info Updated", Toast.LENGTH_LONG).show();
        }
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
    private String get_current_time(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss");
        return sdf.format(new Date());
    }
    
}