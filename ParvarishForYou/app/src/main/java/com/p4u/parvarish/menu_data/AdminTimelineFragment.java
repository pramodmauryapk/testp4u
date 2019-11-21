package com.p4u.parvarish.menu_data;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.p4u.parvarish.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AdminTimelineFragment extends Fragment implements TimelineRecyclerAdapter_model.OnItemClickListener {

    private static final String TAG = "ShowTimelineFragment";
    private Context context;
    private TimelineRecyclerAdapter_model mAdapter;
    private List<Article_Model> article_array;
    private DatabaseReference myRef;
    private RecyclerView mRecyclerView;
    private FirebaseStorage mStorage;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef,dR;
    private ValueEventListener mDBListener;
    private View dialogView;
    private Button positive,negative;
    private TextView inst;
    private CardView cd;
    private View v;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_timeline_data, container, false);
        context = container.getContext();
        initViews();
        inst.setVisibility(View.VISIBLE);
        cd.setVisibility(View.GONE);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        article_array = new ArrayList<>();
        mAdapter = new TimelineRecyclerAdapter_model(context, article_array);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mStorageRef = FirebaseStorage.getInstance().getReference("TIMELINE");
        mStorage  = FirebaseStorage.getInstance();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("TIMELINE");
        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                article_array.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Article_Model article = postSnapshot.getValue(Article_Model.class);
                        Objects.requireNonNull(article).setKey(postSnapshot.getKey());
                        if (article.getStatus().equals("0")) {
                            article_array.add(0,article);
                        }
                    }


                    mRecyclerView.smoothScrollToPosition(0);
                    mAdapter.notifyDataSetChanged();
                if(article_array.size()==0){
                    inst.setText("No Post Found");
                }else{
                    inst.setText("Tap to select");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });




        return v;
    }
    @SuppressLint("InflateParams")
    private void show_publish_dialog(final String id, final String url, final String status) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        final LayoutInflater inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.allowdenydialog, null);
        dialogBuilder.setView(dialogView);
        init_dialog_views();
        final AlertDialog b = dialogBuilder.create();
        b.show();
        myRef = FirebaseDatabase.getInstance().getReference().child("TIMELINE");
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myRef.child(id).child("status").setValue("1");
                b.cancel();
            }
        });
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dR = FirebaseDatabase.getInstance().getReference().child("TIMELINE").child(id);
                 StorageReference imageRef = mStorage.getReferenceFromUrl(url);
                 imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void aVoid) {
                       dR.removeValue();
                       Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();
                   }
               });

                b.cancel();
            }
        });
    }

    private void init_dialog_views() {
        positive=dialogView.findViewById(R.id.positiveBtn);
        negative=dialogView.findViewById(R.id.negativeBtn);
    }

    private void initViews(){
        mRecyclerView =  v.findViewById(R.id.mRecyclerView);
        inst=v.findViewById(R.id.textView1);
        cd=v.findViewById(R.id.li1);

    }


    @Override
    public void onItemClick(int position) {
        Article_Model article = article_array.get(position);
        show_publish_dialog(
                article.getId(),
                article.getImageUrl(),
                article.getStatus()

        );
    }

    @Override
    public void onShowItemClick(int position) {

    }

    @Override
    public void onDeleteItemClick(int position) {

    }
}
