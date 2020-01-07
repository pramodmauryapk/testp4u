package com.p4u.parvarish.HelpingHand;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
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
import com.p4u.parvarish.MenuPages.Page_data_Model;
import com.p4u.parvarish.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AdminHalpingHandFragment extends Fragment implements HalpingHandRecyclerAdapter_model.OnItemClickListener {

    private static final String TAG = AdminHalpingHandFragment.class.getSimpleName();

    private Context context;
    private HalpingHandRecyclerAdapter_model mAdapter;
    private List<Page_data_Model> article_array;
    private DatabaseReference myRef;
    private RecyclerView mRecyclerView;
    private FirebaseStorage mStorage;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef,dR;
    private ValueEventListener mDBListener;
    private View dialogView;
    private Button positive,negative;
    private Spinner spcategory;
    private TextView inst;
    private CardView cd;
    private String cat;
    private View v;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_halpinghand_data, container, false);
        context = container.getContext();
        initViews();
        inst.setVisibility(View.VISIBLE);
        cd.setVisibility(View.GONE);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        article_array = new ArrayList<>();
        mAdapter = new HalpingHandRecyclerAdapter_model(context, article_array);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mStorageRef = FirebaseStorage.getInstance().getReference("TIMELINE");
        mStorage  = FirebaseStorage.getInstance();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("TIMELINE");
        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                article_array.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Page_data_Model article = postSnapshot.getValue(Page_data_Model.class);
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
        cat="General";
        myRef = FirebaseDatabase.getInstance().getReference().child("TIMELINE");
        spcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cat=spcategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                cat=spcategory.getItemAtPosition(0).toString();
            }
        });
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myRef.child(id).child("status").setValue("1");
                myRef.child(id).child("category").setValue(cat);
                b.cancel();
            }
        });

        negative.setOnClickListener(new View.OnClickListener() {
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
    }

    private void init_dialog_views() {
        positive=dialogView.findViewById(R.id.positiveBtn);
        negative=dialogView.findViewById(R.id.negativeBtn);
        spcategory=dialogView.findViewById(R.id.spcategory);
    }

    private void initViews(){
        mRecyclerView =  v.findViewById(R.id.mRecyclerView);
        inst=v.findViewById(R.id.textView1);
        cd=v.findViewById(R.id.li1);

    }


    @Override
    public void onItemClick(int position) {
        Page_data_Model article = article_array.get(position);
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
