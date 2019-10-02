package com.p4u.parvarish.main_menu;

import android.content.Context;
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
import java.util.Stack;

import com.p4u.parvarish.R;
import com.p4u.parvarish.galary.ImageGalleryFragment;
import com.p4u.parvarish.grid.Menu;
import com.p4u.parvarish.grid.RecycleAdapter;

import static java.util.Objects.requireNonNull;

public class UserMenuFragment extends HomeFragment{


    private FirebaseAuth mAuth;
    private View v;
    DatabaseReference myRef;
    private List<Menu> mList = new ArrayList<>();
    private String Role;
    private FirebaseUser user;

    Context context;
    private int screenWidth;
    private int screenHeight;
    private Fragment child2Fragment;
    MainActivity my;
    int i;
    private Bundle bundle;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_menu,container,false);

        bundle=new Bundle();
        context= container.getContext();
        my=new MainActivity();
        my.fragmentStack = new Stack<>();

      //  getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null);
        Role = requireNonNull(this.getArguments()).getString("user_role");
        getWidthAndHeight();

        addmenus();

        add_click_listner();



        return v;
    }



    private void add_click_listner() {

        RecyclerView mRecyclerView = v.findViewById(R.id.cardList);
        GridLayoutManager gd=new GridLayoutManager(context,screenWidth/200 );

        mRecyclerView.setLayoutManager(gd);
        RecycleAdapter mAdapter = new RecycleAdapter(getContext(), mList);

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //makeToast("" + mList.get(position).getPosition());
                switch (mList.get(position).getPosition()) {
                    case 0:
                        UserMenuFragment.this.switchFragment(new TelentSupportFragment());
                        i = 0;
                        break;
                    case 1:
                        UserMenuFragment.this.switchFragment(new AdminMenuFragment());
                        i = 1;
                        break;
                    case 2:
                        UserMenuFragment.this.switchFragment(new TechnicalPartnershipFragment());
                        i = 2;
                        break;
                    case 3:
                        UserMenuFragment.this.switchFragment(new CapacityBuildingFragment());
                        i = 3;
                        break;
                    case 4:
                        UserMenuFragment.this.switchFragment(new ImageGalleryFragment());
                        i = 4;
                        break;


                }

            }
        });
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void addmenus() {

        mList.clear();

        mList.add(new Menu(Objects.requireNonNull(getActivity()).getDrawable(R.drawable.ic_touch_app_black_24dp),"Telent Support", 0));
        mList.add(new Menu(Objects.requireNonNull(getActivity()).getDrawable(R.drawable.ic_account_balance_black_24dp),"Knowledge Center", 1));
        mList.add(new Menu(Objects.requireNonNull(getActivity()).getDrawable(R.drawable.ic_pets_black_24dp),"Technical Partnership", 2));
        mList.add(new Menu(Objects.requireNonNull(getActivity()).getDrawable(R.drawable.ic_business_black_24dp),"Capacity Building", 3));
        mList.add(new Menu(Objects.requireNonNull(getActivity()).getDrawable(R.drawable.ic_camera_black_24dp),"Gallery", 4));
     //   mList.add(new Menu(Objects.requireNonNull(getActivity()).getDrawable(R.drawable.ic_school_black_24dp),"Centers", 13));
     //   mList.add(new Menu(Objects.requireNonNull(getActivity()).getDrawable(R.drawable.ic_import_contacts_black_24dp),"Exams", 14));
     //   mList.add(new Menu(Objects.requireNonNull(getActivity()).getDrawable(R.drawable.ic_search_black_24dp),"Search--", 15));
        //   mList.add(new Menu(Objects.requireNonNull(getActivity()).getDrawable(R.drawable.ic_format_align_center_black_24dp),"Articles", 8));
        //   mList.add(new Menu(Objects.requireNonNull(getActivity()).getDrawable(R.drawable.ic_ondemand_video_black_24dp),"Videos", 9));
        //  mList.add(new Menu(Objects.requireNonNull(getActivity()).getDrawable(R.drawable.ic_format_list_bulleted_black_24dp),"Newsletter", 10));
        //    mList.add(new Menu(Objects.requireNonNull(getActivity()).getDrawable(R.drawable.ic_file_download_black_24dp),"Downloads", 11));




    }

    private void makeToast(String input) {
        Toast.makeText(getContext(), input, Toast.LENGTH_SHORT).show();
    }

    private void getWidthAndHeight() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        requireNonNull(getActivity()).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        screenWidth = displaymetrics.widthPixels;
    }
    // switching fragment
    private void switchFragment(Fragment fragment) {
        bundle.putString("user_role",Role);
        fragment.setArguments(bundle);
        my.fragmentStack.push(fragment);
        requireNonNull(getActivity()).getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null)
                .commit();
	/*	FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
		fragmentManager.popBackStack();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment)
				.addToBackStack("my_frame")
				.commit();
*/

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
