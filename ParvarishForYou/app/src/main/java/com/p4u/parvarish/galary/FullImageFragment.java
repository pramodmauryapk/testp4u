package com.p4u.parvarish.galary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.p4u.parvarish.R;

import static java.util.Objects.requireNonNull;

public class FullImageFragment extends Fragment {


    private static final String DRAWABLE_RESOURE = "resource";
    private static final String TAG = "FullImageFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_full_image, container, false);


        initViews();
        ImageView showimage = v.findViewById(R.id.img);
        Button show = v.findViewById(R.id.btnClose);

        int drawbleResource= requireNonNull(getArguments()).getInt(DRAWABLE_RESOURE,0);
        showimage.setImageResource(drawbleResource);

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemFragment fragment = new ItemFragment();
                switchFragment(fragment);
            }
        });


        return v;
    }
    private void switchFragment(Fragment fragment) {

        requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null).commit();

    }
    private void initViews(){

    }

}
