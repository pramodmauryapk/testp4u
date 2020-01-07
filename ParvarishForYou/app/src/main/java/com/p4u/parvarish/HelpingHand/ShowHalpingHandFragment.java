package com.p4u.parvarish.HelpingHand;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.p4u.parvarish.MenuPages.Page_data_Model;
import com.p4u.parvarish.R;
import com.p4u.parvarish.user_pannel.Teacher;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.p4u.parvarish.HelpingHand.Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;
import static java.util.Objects.requireNonNull;

public class ShowHalpingHandFragment extends Fragment implements HalpingHandRecyclerAdapter_model.OnItemClickListener {

    private static final String TAG = ShowHalpingHandFragment.class.getSimpleName();
    private static final int REQUEST_PERMISSION = 10;


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
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
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
    private String userChoosenTask;

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
                    chosenImageView.setImageBitmap(null);
                    chosenImageView.setVisibility(View.VISIBLE);
                   selectImage();


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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;

        }
    }
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(getActivity());

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void galleryIntent(){
      Intent intent = new Intent();
      intent.setType("image/*");
      intent.setAction(Intent.ACTION_GET_CONTENT);//
      startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
  }
    private void cameraIntent(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }
    private void onCaptureImageResult(Intent data) {
       Bitmap thumbnail = (Bitmap) requireNonNull(data.getExtras()).get("data");
       chosenImageView.setImageBitmap(thumbnail);
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".jpg";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(requireNonNull(getActivity()), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
            return;
        }
        saveToSDCard(thumbnail,fname);
    }
    private void saveToSDCard(Bitmap bitmap, String name) {
        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mExternalStorageAvailable = mExternalStorageWriteable = true;
            Log.v(TAG, "SD Card is available for read and write "
                    + mExternalStorageAvailable + mExternalStorageWriteable);
            saveFile(bitmap, name);
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
            Log.v(TAG, "SD Card is available for read "
                    + mExternalStorageAvailable);
        } else {
            mExternalStorageAvailable = mExternalStorageWriteable = false;
            Log.v(TAG, "Please insert a SD Card to save your Ad "
                    + mExternalStorageAvailable + mExternalStorageWriteable);
        }
    }
    private void saveFile(Bitmap bitmap, String name) {

        String filename = name;
        ContentValues values = new ContentValues();
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File sdImageMainDirectory = new File(root + "/saved_images");
        sdImageMainDirectory.mkdirs();

        File outputFile = new File(sdImageMainDirectory, filename);
        values.put(MediaStore.MediaColumns.DATA, outputFile.toString());
        values.put(MediaStore.MediaColumns.TITLE, filename);
        values.put(MediaStore.MediaColumns.DATE_ADDED, System.currentTimeMillis());
        values.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
        Uri uri = requireNonNull(getActivity()).getContentResolver().insert(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        if (outputFile.exists())
            outputFile.delete();
        try {
            OutputStream outStream = getActivity().getContentResolver().openOutputStream(uri);
            bitmap.compress(Bitmap.CompressFormat.PNG, 95, outStream);
            outStream.flush();
            outStream.close();
            // this is where im having the problem
            // Tell the media scanner about the new file so that it is
            // immediately available to the user.
            MediaScannerConnection.scanFile(getActivity(),new String[] { outputFile.toString() }, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.v("ExternalStorage", "Scanned " + path + ":");
                            Log.v("ExternalStorage", "-> uri=" + uri);
                            filePath=uri;
                        }
                    });
        } catch (IOException e) {
            // Unable to create file, likely because external storage is
            // not currently mounted.
            Log.v("ExternalStorage", "Error writing " + outputFile, e);
        }
    }
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(requireNonNull(getActivity()).getContentResolver(), data.getData());
                filePath=data.getData();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        chosenImageView.setImageBitmap(bm);
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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
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
