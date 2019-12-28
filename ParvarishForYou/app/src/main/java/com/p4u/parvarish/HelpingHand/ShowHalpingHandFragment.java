package com.p4u.parvarish.HelpingHand;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.mindorks.paracamera.Camera;
import com.p4u.parvarish.MenuPages.Page_data_Model;
import com.p4u.parvarish.R;
import com.p4u.parvarish.user_pannel.Teacher;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static java.util.Objects.requireNonNull;

public class ShowHalpingHandFragment extends Fragment implements HalpingHandRecyclerAdapter_model.OnItemClickListener {

    private static final String TAG = "ShowHalpingHandFragment";

    private Context context;
    private HalpingHandRecyclerAdapter_model mAdapter;
    private List<Page_data_Model> article_array;
    private RecyclerView mRecyclerView;
    private FirebaseStorage mStorage;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef,dR;
    private ValueEventListener mDBListener;
    private TextView inst;
    private CardView cd;

    private EditText descriptionEditText;
    private ImageView chosenImageView;
    private Uri filePath,imageUri;
    private Button positive,negative;
    public static int count = 0;
    private StorageTask mUploadTask;
    private ImageButton chooseImageBtn,img2;
    private CircleImageView uploadBtn;
    private String user_id, user_name,user_role;
    private View v,dialogView;

    private Camera camera;
    // newInstance constructor for creating fragment with arguments
    public static ShowHalpingHandFragment newInstance(int page) {
        ShowHalpingHandFragment fragment = new ShowHalpingHandFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

       v = inflater.inflate(R.layout.fragment_halpinghand_data, container, false);
        context = container.getContext();
        initViews();
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        article_array = new ArrayList<>();
        mAdapter = new HalpingHandRecyclerAdapter_model(context, article_array);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mStorageRef = FirebaseStorage.getInstance().getReference("TIMELINE");
        mStorage  = FirebaseStorage.getInstance().getReference("TIMELINE").getStorage();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("TIMELINE");
        user_name="Unknown User";
        user_id="Unknown";
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            DatabaseReference myref = FirebaseDatabase.getInstance().getReference().child("USERS");
            myref.addValueEventListener(new ValueEventListener() {

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     Log.d(TAG, "Accessing database");
                    getting_name(dataSnapshot);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                     Log.d(TAG, "failed to read values", databaseError.toException());
                }
            });

        }
        chooseImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosenImageView.setImageBitmap(null);
                chosenImageView.setVisibility(View.VISIBLE);
                openFileChooser();
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PackageManager packageManager = context.getPackageManager();
                if(!packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)){
                    Toast.makeText(getActivity(), "This device does not have a camera.", Toast.LENGTH_SHORT)
                            .show();
                }else {

                     //  takePhoto();


                }
            }
        });
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(context, "An Upload is Still in Progress", Toast.LENGTH_SHORT).show();
                } else {
                        if(descriptionEditText.getText().toString().equals("")&&filePath==null){
                            Toast.makeText(context, "Empty Message", Toast.LENGTH_SHORT).show();
                        }else {

                                uploadFile();

                        }



                }
            }
        });
        return v;
    }



    private void initViews(){

        chooseImageBtn =v. findViewById(R.id.button_choose_image);
        img2=v.findViewById(R.id.img2);
        uploadBtn = v.findViewById(R.id.uploadBtn);
        descriptionEditText = v.findViewById ( R.id.descriptionEditText );
        descriptionEditText.setText(null);
        chosenImageView = v.findViewById(R.id.chosenImageView);
        mRecyclerView =  v.findViewById(R.id.mRecyclerView);
        inst=v.findViewById(R.id.textView1);
        cd=v.findViewById(R.id.li1);

    }
    public void onStart() {

        super.onStart();
        load_list();

    }
   @SuppressLint("LongLogTag")
    private void load_list() {
        try {
            mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    article_array.clear();

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Page_data_Model article = postSnapshot.getValue(Page_data_Model.class);
                        Objects.requireNonNull(article).setKey(postSnapshot.getKey());
                        if (article.getStatus().equals("1")) {
                            article_array.add(0,article);
                        }
                    }
                    mRecyclerView.smoothScrollToPosition(0);
                    mAdapter.notifyDataSetChanged();




                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });


        }catch (Exception e){
            Log.d(TAG, "Exception in Adding List "+e);
        }

    }
    /*
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(requireNonNull(context).getContentResolver(),filePath);
                chosenImageView.setImageBitmap(bitmap);
                //Picasso.get().load(filePath).into(chosenImageView);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }*/

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

    private void uploadFile() {

        final String uploadId = mDatabaseRef.push().getKey();
        final Page_data_Model upload = new Page_data_Model();
        if(filePath!=null) {
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
                            //displaying success toast
                            //Toast.makeText(context, "Your post successfully updated. Our team will allow if content found useful.", Toast.LENGTH_LONG).show();

                            if (user_name != null && user_id != null) {
                                upload.setId(uploadId);
                                upload.setTitle(user_name);
                                upload.setImageUrl(uri.toString());
                                upload.setDescription(descriptionEditText.getText().toString());
                                upload.setTime(get_current_time());
                                upload.setStatus("0");
                                upload.setUserid(user_id);
                                upload.setCategory("General");

                            } else {
                                upload.setId(uploadId);
                                upload.setTitle("Unknown User");
                                upload.setImageUrl(uri.toString());
                                upload.setDescription(descriptionEditText.getText().toString());
                                upload.setTime(get_current_time());
                                upload.setStatus("0");
                                upload.setUserid("Unknown");
                                upload.setCategory("General");
                            }

                            mDatabaseRef.child(Objects.requireNonNull(uploadId)).setValue(upload);
                            Toast.makeText(context, "Your post successfully updated. Our team will allow if content found useful.", Toast.LENGTH_LONG).show();
                            clearall();


                        }

                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    clearall();
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
        }else if(!descriptionEditText.getText().toString().equals("")){
            if (user_name != null && user_id != null) {
                upload.setId(uploadId);
                upload.setTitle(user_name);
                upload.setImageUrl(null);
                upload.setDescription(descriptionEditText.getText().toString());
                upload.setTime(get_current_time());
                upload.setStatus("0");
                upload.setUserid(user_id);
                upload.setCategory("General");
            }else {
                upload.setId(uploadId);
                upload.setTitle("Unknown User");
                upload.setImageUrl(null);
                upload.setDescription(descriptionEditText.getText().toString());
                upload.setTime(get_current_time());
                upload.setStatus("0");
                upload.setUserid("Unknown");
                upload.setCategory("General");
            }
            mDatabaseRef.child(Objects.requireNonNull(uploadId)).setValue(upload);
            Toast.makeText(context, "Your post successfully updated. Our team will allow if content found useful.", Toast.LENGTH_LONG).show();
            clearall();
        }

    }
    private void clearall() {
        descriptionEditText.setText(null);
        descriptionEditText.requestFocus();
        chosenImageView.setImageResource(R.drawable.placeholder);
        chosenImageView.setVisibility(View.GONE);
        filePath=null;
    }

    private String get_current_time(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss");
        return sdf.format(new Date());
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void getting_name(DataSnapshot dataSnapshot) {

        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Teacher uInfo=ds.getValue(Teacher.class);
            if(requireNonNull(uInfo).getUserId().equals(requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())) {
                user_name = requireNonNull(uInfo).getUserName();
                user_id=uInfo.getUserId();
                user_role=uInfo.getUserRole();


            }

        }

    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        camera = new Camera.Builder()
                .resetToCorrectOrientation(true)
                .setTakePhotoRequestCode(1)
                .setDirectory("pics")
                .setName("ali_" + System.currentTimeMillis())
                .setImageFormat(Camera.IMAGE_JPEG)
                .setCompression(75)
                .setImageHeight(1000)
                .build(this);

    }
    private void takePhoto() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

        File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(photo));
        imageUri = Uri.fromFile(photo);
        requireNonNull(getActivity()).startActivityForResult(intent,989);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageUri;
                    requireNonNull(getActivity()).getContentResolver().notifyChange(selectedImage, null);
                    ContentResolver cr = getActivity().getContentResolver();
                    Bitmap bitmap;
                    try {
                        bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, selectedImage);

                        chosenImageView.setImageBitmap(bitmap);
                        //Toast.makeText(getActivity(), selectedImage.toString(),Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Failed to load", Toast.LENGTH_SHORT)
                                .show();

                    }
                }
                break;
            case 989:
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                    filePath = data.getData();
                    try {
                        Bitmap bitmap= MediaStore.Images.Media.getBitmap(requireNonNull(getActivity()).getContentResolver(),filePath);
                        chosenImageView.setImageBitmap(bitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }


    @Override
    public void onItemClick(int position) {
        openphoto(article_array.get(position).getImageUrl());
    }
    private void openphoto(String url){

        ImageZoomFragment fragment = new ImageZoomFragment();
        Bundle args = new Bundle();
        args.putString("PHOTO", url);

        fragment.setArguments(args);
        switchFragment(fragment);
    }

    @Override
    public void onShowItemClick(int position) {

        if(article_array.get(position)!=null) {


            Intent shareIntent=new Intent(Intent.ACTION_SEND);
            //checking message is blank or not
            if(!article_array.get(position).getDescription().equals("")) {


                //without the below line intent will show error
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT,article_array.get(position).getDescription());


            }
            // if(article_array.get(position).getImageUrl()!=null) {
            //    shareIntent.setType("image/*");
            //      url = article_array.get(position).getImageUrl();
            //      new Download_GIF(url).execute();
            //Uri imageUri = Uri.parse("file:///sdcard/DCIM/Screenshots/Screenshot_20191106-205151_Parvarish4You.jpg");
            //      shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            // shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            // }
            startActivity(Intent.createChooser(shareIntent,"share using"));

        }
    }

    @Override
    public void onDeleteItemClick(int position) {
        if(user_role.equals("ADMIN"))
        {
            Page_data_Model article = article_array.get(position);
            show_publish_dialog(article.getId(),article.getImageUrl());


        }else{
            Toast.makeText(getContext(), "Please request to ADMIN", Toast.LENGTH_SHORT).show();
        }

    }
    @SuppressLint("InflateParams")
    private void show_publish_dialog(final String id, final String url) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        final LayoutInflater inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.allowdenydialog, null);
        dialogBuilder.setView(dialogView);
        init_dialog_views();
        positive.setText(R.string.delete);
        negative.setText(R.string.cancel);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dR = FirebaseDatabase.getInstance().getReference().child("TIMELINE").child(id);
                if(url!=null) {
                    StorageReference imageRef = mStorage.getReferenceFromUrl(url);
                    imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            dR.removeValue();

                        }
                    });
                }else{
                    dR.removeValue();

                }
                Toast.makeText(getContext(), "Item deleted", Toast.LENGTH_SHORT).show();
                b.cancel();

            }
        });
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.cancel();
            }
        });
    }

    private void init_dialog_views() {
        positive=dialogView.findViewById(R.id.positiveBtn);
        negative=dialogView.findViewById(R.id.negativeBtn);
    }
    private void switchFragment(Fragment fragment) {

        requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null).commit();

    }
}
