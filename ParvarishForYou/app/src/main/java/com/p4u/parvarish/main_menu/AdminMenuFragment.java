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
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

public class AdminMenuFragment extends HomeFragment {

    private static final String TAG = "AdminMenuFragment";
    private DatabaseReference myRef;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_menu, container, false);
        initViews();
        Context context = container.getContext();
        assert this.getArguments() != null;
        String role = this.getArguments().getString("user_role");
        getWidthAndHeight();
        final GridView androidGridView = v.findViewById(R.id.grid_view_image_text);
        String[] gridViewString = {};
        int[] gridViewImageId = {};
        assert role != null;
        if(role.equals("USER")){

            gridViewString = new String[]{
                    "Center List",
                    "Search Book",
                    "Donor List"

            };
            gridViewImageId = new int[]{

                    R.drawable.ic_list_black_24dp,
                    R.drawable.ic_youtube_searched_for_black_24dp,
                    R.drawable.ic_card_giftcard_black_24dp

            };

        }else if(role.equals("HEAD")){
            gridViewString = new String[]{
                    "Center List",
                    "Search Book",
                    "Donor List",
                    "Add Book",
                    "Issue Book",
                    "Submit Book",
                    "User Mobiles",
                    "Book List",

            };
            gridViewImageId = new int[]{

                    R.drawable.ic_list_black_24dp,
                    R.drawable.ic_youtube_searched_for_black_24dp,
                    R.drawable.ic_card_giftcard_black_24dp,
                    R.drawable.ic_add_book_24dp,
                    R.drawable.ic_child_friendly_black_24dp,
                    R.drawable.ic_transfer_within_a_station_black_24dp,
                    R.drawable.ic_phone_in_talk_black_24dp,
                    R.drawable.ic_book_black_24dp,

            };
        }
        else{
            gridViewString = new String[]{
                    "Center List",
                    "Search Book",
                    "Donor List",
                    "Add Book",
                    "Issue Book",
                    "Submit Book",
                    "User Mobiles",
                    "Book List",
                    "Manage User",
                    "Update Book",
                    "Manage News",
                    "Manage Gallery",
                    "Report Book"

            };

            gridViewImageId = new int[]{

                    R.drawable.ic_list_black_24dp,
                    R.drawable.ic_youtube_searched_for_black_24dp,
                    R.drawable.ic_card_giftcard_black_24dp,
                    R.drawable.ic_add_book_24dp,
                    R.drawable.ic_child_friendly_black_24dp,
                    R.drawable.ic_transfer_within_a_station_black_24dp,
                    R.drawable.ic_phone_in_talk_black_24dp,
                    R.drawable.ic_book_black_24dp,
                    R.drawable.ic_verified_user_black_24dp,
                    R.drawable.ic_update_black_24dp,
                    R.drawable.ic_library_books_black_24dp,
                    R.drawable.ic_image_black_24dp,
                    R.drawable.ic_report_black_24dp

            };

        }
        LayoutGridView adapterViewAndroid = new LayoutGridView(getContext(), gridViewString, gridViewImageId);


        androidGridView.setAdapter(adapterViewAndroid);
        androidGridView.setColumnWidth(getWidthAndHeight()/5);
        androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int i, long id) {

                // Toast.makeText(getContext(), "GridView Item: " + gridViewString[+i], Toast.LENGTH_LONG).show();
                switch (i) {

                    case 0:
                        switchFragment(new CentreListFragment());
                        break;
                    case 1:
                        switchFragment(new SearchBookFragment());
                        break;
                    case 2:
                        switchFragment(new DonorListFragment());
                        break;
                    case 3:
                        switchFragment(new AddBookFragment());
                        break;
                    case 4:
                        switchFragment(new IssueBookFragment());
                        break;
                    case 5:switchFragment(new SubmitBookFragment());

                        break;
                    case 6: switchFragment(new UserMobileFragment());

                        break;
                    case 7:switchFragment(new ManageUserFragment());
                        break;
                    case 8:
                        switchFragment(new UpdateBookFragment());
                        break;
                    case 9:
                        switchFragment(new NewsAddFragment());
                        break;
                    case 10:
                        switchFragment(new ManageGalleryFragment());
                        break;
                    case 11:
                        Intent ActivityIndent = new Intent(getContext(), ListViewActivity.class);
                        startActivity(ActivityIndent);
                        break;
                    case 12:
                        break;

                }

            }
        });





        return v;
    }
    private void initViews(){

    }
    private void makeToast(String input) {
        Toast.makeText(getContext(), input, Toast.LENGTH_SHORT).show();
    }
    private int getWidthAndHeight() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenHeight = displaymetrics.heightPixels;
        int screenWidth = displaymetrics.widthPixels;
        return screenWidth;
    }
    // switching fragment
    private void switchFragment(Fragment fragment) {

        requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null).commit();

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
