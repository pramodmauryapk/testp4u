package my.app.p4ulibrary.main_menu;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.Objects;

import my.app.p4ulibrary.R;
import my.app.p4ulibrary.classes.ViewPagerAdapter;
import my.app.p4ulibrary.home_for_all.AllBookFragment;
import my.app.p4ulibrary.home_for_all.AllCentreFragment;
import my.app.p4ulibrary.home_for_all.AllDonorFragment;
import my.app.p4ulibrary.home_for_all.AllUserFragment;


public class HomeFragment extends Fragment {
	ViewPager viewPager;
	private LinearLayout l1,l2,l3,l4;
	private View v1,v2,v3,v4;
	private View v;
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.home_fragment, container, false);
		Context context = container.getContext();
		viewPager = (ViewPager)v.findViewById(R.id.viewpager);

		ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter (getContext ());

		viewPager.setAdapter(viewPagerAdapter);
		iconsizesetting (getContext ());

		//                              //////////////////////////////////////////////////////////////////
		l1 = (LinearLayout) v.findViewById(R.id.userlist);

		l2 = (LinearLayout) v.findViewById(R.id.centerlist);

		l3 = (LinearLayout) v.findViewById(R.id.booklist);

		l4 = (LinearLayout) v.findViewById(R.id.donorlist);
		l1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				AllUserFragment fragment1 = new AllUserFragment ();
				switchFragment(fragment1);
			}
		});
		l2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				AllCentreFragment fragment2 = new AllCentreFragment ();
				switchFragment(fragment2);
			}
		});
		l3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				AllBookFragment fragment3 = new AllBookFragment ();
				switchFragment(fragment3);
			}
		});
		l4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				AllDonorFragment fragment4 = new AllDonorFragment ();
				switchFragment(fragment4);
			}
		});
		//////////////////////////////////////////////
		return v;

	}

	


	@Override
	public void onResume() {

		super.onResume();
	}

	// switching fragment
	@SuppressWarnings("unused")
	private void switchFragment(Fragment fragment) {


		Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, fragment)
				.addToBackStack("my_fragment").commit();
	}

	public boolean onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull (getContext ()));
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
		alert.show();
		return false;

	}
	private void iconsizesetting(Context context)
	{
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		assert wm != null;
		Display display = wm.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics ();
		display.getMetrics(metrics);
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;

		View view1 = v.findViewById(R.id.userlist);
		View view2 = v.findViewById(R.id.centerlist);
		View view3 = v.findViewById(R.id.booklist);
		View view4 = v.findViewById(R.id.donorlist);
		ViewGroup.LayoutParams layoutParams1 = view1.getLayoutParams();
		layoutParams1.width = width/4;
		view1.setLayoutParams(layoutParams1);
		ViewGroup.LayoutParams layoutParams2 = view2.getLayoutParams();
		layoutParams2.width = width/4;
		view2.setLayoutParams(layoutParams2);
		ViewGroup.LayoutParams layoutParams3 = view3.getLayoutParams();
		layoutParams3.width = width/4;
		view3.setLayoutParams(layoutParams3);
		ViewGroup.LayoutParams layoutParams4 = view4.getLayoutParams();
		layoutParams4.width = width/4;
		view4.setLayoutParams(layoutParams4);
	}



}