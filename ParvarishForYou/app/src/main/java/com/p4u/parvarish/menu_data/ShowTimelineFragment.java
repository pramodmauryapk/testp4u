package com.p4u.parvarish.menu_data;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.ListView;
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
import com.p4u.parvarish.R;
import com.p4u.parvarish.user_pannel.Teacher;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static java.util.Objects.requireNonNull;

public class ShowTimelineFragment extends Fragment {

    private static final String TAG = "ShowTimelineFragment";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Context context;
    private TimelineRecyclerAdapter_model mAdapter;
    private List<Article_Model> article_array;
    private DatabaseReference myRef;
    private RecyclerView mRecyclerView;
    private FirebaseStorage mStorage;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private View dialogView;
    private Button positive,negative;
    private TextView inst;
    private CardView cd;

    private ListView listViewArticles;
    private String user_role;
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText descriptionEditText;
    private ImageView chosenImageView;
    private Uri filePath;

    private StorageTask mUploadTask;
    private ImageButton chooseImageBtn,img2;
    private CircleImageView uploadBtn;
    String user_name,user_id;
    private Bitmap mImageBitmap;
    private String imageFilePath;
    private View v;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

       v = inflater.inflate(R.layout.fragment_timeline_data, container, false);
        context = container.getContext();
        initViews();
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        article_array = new ArrayList<>();

        mAdapter = new TimelineRecyclerAdapter_model(context, article_array);
        mRecyclerView.setAdapter(mAdapter);
       // mAdapter.setOnItemClickListener(this);
        mStorageRef = FirebaseStorage.getInstance().getReference("TIMELINE");
        mStorage  = FirebaseStorage.getInstance().getReference("TIMELINE").getStorage();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("TIMELINE");

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
                        Article_Model article = postSnapshot.getValue(Article_Model.class);
                        Objects.requireNonNull(article).setKey(postSnapshot.getKey());
                        if (article.getStatus().equals("1")) {
                            article_array.add(0,article);
                        }
                    }

                    mAdapter.notifyDataSetChanged();
                    mRecyclerView.smoothScrollToPosition(0);



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
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                // convert byte array to Bitmap

                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0,
                        byteArray.length);

                chosenImageView.setImageBitmap(bitmap);


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
        ContentResolver cR = requireNonNull(context).getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (filePath != null) {
            final ProgressDialog progressDialog=new ProgressDialog(context);
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
                            Toast.makeText(context, "File Uploaded ", Toast.LENGTH_LONG).show();
                            String uploadId = mDatabaseRef.push().getKey();
                            Article_Model upload = new Article_Model(
                                    uploadId,
                                    user_name,
                                    uri.toString(),
                                    descriptionEditText.getText().toString(),
                                    get_current_time(),
                                    "0",
                                    user_id
                            );

                            mDatabaseRef.child(Objects.requireNonNull(uploadId)).setValue(upload);
                            Toast.makeText(context, "Upload successful", Toast.LENGTH_LONG).show();
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
            if(!descriptionEditText.getText().toString().equals("")) {
                String uploadId = mDatabaseRef.push().getKey();
                Article_Model upload = new Article_Model(
                        uploadId,
                        user_name,
                        null,
                        descriptionEditText.getText().toString(),
                        get_current_time(),
                        "0",
                        user_id
                );

                mDatabaseRef.child(Objects.requireNonNull(uploadId)).setValue(upload);
                Toast.makeText(context, "Details saved", Toast.LENGTH_LONG).show();
                clearall();
            }
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


            }

        }

    }









}
