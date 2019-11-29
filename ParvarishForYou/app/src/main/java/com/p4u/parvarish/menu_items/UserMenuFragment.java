package com.p4u.parvarish.menu_items;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.p4u.parvarish.MenuPages.PageListFragment;
import com.p4u.parvarish.R;
import com.p4u.parvarish.Timeline.ShowTimelineFragment;
import com.p4u.parvarish.gallary.newgallaryFragment;
import com.p4u.parvarish.gallary.slideshowFragment;
import com.p4u.parvarish.video.youtubegallaryFragment;

import static java.util.Objects.requireNonNull;

public class UserMenuFragment extends HomeFragment {

    private String Role;
    private Bundle bundle;
    private String[] gridViewString = {
            "Talent Support",
            "Knowledge Center",
            "Technical Partnership",
            "Capacity Building",
            "Internship Program",
            "Academic Partner",
            "Gallery",
            "Slide Show",
            "Videos",
            "Timeline"
    } ;

    private int[] gridViewImageId = {
            R.drawable.ic_touch_app_black_24dp,
            R.drawable.ic_account_balance_black_24dp,
            R.drawable.ic_pets_black_24dp,
            R.drawable.ic_business_black_24dp,
            R.drawable.ic_transfer_within_a_station_black_24dp,
            R.drawable.ic_school_black_24dp,
            R.drawable.ic_camera_black_24dp,
            R.drawable.ic_vibration_black_24dp,
            R.drawable.ic_ondemand_video_black_24dp,
            R.drawable.ic_playlist_add_black_24dp

    };


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_menu, container, false);
        Context context = container.getContext();
        bundle=new Bundle();
        assert this.getArguments() != null;
        Role = this.getArguments().getString("user_role");
        LayoutGridView adapterViewAndroid = new LayoutGridView(context, gridViewString, gridViewImageId);
        final GridView androidGridView = v.findViewById(R.id.grid_view_image_text);
        androidGridView.setAdapter(adapterViewAndroid);
        androidGridView.setColumnWidth(getWidthAndHeight()/6);
        androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int i, long id) {
                switch (i) {
                    case 0:
                        switchFragment(new PageListFragment(),0);
                        break;
                    case 1:
                        switch_menu(new AdminMenuFragment());
                        break;
                    case 2:
                        switchFragment(new PageListFragment(),2);
                        break;
                    case 3:
                        switchFragment(new PageListFragment(),3);
                        break;
                    case 4:
                        switchFragment(new PageListFragment(),4);
                        break;
                    case 5:
                        switchFragment(new PageListFragment(),5);
                        break;
                    case 6:
                        switchFragment(new newgallaryFragment(),6);
                        break;
                    case 7:
                        switchFragment(new slideshowFragment(),7);

                        break;
                    case 8:
                        //Intent ActivityIndent = new Intent(getActivity(), RecyclerViewActivity.class);
                        //startActivity(ActivityIndent);
                        switchFragment(new youtubegallaryFragment(),8);
                        //switchFragment(new chatlistfragment(),8);
                        break;
                    case 9:switchFragment(new ShowTimelineFragment(),9);//new ImageGalleryFragment()
                        break;

                }

            }
        });

        return v;
    }

    private int getWidthAndHeight() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.widthPixels;
    }
    // switching fragment
    private void switchFragment(Fragment fragment,int position) {
        bundle.putString("user_role",Role);
        bundle.putString("position",String.valueOf(position));
        fragment.setArguments(bundle);
        requireNonNull(getActivity()).getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null)
                .commit();

    }
    private void switch_menu(Fragment fragment) {
        bundle.putString("user_role",Role);
        fragment.setArguments(bundle);
        if(getActivity()!=null) {
            requireNonNull(getActivity()).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.menu_fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }

    }









}
