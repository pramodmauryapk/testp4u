package com.p4u.parvarish.gallary;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.p4u.parvarish.R;

import static java.util.Objects.requireNonNull;

public class ManagegalaryFragment extends Fragment {

    private static final String TAG = ManagegalaryFragment.class.getSimpleName();

    private Button openTeachersActivityBtn,openUploadActivityBtn;
    private View v;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_manage_gallery,container,false);

        context = container.getContext();
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
               // startActivity(new Intent(context, UploadImage.class));
               // requireNonNull(getActivity()).finish();
                switchFragment(new AddGallaryItemFragment());
            }
        });


        return v;
    }
    private void initViews(){
        openTeachersActivityBtn = v.findViewById ( R.id.openTeachersActivityBtn );
        openUploadActivityBtn = v.findViewById ( R.id.openUploadActivityBtn );

    }
    private void switchFragment(Fragment fragment) {
        if(getActivity()!=null) {
            requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .addToBackStack(null).commit();
        }
    }


}
