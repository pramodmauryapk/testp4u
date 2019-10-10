package com.p4u.parvarish.main_menu;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.Objects;
import java.util.Stack;

import com.p4u.parvarish.R;
import com.p4u.parvarish.admin_pannel.AddBookFragment;
import com.p4u.parvarish.admin_pannel.IssueBookFragment;
import com.p4u.parvarish.admin_pannel.SearchBookFragment;
import com.p4u.parvarish.admin_pannel.SubmitBookFragment;
import com.p4u.parvarish.admin_pannel.UpdateBookFragment;
import com.p4u.parvarish.galary.ImageGalleryFragment;
import com.p4u.parvarish.galary.ManageGalleryFragment;
import com.p4u.parvarish.grid.LayoutGridView;
import com.p4u.parvarish.list_style.ListViewActivity;
import com.p4u.parvarish.marquee.NewsAddFragment;
import com.p4u.parvarish.user_pannel.ManageUserFragment;
import com.p4u.parvarish.user_pannel.UserMobileFragment;

import static java.util.Objects.requireNonNull;

public class UserMenuFragment extends HomeFragment{

    private static final String TAG = "UserMenuFragment";
    private String Role;
    private Bundle bundle;
    private String[] gridViewString = {
            "Telent Support",
            "Knowledge Center",
            "Technical Partnership",
            "Capacity Building",
            "Gallery",


    } ;

    private int[] gridViewImageId = {
            R.drawable.ic_touch_app_black_24dp,
            R.drawable.ic_account_balance_black_24dp,
            R.drawable.ic_pets_black_24dp,
            R.drawable.ic_business_black_24dp,
            R.drawable.ic_camera_black_24dp,


    };





    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_menu, container, false);
        bundle=new Bundle();
        Context context = container.getContext();
        assert this.getArguments() != null;
        Role = this.getArguments().getString("user_role");
        LayoutGridView adapterViewAndroid = new LayoutGridView(getContext(), gridViewString, gridViewImageId);
        final GridView androidGridView = v.findViewById(R.id.grid_view_image_text);
        androidGridView.setAdapter(adapterViewAndroid);
        androidGridView.setColumnWidth(getWidthAndHeight()/4);
        androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int i, long id) {

                switch (i) {
                    case 0:
                        switchFragment(new TelentSupportFragment());
                        break;
                    case 1:
                        switchFragment(new AdminMenuFragment());
                        break;
                    case 2:
                        switchFragment(new TechnicalPartnershipFragment());
                        break;
                    case 3:
                        switchFragment(new CapacityBuildingFragment());
                        break;
                    case 4:
                        switchFragment(new ImageGalleryFragment());
                        break;

                }

            }
        });

        return v;
    }




    private void makeToast(String input) {
        Toast.makeText(getContext(), input, Toast.LENGTH_SHORT).show();
    }

    private int getWidthAndHeight() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenHeight = displaymetrics.heightPixels;
        return displaymetrics.widthPixels;
    }
    // switching fragment
    private void switchFragment(Fragment fragment) {
        bundle.putString("user_role",Role);
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
        requireNonNull(getActivity()).getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.menu_fragment_container, fragment)
                .addToBackStack(null)
                .commit();

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
