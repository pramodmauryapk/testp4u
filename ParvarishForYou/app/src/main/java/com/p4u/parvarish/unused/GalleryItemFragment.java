package com.p4u.parvarish.unused;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.p4u.parvarish.R;
import com.p4u.parvarish.gallary.Image_Model;

import java.util.List;

import static java.util.Objects.requireNonNull;


public class GalleryItemFragment extends Fragment {
    private static final String TAG = "GalleryItemFragment";
    private static final String POSITON = "position";
    private static final String SCALE = "scale";
    private static final String DRAWABLE_RESOURE = "resource";
    private ImageView imageView;
    private int screenWidth;
    private int screenHeight;
    private List<Image_Model> mImageModels;
    private List<String> list;
    private Context context;
    private ArrayAdapter mAdapter;
    private int[] imageArray = new int[]{};

    static Fragment newInstance(ImageGalleryFragment context, int pos, float scale) {
        Bundle b = new Bundle();
        b.putInt(POSITON, pos);
        b.putFloat(SCALE, scale);

        return Fragment.instantiate(requireNonNull(context.getActivity()), GalleryItemFragment.class.getName(), b);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWidthAndHeight();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        context = container.getContext();
       int[] imageArray = new int[]{R.drawable.image1, R.drawable.image2,
                R.drawable.image3, R.drawable.image4, R.drawable.image5,
                R.drawable.image6, R.drawable.image7, R.drawable.image8,
                R.drawable.image9, R.drawable.image10};
        final int postion = requireNonNull(this.getArguments()).getInt(POSITON);
        float scale = this.getArguments().getFloat(SCALE);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(screenWidth / 2, screenHeight / 2);
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_image, container, false);
        TextView textView = linearLayout.findViewById(R.id.text);
        CarouselLayout root = linearLayout.findViewById(R.id.root_container);
        imageView = linearLayout.findViewById(R.id.pagerImg);
        textView.setText("Gallery item: " + postion);
        imageView.setLayoutParams(layoutParams);


        imageView.setImageResource(imageArray[postion]);






        //handling click event
        imageView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            }
       });

        root.setScaleBoth(scale);

        return linearLayout;
    }
    private String get_images(){

        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("UPLOADED_IMAGES");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                list.clear();
                for (DataSnapshot teacherSnapshot : dataSnapshot.getChildren()) {
                    Image_Model upload = teacherSnapshot.getValue(Image_Model.class);
                    assert upload != null;
                    upload.setKey(teacherSnapshot.getKey());
                    list.add(upload.getImageUrl());


                }
            //imageArray.
               //mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return  list.get(0);
    }
    private void switchFragment(Fragment fragment) {

        requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null).commit();

    }
    
    private void getWidthAndHeight() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        screenWidth = displaymetrics.widthPixels;
    }

}
