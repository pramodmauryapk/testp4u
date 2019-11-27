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

import com.p4u.parvarish.Notification.NotificationActivity;
import com.p4u.parvarish.R;
import com.p4u.parvarish.book_pannel.AddBookFragment;
import com.p4u.parvarish.book_pannel.BeneficiaryAddFragment;
import com.p4u.parvarish.book_pannel.BookListFragment;
import com.p4u.parvarish.book_pannel.IssueBookFragment;
import com.p4u.parvarish.book_pannel.SearchBookFragment;
import com.p4u.parvarish.book_pannel.UpdateBookFragment;
import com.p4u.parvarish.gallary.ManagegalaryFragment;
import com.p4u.parvarish.Timeline.AdminTimelineFragment;
import com.p4u.parvarish.Articles.ManageArticleFragment;
import com.p4u.parvarish.news_marquee.NewsAddFragment;
import com.p4u.parvarish.user_pannel.ManageUserFragment;
import com.p4u.parvarish.user_pannel.UserMobileFragment;
import com.p4u.parvarish.video.AddYoutubeVideofragment;

import static java.util.Objects.requireNonNull;

public class AdminMenuFragment extends HomeFragment {

    private static final String TAG = "AdminMenuFragment";
    private String role;
    private Bundle bundle;
    private Context context;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_menu, container, false);
        initViews();
        context = container.getContext();
        assert this.getArguments() != null;
        role = this.getArguments().getString("user_role");
        getWidthAndHeight();
        final GridView androidGridView = v.findViewById(R.id.grid_view_image_text);
        String[] gridViewString;
        int[] gridViewImageId;
        assert role != null;
        if(role.equals("USER")){

            gridViewString = new String[]{
                    "Center List",
                    "Search Book",
                    "Donor List",
                    "Book List"

            };
            gridViewImageId = new int[]{

                    R.drawable.ic_list_black_24dp,
                    R.drawable.ic_youtube_searched_for_black_24dp,
                    R.drawable.ic_card_giftcard_black_24dp,
                    R.drawable.ic_book_black_24dp

            };

        }else if(role.equals("HEAD")){
            gridViewString = new String[]{
                    "Center List",
                    "Search Book",
                    "Donor List",
                    "Book List",
                    "Add Book",
                    "Issue Book",
                    "Submit Book",
                    "User Mobiles"


            };
            gridViewImageId = new int[]{

                    R.drawable.ic_list_black_24dp,
                    R.drawable.ic_youtube_searched_for_black_24dp,
                    R.drawable.ic_card_giftcard_black_24dp,
                    R.drawable.ic_book_black_24dp,
                    R.drawable.ic_add_book_24dp,
                    R.drawable.ic_child_friendly_black_24dp,
                    R.drawable.ic_transfer_within_a_station_black_24dp,
                    R.drawable.ic_phone_in_talk_black_24dp


            };
        }
        else{
            gridViewString = new String[]{
                    "Center List",
                    "Search Book",
                    "Donor List",
                    "Book List",
                    "Add Book",
                    "Issue Book",
                    "Submit Book",
                    "User Mobiles",
                    "Manage User",
                    "Update Book",
                    "Manage News",
                    "Manage Gallery",
                    "Report Book",
                    "Manage Articles",
                    "Manage Timeline",
                    "Add Video"

            };

            gridViewImageId = new int[]{

                    R.drawable.ic_list_black_24dp,
                    R.drawable.ic_youtube_searched_for_black_24dp,
                    R.drawable.ic_card_giftcard_black_24dp,
                    R.drawable.ic_book_black_24dp,
                    R.drawable.ic_add_book_24dp,
                    R.drawable.ic_child_friendly_black_24dp,
                    R.drawable.ic_transfer_within_a_station_black_24dp,
                    R.drawable.ic_phone_in_talk_black_24dp,
                    R.drawable.ic_verified_user_black_24dp,
                    R.drawable.ic_update_black_24dp,
                    R.drawable.ic_library_books_black_24dp,
                    R.drawable.ic_image_black_24dp,
                    R.drawable.ic_report_black_24dp,
                    R.drawable.ic_burst_mode_black_24dp,
                    R.drawable.ic_mode_comment_black_24dp,
                    R.drawable.ic_video_call_black_24dp

            };

        }
        LayoutGridView adapterViewAndroid = new LayoutGridView(context, gridViewString, gridViewImageId);
        androidGridView.setAdapter(adapterViewAndroid);
        androidGridView.setColumnWidth(getWidthAndHeight()/6);
        androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int i, long id) {


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
                        switchFragment(new BookListFragment());
                        break;
                    case 4:
                        switchFragment(new AddBookFragment());
                        break;
                    case 5:
                        switchFragment(new BeneficiaryAddFragment());
                        break;
                    case 6:
                        switchFragment(new IssueBookFragment());
                        break;
                    case 7:
                        switchFragment(new UserMobileFragment());
                        break;
                    case 8:
                        switchFragment(new ManageUserFragment());
                        break;
                    case 9:
                        switchFragment(new UpdateBookFragment());
                        break;
                    case 10:
                        switchFragment(new NewsAddFragment());
                        break;
                    case 11:
                        switchFragment(new ManagegalaryFragment());
                        break;
                    case 12:
                        Intent ActivityIndent = new Intent(context, NotificationActivity.class);
                        startActivity(ActivityIndent);
                       // switchFragment(new newgallaryFragment());
                        break;
                    case 13:
                        switchFragment(new ManageArticleFragment());

                        break;
                    case 14:
                        switchFragment(new AdminTimelineFragment());
                        break;
                    case 15:
                        switchFragment(new AddYoutubeVideofragment());
                        break;

                }

            }
        });





        return v;
    }
    private void initViews(){

    }







    private int getWidthAndHeight() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenHeight = displaymetrics.heightPixels;
        return displaymetrics.widthPixels;
    }
    // switching fragment
    private void switchFragment(Fragment fragment) {
        if(getActivity()!=null) {
            requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_frame, fragment)
                    .addToBackStack(null).commit();
        }

    }
    private void switch_menu(Fragment fragment) {
        bundle.putString("user_role",role);
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
