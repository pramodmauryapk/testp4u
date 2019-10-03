package com.p4u.parvarish.galary;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.p4u.parvarish.R;

import static java.util.Objects.requireNonNull;

public class ManageGalleryFragment extends Fragment {

    private static final String TAG = "ManageGalleryFragment";
    private Button openTeachersActivityBtn,openUploadActivityBtn;
    private View v;
    private RatingBar ratingBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_manage_gallery,container,false);


        initViews();

        openTeachersActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFragment(new ImageItemsFragment());
            }
        });
        openUploadActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), UploadActivity.class));
                requireNonNull(getActivity()).finish();
            }
        });


        return v;
    }
    private void initViews(){
        openTeachersActivityBtn = v.findViewById ( R.id.openTeachersActivityBtn );
        openUploadActivityBtn = v.findViewById ( R.id.openUploadActivityBtn );
    }
    private void switchFragment(Fragment fragment) {

        requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null).commit();
    }
    @Override
    public void onResume() {
        super.onResume();
    }

}
