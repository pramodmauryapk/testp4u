package com.p4u.parvarish.unused;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.p4u.parvarish.R;

import static java.util.Objects.requireNonNull;

public class FullImageFragment extends Fragment {


    private static final String DRAWABLE_RESOURE = "resource";
    private static final String TAG = "FullImageFragment";
    final static int LOOPS = 1000;
    public CarouselPagerAdapter adapter;
    ViewPager pager;
    static int count = 10;
    private Context context;
    static int FIRST_PAGE = 10;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_full_image, container, false);

        context = container.getContext();
        initViews();
        ImageView showimage = v.findViewById(R.id.img);
        Button close = v.findViewById(R.id.btnClose);

        int drawbleResource= requireNonNull(getArguments()).getInt(DRAWABLE_RESOURE,0);
        showimage.setImageResource(drawbleResource);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(new ImageGalleryFragment());
                /*DisplayMetrics metrics = new DisplayMetrics();
                Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int pageMargin = ((metrics.widthPixels / 4) * 2);
                pager.setPageMargin(-pageMargin);

                adapter = new CarouselPagerAdapter(new ImageGalleryFragment(),getActivity().getSupportFragmentManager());
                pager.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                pager.addOnPageChangeListener(adapter);

                // Set current item to the middle page so we can fling to both
                // directions left and right
                pager.setCurrentItem(FIRST_PAGE);
                pager.setOffscreenPageLimit(3);*/
            }
        });


        return v;
    }
    private void switchFragment(Fragment fragment) {
        if(getActivity()!=null) {
            requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .addToBackStack(null).commit();
        }

    }
    private void initViews(){

    }


}
