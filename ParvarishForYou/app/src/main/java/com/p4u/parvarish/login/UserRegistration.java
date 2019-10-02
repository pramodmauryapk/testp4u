package com.p4u.parvarish.login;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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
import com.squareup.picasso.Picasso;

import java.util.Objects;

import com.p4u.parvarish.R;
import com.p4u.parvarish.WelcomeActivity;
import com.p4u.parvarish.user_pannel.Teacher;

import static java.util.Objects.requireNonNull;

public class UserRegistration extends AppCompatActivity {

    ImageView top_curve;

    TextView login_title;

    LinearLayout already_have_account_layout;
    CardView register_card;
    private static final String TAG = "CreateuserFragment";
    TextInputLayout tlname,tlpassword,tlapassword,tlemail,tlmobile,tladdress,tlidentity;
    private TextInputEditText etFullname, etPassword,etaPassword, etEmail, etMobile,etAddress,etIdentity;
    private FirebaseAuth auth;
    private Spinner spRole;
    private ProgressBar progressBar;
    private TextInputLayout tvrole;
    private DatabaseReference myref;
    private Button btnCreatenewuser;
    private ImageView imageView;
    private Uri mImageUri;
    private final int PICK_IMAGE_REQUEST = 71;
    private String email,password,aPassword,id,name,role,mobilenumber,address,identity,status;

    private StorageReference mStorageRef;
    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_registration);
        (requireNonNull(getSupportActionBar())).hide();
        initViews();
        top_curve = findViewById(R.id.top_curve);
        login_title = findViewById(R.id.registration_title);
        already_have_account_layout = findViewById(R.id.already_have_account_text);
        register_card = findViewById(R.id.register_card);


        Animation top_curve_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.top_down);
        top_curve.startAnimation(top_curve_anim);

        Animation editText_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.edittext_anim);
        etFullname.startAnimation(editText_anim);
        etPassword.startAnimation(editText_anim);
        etaPassword.startAnimation(editText_anim);
        spRole .startAnimation(editText_anim);
        etEmail.startAnimation(editText_anim);
        etMobile.startAnimation(editText_anim);
        etAddress.startAnimation(editText_anim);
        etIdentity.startAnimation(editText_anim);

        Animation field_name_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.field_name_anim);
        tlname.startAnimation(field_name_anim);
        tlemail.startAnimation(field_name_anim);
        tlidentity.startAnimation(field_name_anim);
        tladdress.startAnimation(field_name_anim);
        tlapassword.startAnimation(field_name_anim);
        tlpassword.startAnimation(field_name_anim);
        tlmobile.startAnimation(field_name_anim);
        login_title.startAnimation(field_name_anim);

        Animation center_reveal_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.center_reveal_anim);
        register_card.startAnimation(center_reveal_anim);

        Animation new_user_anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.down_top);
        already_have_account_layout.startAnimation(new_user_anim);
        tvrole.setVisibility (View.GONE);
        spRole.setVisibility (View.GONE);
        //get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("Users_Images");
        myref = FirebaseDatabase.getInstance().getReference().child("Users");

    }

    public void login(View view) {
        finish();
    }
    public void registerButton(View view) {
        if (mUploadTask != null && mUploadTask.isInProgress()) {
            Toast.makeText(this, "An Upload is Still in Progress", Toast.LENGTH_SHORT).show();
        } else {
            uploadFile();

        }
    }
    private void initViews(){
        etFullname = findViewById(R.id.et_fullname);
        etPassword =findViewById(R.id.et_createpassword);
        etaPassword= findViewById(R.id.et_confirmpassword);
        spRole = findViewById(R.id.sp_role);
        etEmail = findViewById(R.id.et_createemail);
        etMobile = findViewById(R.id.et_createmobile);
        etAddress=findViewById(R.id.et_address);
        etIdentity=findViewById(R.id.et_identity);
        tvrole=findViewById (R.id.tv_role);
        tlname=findViewById(R.id.tv_createusername);
        tlemail=findViewById(R.id.tv_createemail);
        tlmobile=findViewById(R.id.tv_createmobile);
        tlpassword=findViewById(R.id.tv_createpassword);
        tlapassword=findViewById(R.id.tv_comfirmpassword);
        tladdress=findViewById(R.id.tv_address);
        tlidentity=findViewById(R.id.tv_identity);
        progressBar = findViewById(R.id.pb);
        Button btnChoose = findViewById(R.id.btnChoose);
        imageView = findViewById(R.id.imgView);
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
        ContentResolver cR = requireNonNull(getContentResolver());
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
    }
    private Boolean validate(){
        if (TextUtils.isEmpty(name)) {
            tlname.setError("Enter full name!");

            return false;
        }

       if (TextUtils.isEmpty(mobilenumber)&&mobilenumber.length ()!=10) {
           tlmobile.setError("Enter Mobile Number!");

            return false;
        }

        if (TextUtils.isEmpty(email)) {
            tlemail.setError("Enter email address!");

            return false;
        }
        if(password.equals(aPassword)&&(TextUtils.isEmpty(password))) {
            tlapassword.setError("Both password are not same!");

            return false;

        }
        if (password.length() < 6) {
            tlpassword.setError("Password too short, enter minimum 6 characters!");

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
                                            uri.toString()
                                    );


                                    myref.child(Objects.requireNonNull(id)).setValue(upload);
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(UserRegistration.this, "User UserRegistration successful", Toast.LENGTH_LONG).show();
                                    UserRegistration.this.startActivity(new Intent(UserRegistration.this, WelcomeActivity.class));
                                    UserRegistration.this.finish();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(UserRegistration.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "You haven't Selected Any file selected", Toast.LENGTH_SHORT).show();
        }

    }

    public void chooseimage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
}
