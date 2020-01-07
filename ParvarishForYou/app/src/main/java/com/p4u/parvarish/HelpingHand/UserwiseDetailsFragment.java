package com.p4u.parvarish.HelpingHand;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.mindorks.paracamera.Camera;
import com.p4u.parvarish.MenuPages.Page_data_Model;
import com.p4u.parvarish.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.util.Objects.requireNonNull;

public class UserwiseDetailsFragment extends Fragment implements HalpingHandRecyclerAdapter_model.OnItemClickListener {

    private static final String TAG = UserwiseDetailsFragment.class.getSimpleName();

    private TextView nameDetailTextView;
    private TextView Email;
    private TextView Role;
    private TextView Mobile,Identity,Address,Status;
    private CircleImageView teacherDetailImageView;
    private View v;
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
    private String name,user_id, user_name,user_role;
    private View dialogView;

    private Camera camera;

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_userchat_detail,container,false);
        context = container.getContext();
        initpersonals();
        //RECEIVE DATA FROM ITEMSACTIVITY VIA INTENT

        assert getArguments() != null;
        name= getArguments().getString("NAME_KEY");
        String email=getArguments().getString("EMAIL_KEY");
        String imageURL=getArguments().getString("IMAGE_KEY");
        String mobile=getArguments().getString("MOBILE_KEY");
        String role=getArguments().getString("ROLE_KEY");
        String identity=getArguments().getString("IDENTITY_KEY");
        String address=getArguments().getString("ADDRESS_KEY");
        String status=getArguments().getString("STATUS_KEY");
        //SET RECEIVED DATA TO TEXTVIEWS AND IMAGEVIEWS
        nameDetailTextView.setText(name);
        Email.setText(email);
        Role.setText("ROLE: "+role);
        Mobile.setText("MOBILE: "+mobile);
        Identity.setText("IDENTITY: "+identity);
        Address.setText("ADDRESS: "+address);
        assert status != null;
        if(status.equals("1")){
            Status.setText("STATUS: "+"ACTIVE USER");

        }
        else {
            Status.setText("STATUS: "+"USER NOT ACTIVE");
        }

        Picasso.get()
                .load(imageURL)
                .placeholder(R.drawable.userpic)
                .fit()
                .centerCrop()
                .into(teacherDetailImageView);

        mRecyclerView =  v.findViewById(R.id.mRecyclerView);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        article_array = new ArrayList<>();
        mAdapter = new HalpingHandRecyclerAdapter_model(context, article_array);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("TIMELINE");


        return v;
    }

    private void initpersonals(){
        nameDetailTextView= v.findViewById(R.id.nameDetailTextView);
        Email= v.findViewById(R.id.tviewemail);
        Role= v.findViewById(R.id.tviewrole);
        Mobile = v.findViewById(R.id.tviewmobile);
        Identity=v.findViewById(R.id.tviewidentity);
        Address=v.findViewById(R.id.tviewaddress);
        Status=v.findViewById(R.id.tviewstatus);
        teacherDetailImageView=v.findViewById(R.id.teacherDetailImageView);
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
                        if (article.getStatus().equals("1")&&(article.getTitle().equals(name))) {
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
