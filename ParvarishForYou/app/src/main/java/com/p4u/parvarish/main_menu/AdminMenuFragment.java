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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.p4u.parvarish.R;
import com.p4u.parvarish.WelcomeActivity;
import com.p4u.parvarish.admin_pannel.AddBookFragment;
import com.p4u.parvarish.admin_pannel.IssueBookFragment;
import com.p4u.parvarish.admin_pannel.SearchBookFragment;
import com.p4u.parvarish.admin_pannel.SubmitBookFragment;
import com.p4u.parvarish.admin_pannel.UpdateBookFragment;
import com.p4u.parvarish.galary.ManageGalleryFragment;
import com.p4u.parvarish.grid.Menu;
import com.p4u.parvarish.grid.GridRecyclerAdapter;
import com.p4u.parvarish.marquee.NewsAddFragment;
import com.p4u.parvarish.user_pannel.UserListFragment;
import com.p4u.parvarish.user_pannel.UserMobileFragment;

import static java.util.Objects.requireNonNull;

public class AdminMenuFragment extends Fragment {

    private static final String TAG = "AdminMenuFragment";
    private FirebaseAuth mAuth;
    private View v;
    DatabaseReference myRef;
    private List<Menu> mList = new ArrayList<>();
    private String Role;
    private FirebaseUser user;
    private Context context;
    private int screenWidth;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_menu,container,false);


        initViews();
        context= container.getContext();
        assert this.getArguments() != null;
        Role = this.getArguments().getString("user_role");
        getWidthAndHeight();
        addmenus();
        add_click_listner();


        return v;
    }
    private void initViews(){

    }
    private void add_click_listner() {
        RecyclerView mRecyclerView = v.findViewById(R.id.cardList);
        mRecyclerView.setLayoutManager(new GridLayoutManager(context,screenWidth/200));
        GridRecyclerAdapter mAdapter = new GridRecyclerAdapter(getContext(), mList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //makeToast("" + mList.get(position).getPosition());
                switch (mList.get(position).getPosition()) {
                    case 0:
                        AdminMenuFragment.this.switchFragment(new CentreListFragment());
                        break;
                    case 1:
                        AdminMenuFragment.this.switchFragment(new SearchBookFragment());
                        break;
                    case 2:
                        AdminMenuFragment.this.switchFragment(new DonorListFragment());
                        break;
                    case 3:
                        AdminMenuFragment.this.switchFragment(new IssueBookFragment());
                        break;
                    case 4:
                        AdminMenuFragment.this.switchFragment(new SubmitBookFragment());
                        break;
                    case 5:
                        AdminMenuFragment.this.switchFragment(new UserMobileFragment());
                        break;
                    case 6:
                        AdminMenuFragment.this.switchFragment(new UserListFragment());
                        break;
                    case 7:
                        AdminMenuFragment.this.switchFragment(new AddBookFragment());
                        break;
                    case 8:
                        AdminMenuFragment.this.switchFragment(new UpdateBookFragment());
                        break;
                    case 9:
                        AdminMenuFragment.this.switchFragment(new NewsAddFragment());
                        break;
                    case 10:
                        AdminMenuFragment.this.switchFragment(new ManageGalleryFragment());
                        break;

                }
            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void addmenus() {

        mList.clear();
        mList.add(new Menu(Objects.requireNonNull(getActivity()).getDrawable(R.drawable.ic_list_black_24dp), "Center List", 0));
        mList.add(new Menu(Objects.requireNonNull(getActivity()).getDrawable(R.drawable.ic_youtube_searched_for_black_24dp),"Search Book", 1));
        mList.add(new Menu(Objects.requireNonNull(getActivity()).getDrawable(R.drawable.ic_card_giftcard_black_24dp),"Donor List", 2));
        if(Role.equals("HEAD")){
            mList.add(new Menu(Objects.requireNonNull(getActivity()).getDrawable(R.drawable.ic_child_friendly_black_24dp),"Issue Book", 3));
            mList.add(new Menu(Objects.requireNonNull(getActivity()).getDrawable(R.drawable.ic_transfer_within_a_station_black_24dp),"Submit Book", 4));
            mList.add(new Menu(Objects.requireNonNull(getActivity()).getDrawable(R.drawable.ic_phone_in_talk_black_24dp),"User Mobiles", 5));
            mList.add(new Menu(Objects.requireNonNull(getActivity()).getDrawable(R.drawable.ic_add_book_24dp),"Add Book", 7));
            // mList.add(new Menu(Objects.requireNonNull(getActivity()).getDrawable(R.drawable.ic_report_black_24dp),"Report Book", 23));
        }
       else if(Role.equals("ADMIN")) {
            mList.add(new Menu(Objects.requireNonNull(getActivity()).getDrawable(R.drawable.ic_child_friendly_black_24dp),"Issue Book", 3));
            mList.add(new Menu(Objects.requireNonNull(getActivity()).getDrawable(R.drawable.ic_transfer_within_a_station_black_24dp),"Submit Book", 4));
            mList.add(new Menu(Objects.requireNonNull(getActivity()).getDrawable(R.drawable.ic_phone_in_talk_black_24dp),"User Mobiles", 5));
            mList.add(new Menu(Objects.requireNonNull(getActivity()).getDrawable(R.drawable.ic_verified_user_black_24dp), "Manage User", 6));
            mList.add(new Menu(Objects.requireNonNull(getActivity()).getDrawable(R.drawable.ic_add_book_24dp),"Add Book", 7));
            mList.add(new Menu(Objects.requireNonNull(getActivity()).getDrawable(R.drawable.ic_update_black_24dp),"Update Book", 8));
            mList.add(new Menu(Objects.requireNonNull(getActivity()).getDrawable(R.drawable.ic_library_books_black_24dp),"Manage News", 9));
            mList.add(new Menu(Objects.requireNonNull(getActivity()).getDrawable(R.drawable.ic_image_black_24dp),"Manage Gallery", 10));

            // mList.add(new Menu(Objects.requireNonNull(getActivity()).getDrawable(R.drawable.ic_report_black_24dp),"Report Book", 23));
        }






    }

    private void getWidthAndHeight() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenHeight = displaymetrics.heightPixels;
        screenWidth = displaymetrics.widthPixels;
    }
    // switching fragment
    private void switchFragment(Fragment fragment) {

        requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null).commit();
	/*	FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
		fragmentManager.popBackStack();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment)
				.addToBackStack("my_frame")
				.commit();
*/
        // FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        // ft.add(R.id.content_frame, fragment);
        // fragmentStack.lastElement().onPause();
        // ft.hide(fragmentStack.lastElement());
        // fragmentStack.push(fragment);
        // ft.commit();
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


	/*
	private void onClickDialog() {
		new FancyAlertDialog.Builder(getActivity())
				.setTitle("Rate us if you like the app")
				.setMessage("Do you really want to Exit ?")
				.setNegativeBtnText("Cancel")
				.setPositiveBtnText("Close")
				.setAnimation(Animation.POP)
				.isCancellable(true)
				.setIcon(R.drawable.logo, Icon.Visible)
				.OnPositiveClicked(new FancyAlertDialogListener() {
					@Override
					public boolean OnClick() {


						return false;
					}
				})
				.OnNegativeClicked(new FancyAlertDialogListener() {
					@Override
					public boolean OnClick() {

					//	bundle = new Bundle();
					//	newContent=new HomeFragment();
					//	newContent.setArguments(bundle);
					//	switchFragment(newContent);



						return false;
					}
				})
				.build();


	}
*/






}
