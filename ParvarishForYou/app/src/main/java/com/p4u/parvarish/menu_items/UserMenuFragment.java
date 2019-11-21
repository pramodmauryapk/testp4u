package com.p4u.parvarish.menu_items;

import android.content.Context;
import android.content.Intent;
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

import com.p4u.parvarish.R;
import com.p4u.parvarish.galary.GalleryFragment;
import com.p4u.parvarish.menu_data.ShowTimelineFragment;
import com.p4u.parvarish.menu_data.menudataFragment;
import com.p4u.parvarish.video.RecyclerViewActivity;

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
        androidGridView.setColumnWidth(getWidthAndHeight()/4);
        androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int i, long id) {
                switch (i) {
                    case 0:
                        switchFragment(new menudataFragment(),0);
                        break;
                    case 1:
                        switch_menu(new AdminMenuFragment());
                        break;
                    case 2:
                        switchFragment(new menudataFragment(),2);
                        break;
                    case 3:
                        switchFragment(new menudataFragment(),3);
                        break;
                    case 4:
                        switchFragment(new menudataFragment(),4);
                        break;
                    case 5:
                        switchFragment(new menudataFragment(),5);
                        break;
                    case 6:
                        switchFragment(new GalleryFragment(),6);//new ImageGalleryFragment()
                        break;
                    case 7:
                        Intent ActivityIndent = new Intent(getActivity(), RecyclerViewActivity.class);
                        startActivity(ActivityIndent);
                        break;
                    case 8:switchFragment(new ShowTimelineFragment(),8);//new ImageGalleryFragment()
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


		/*AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull (getContext ()));
		builder.setMessage("Are you sure you want to exit?")
				.setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						//Objects.requireNonNull (getActivity ()).finishAffinity ();

					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					//	HomeFragment fragment=new HomeFragment ();
					//	switchFragment (fragment);
						dialog.cancel();

					}
				});
		AlertDialog alert = builder.create();
		alert.show();*/







}
