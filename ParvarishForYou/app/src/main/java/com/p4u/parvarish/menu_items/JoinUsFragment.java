package com.p4u.parvarish.menu_items;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.p4u.parvarish.R;
import com.p4u.parvarish.login.Login_emailActivity;
import com.p4u.parvarish.login.UserRegistrationActivity;
import com.p4u.parvarish.user_pannel.Teacher;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static java.util.Objects.requireNonNull;

public class JoinUsFragment extends Fragment {

    private static final String TAG = "JoinUsFragment";
    private EditText editText;
    private TextView textView;
    private Button button,btnChoose,btnregister;
    private View v;
    private ImageView top_curve;
    private TextInputLayout tlname,tlpassword,tlapassword,tlemail,tlmobile,tladdress,tlidentity;
    private TextInputEditText etFullname, etPassword,etaPassword, etEmail, etMobile,etAddress,etIdentity;
    private FirebaseAuth auth;
    private Spinner spRole;
    private ProgressBar progressBar;
    private TextView login;
    private TextInputLayout tvrole;
    private DatabaseReference myref;
    private ImageView imageView;
    private Uri mImageUri;
    private final int PICK_IMAGE_REQUEST = 71;
    private String email,password,aPassword,id,name,role,mobilenumber,address,identity,status,feedback,news,rating,time;
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.activity_registration, container, false);

        initViews();
        top_curve.setVisibility(View.GONE);
        tvrole.setVisibility (View.GONE);
        spRole.setVisibility (View.GONE);
        //get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("USERS_IMAGES");
        myref = FirebaseDatabase.getInstance().getReference().child("USERS");

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseimage(view);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Login_emailActivity.class));
                requireNonNull(getActivity()).finish();
            }
        });
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(getContext(), "An Upload is Still in Progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile();

                }
            }
        });
        return v;
    }

    private void initViews(){
        top_curve = v.findViewById(R.id.top_curve);

        etFullname = v.findViewById(R.id.et_fullname);
        etPassword =v.findViewById(R.id.et_createpassword);
        etaPassword= v.findViewById(R.id.et_confirmpassword);
        spRole = v.findViewById(R.id.sp_role);
        etEmail =v. findViewById(R.id.et_createemail);
        etMobile = v.findViewById(R.id.et_createmobile);
        etAddress=v.findViewById(R.id.et_address);
        etIdentity=v.findViewById(R.id.et_identity);
        tvrole=v.findViewById (R.id.tv_role);
        tlname=v.findViewById(R.id.tv_createusername);
        tlemail=v.findViewById(R.id.tv_createemail);
        tlmobile=v.findViewById(R.id.tv_createmobile);
        tlpassword=v.findViewById(R.id.tv_createpassword);
        tlapassword=v.findViewById(R.id.tv_comfirmpassword);
        tladdress=v.findViewById(R.id.tv_address);
        tlidentity=v.findViewById(R.id.tv_identity);
        progressBar = v.findViewById(R.id.pb);
        btnChoose = v.findViewById(R.id.btnChoose);
        btnregister=v.findViewById(R.id.register_button);
        login=v.findViewById(R.id.login);
        imageView = v.findViewById(R.id.imgView);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            mImageUri = data.getData();
            // Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mImageUri);
            //imageView.setImageBitmap(bitmap);
            Picasso.get().load(mImageUri).into(imageView);
        }
    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = requireNonNull(requireNonNull(getActivity()).getContentResolver());
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void get_values(){
        email = requireNonNull(etEmail.getText()).toString().toLowerCase().trim();
        password = requireNonNull(etPassword.getText()).toString().trim();
        aPassword= requireNonNull(etaPassword.getText()).toString().trim();
        name = requireNonNull(etFullname.getText()).toString().toUpperCase();
        role ="USER";
        mobilenumber = requireNonNull(etMobile.getText()).toString();
        address= requireNonNull(etAddress.getText()).toString().toUpperCase();
        identity= requireNonNull(etIdentity.getText()).toString().toUpperCase();
        status="1";
        feedback="";
        news="";
        rating="";
        time=get_current_time();
    }
    private Boolean validate(){
        if (TextUtils.isEmpty(name)) {
            tlname.setError("Enter full name!");
            return false;
        }
        if (mobilenumber.length ()!=10) {
            tlmobile.setError("Enter Mobile Number!");
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            tlemail.setError("Enter email address!");
            return false;
        }
        if(!password.equals(aPassword)) {
            tlapassword.setError("Both password are not same!");
            return false;
        }
        if (TextUtils.isEmpty(address)) {
            tladdress.setError("Enter Address!");
            return false;
        }
        if (TextUtils.isEmpty(identity)) {
            tlidentity.setError("Enter aadhar or Voter id !");
            return false;
        }
        return true;
    }
    private void uploadFile() {
        get_values();
        Boolean ans=validate();
        if(ans&&mImageUri != null) {

            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setIndeterminate(true);
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    mUploadTask = fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                @Override
                                public void onSuccess(Uri uri) {
                                    //Do what you want with the url
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBar.setVisibility(View.VISIBLE);
                                            progressBar.setIndeterminate(false);
                                            progressBar.setProgress(0);
                                        }
                                    }, 500);
                                    id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                                    Teacher upload = new Teacher(
                                            id,
                                            name,
                                            email,
                                            password,
                                            role,
                                            mobilenumber,
                                            address,
                                            identity,
                                            status,
                                            feedback,
                                            news,
                                            time,
                                            rating,
                                            uri.toString()
                                    );


                                    myref.child(Objects.requireNonNull(id)).setValue(upload);
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getContext(), "Registration successful", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(getContext(), Login_emailActivity.class));
                                    requireNonNull(getActivity()).finish();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressBar.setProgress((int) progress);
                        }
                    });
                }
            });
        }
        else {
            Toast.makeText(getContext(), "You haven't Selected Any file selected", Toast.LENGTH_SHORT).show();
        }

    }

    private void chooseimage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    private String get_current_time(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss");
        return sdf.format(new Date());
    }
}
