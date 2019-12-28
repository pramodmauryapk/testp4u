package com.p4u.parvarish.HelpingHand;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
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
import com.p4u.parvarish.user_pannel.Teacher;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.util.Objects.requireNonNull;

public class ShowCategoryWiseFragment extends Fragment{

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
    private String user_id, user_name,user_role,cat;
    private View v,dialogView;
    private Spinner spcategory;

    private Camera camera;
    // newInstance constructor for creating fragment with arguments
    public static ShowCategoryWiseFragment newInstance(int page) {
        ShowCategoryWiseFragment fragment = new ShowCategoryWiseFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

       v = inflater.inflate(R.layout.fragment_category_wise_data, container, false);
        context = container.getContext();
        initViews();
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        article_array = new ArrayList<>();
        mAdapter = new HalpingHandRecyclerAdapter_model(context, article_array);
        mRecyclerView.setAdapter(mAdapter);

        mStorageRef = FirebaseStorage.getInstance().getReference("TIMELINE");
        mStorage  = FirebaseStorage.getInstance().getReference("TIMELINE").getStorage();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("TIMELINE");
        user_name="Unknown User";
        user_id="Unknown";
        cat="General";
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
        spcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cat=spcategory.getSelectedItem().toString();
                load_list();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                cat=spcategory.getItemAtPosition(0).toString();
                load_list();
            }
        });

        return v;
    }



    private void initViews(){



        mRecyclerView =  v.findViewById(R.id.mRecyclerView);
        spcategory=v.findViewById(R.id.spcategory);


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
                        if (article.getStatus().equals("1")&& article.getCategory().equals(cat)) {
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





}
