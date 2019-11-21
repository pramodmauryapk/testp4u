package com.p4u.parvarish.menu_data;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.p4u.parvarish.fancydialog.Animation;
import com.p4u.parvarish.fancydialog.FancyAlertDialog;
import com.p4u.parvarish.fancydialog.FancyAlertDialogListener;
import com.p4u.parvarish.fancydialog.Icon;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static java.util.Objects.requireNonNull;

public class ManageContentFragment extends Fragment implements ARecyclerAdapter_model.OnItemClickListener{

    private static final String TAG = "ManageArticlesFragment";
    private ARecyclerAdapter_model mAdapter;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<Article_Model> article_list;
    private View dialogView;
    private Button uploadBtn,chooseImageBtn;
    private EditText nameEditText;
    private EditText descriptionEditText;
    private ImageView img;
    private Context context;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;
    private Spinner sppagelist,sp;
    private String s;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_articles_list, container, false);
        context = container.getContext();
        RecyclerView mRecyclerView = v.findViewById(R.id.mRecyclerView);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        article_list = new ArrayList<>();
        mAdapter = new ARecyclerAdapter_model(context, article_list);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        s = requireNonNull(this.getArguments()).getString("Page");
        // s="TELENT_SUPPORT";
        mStorageRef= FirebaseStorage.getInstance().getReference(s);
        mStorage  = FirebaseStorage.getInstance().getReference(s).getStorage();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("PAGES").child(s);
        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                article_list.clear();

                for (DataSnapshot teacherSnapshot : dataSnapshot.getChildren()) {
                    Article_Model upload = teacherSnapshot.getValue(Article_Model.class);
                    Objects.requireNonNull(upload).setKey(teacherSnapshot.getKey());
                    article_list.add(upload);
                }
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        return v;
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }

    @Override
    public void onItemClick(int position) {
        Article_Model article=article_list.get(position);
        String[] ArticleData={
                article.getId(),
                article.getTitle(),
                article.getDescription(),
                article.getImageUrl(),
                article.getTime(),
                article.getStatus()
        };
       // openDetails(ArticleData);
    }

    @Override
    public void onShowItemClick(int position) {

        Article_Model article = article_list.get(position);
        showUpdateDeleteDialog(
                article.getId(),
                article.getTitle(),
                article.getDescription(),
                article.getImageUrl(),
                article.getTime(),
                article.getStatus());


    }

    @Override
    public void onDeleteItemClick(int position) {

        Article_Model selectedItem = article_list.get(position);
        final String selectedKey = selectedItem.getKey();
        if(selectedItem.getImageUrl()!=null) {
            final StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
            imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    new FancyAlertDialog.Builder(getActivity())
                            .setTitle("Rate us if you like the app")
                            .setMessage("Do you really want to Delete This?")
                            .setNegativeBtnText("Cancel")
                            .setPositiveBtnText("Delete")
                            .setAnimation(Animation.POP)
                            .isCancellable(true)
                            .setIcon(R.drawable.logo, Icon.Visible)
                            .OnPositiveClicked(new FancyAlertDialogListener() {
                                @Override
                                public void OnClick() {

                                    mDatabaseRef.child(selectedKey).removeValue();
                                    Toast.makeText(getActivity(), "Item deleted", Toast.LENGTH_SHORT).show();

                                }
                            })
                            .OnNegativeClicked(new FancyAlertDialogListener() {
                                @Override
                                public void OnClick() {
                                    Toast.makeText(getActivity(), "Item not deleted", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .build();


                }
            });
        }else{
            new FancyAlertDialog.Builder(getActivity())
                    .setTitle("Rate us if you like the app")
                    .setMessage("Do you really want to Delete This?")
                    .setNegativeBtnText("Cancel")
                    .setPositiveBtnText("Delete")
                    .setAnimation(Animation.POP)
                    .isCancellable(true)
                    .setIcon(R.drawable.logo, Icon.Visible)
                    .OnPositiveClicked(new FancyAlertDialogListener() {
                        @Override
                        public void OnClick() {

                            mDatabaseRef.child(selectedKey).removeValue();
                            Toast.makeText(getActivity(), "Item deleted", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .OnNegativeClicked(new FancyAlertDialogListener() {
                        @Override
                        public void OnClick() {
                            Toast.makeText(getActivity(), "Item not deleted", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .build();
        }
    }
    @SuppressLint("InflateParams")
    private void showUpdateDeleteDialog(final String Id,
                                  final String Title,
                                  final String Desc,
                                  final String Url,
                                  final String Time,
                                  final String Status) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder (context);
        LayoutInflater inflater = getLayoutInflater ();
        dialogView = inflater.inflate (R.layout.upload_page_fragment, null);
        dialogBuilder.setView (dialogView);
        dialogBuilder.setTitle ("Update Details");
        final AlertDialog b = dialogBuilder.create ();
        b.show ();
        init_dialog_views ();
        sppagelist.setVisibility(View.GONE);
        setValues(Title,Desc,Url);


        chooseImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseimage(view);
            }
        });
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(getContext(), "An Upload is Still in Progress", Toast.LENGTH_SHORT).show();
                } else {
                   uploadFile(Id,Title,Desc,Url,get_current_time(),"1");


                }
            }
        });

    }
    private void uploadFile(final String id, final String Title, final String Desc,final String Url, final String Time, final String Status) {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            mStorageRef = FirebaseStorage.getInstance().getReference(s);
            final StorageReference sRef = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(filePath));

            sRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            progressDialog.dismiss();
                            mDatabaseRef.child(id).child("title").setValue(nameEditText.getText().toString());
                            mDatabaseRef.child(id).child("description").setValue(descriptionEditText.getText().toString());
                            mDatabaseRef.child(id).child("imageUrl").setValue(uri.toString());
                            mDatabaseRef.child(id).child("time").setValue(get_current_time());
                            mDatabaseRef.child(id).child("status").setValue("1");

                            //displaying success toast
                            Toast.makeText(getContext(), "Article Uploaded ", Toast.LENGTH_LONG).show();

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

        }else{
            mDatabaseRef.child(id).child("title").setValue(nameEditText.getText().toString());
            mDatabaseRef.child(id).child("description").setValue(descriptionEditText.getText().toString());
            mDatabaseRef.child(id).child("imageUrl").setValue(Url);
            mDatabaseRef.child(id).child("time").setValue(get_current_time());
            mDatabaseRef.child(id).child("status").setValue("1");

            //displaying success toast
            Toast.makeText(context, "Data Saved ", Toast.LENGTH_LONG).show();

        }


    }
    private void chooseimage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(context.getContentResolver(),filePath);
                img.setImageBitmap(bitmap);

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
    private void setValues(String Title,String Desc,String Url) {
        nameEditText.setText(Title);
        descriptionEditText.setText(Desc);
        Picasso.get()
                .load(Url)
                .placeholder(R.drawable.logo)
                .fit()
                .into(img);

    }
    private void init_dialog_views(){
        chooseImageBtn = dialogView.findViewById(R.id.button_choose_image);
        uploadBtn = dialogView.findViewById(R.id.uploadBtn);
        descriptionEditText = dialogView.findViewById ( R.id.descriptionEditText );
        img = dialogView.findViewById(R.id.chosenImageView);
        nameEditText =dialogView.findViewById(R.id.nameEditText);
        sppagelist=dialogView.findViewById(R.id.sppagelist);

    }
    private String get_current_time(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss");
        return sdf.format(new Date());
    }

}
