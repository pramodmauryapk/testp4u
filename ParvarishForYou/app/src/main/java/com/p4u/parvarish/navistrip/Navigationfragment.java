package com.p4u.parvarish.navistrip;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class Navigationfragment extends Fragment {
    private View v;
    private Context context;
    private ViewPager mViewPager;
    private FragmentPagerAdapter adapterViewPager;
   // private NavigationTabStrip mCenterNavigationTabStrip;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //v = inflater.inflate(R.layout.navigation_fragment,container,false);
        context = container.getContext();




        return v;
    }

    private void initUI() {
        //mViewPager = v. findViewById(R.id.vp);
       // mCenterNavigationTabStrip = v. findViewById(R.id.nts_center);
    }

    private void setUI() {
      /* mViewPager.setAdapter(new MyPagerAdapter(Objects.requireNonNull(getActivity()).getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public boolean isViewFromObject(@NonNull final View view, @NonNull final Object object) {
                return view.equals(object);
            }

            @Override
            public void destroyItem(@NonNull final View container, final int position, @NonNull final Object object) {
                ((ViewPager) container).removeView((View) object);
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
                final View view = new View(getContext());
                container.addView(view);
                return view;
            }
        });*/


     /*  mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
              mCenterNavigationTabStrip.setTabIndex(position);
            }
            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mCenterNavigationTabStrip.setTabIndex(position);
            }
            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        /*final NavigationTabStrip navigationTabStrip = (NavigationTabStrip) findViewById(R.id.nts_center);
        navigationTabStrip.setTitles("Nav", "Tab", "Strip");
        navigationTabStrip.setTabIndex(0, true);
        navigationTabStrip.setTitleSize(15);
        navigationTabStrip.setStripColor(Color.RED);
        navigationTabStrip.setStripWeight(6);
        navigationTabStrip.setStripFactor(2);
        navigationTabStrip.setStripType(NavigationTabStrip.StripType.LINE);
        navigationTabStrip.setStripGravity(NavigationTabStrip.StripGravity.BOTTOM);
        navigationTabStrip.setTypeface("fonts/typeface.ttf");
        navigationTabStrip.setCornersRadius(3);
        navigationTabStrip.setAnimationDuration(300);
        navigationTabStrip.setInactiveColor(Color.GRAY);
        navigationTabStrip.setActiveColor(Color.WHITE);*/
    /*    mCenterNavigationTabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onPageSelected(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
*/
    }
    @Override
    public void onStart(){

        super.onStart();
        initUI();
        //loadlist();
        setUI();
    }

  /*  private void loadlist() {
        adapterViewPager = new MyPagerAdapter(Objects.requireNonNull(getActivity()).getSupportFragmentManager());
        mViewPager.setAdapter(adapterViewPager);
        mCenterNavigationTabStrip.setOnTabStripSelectedIndexListener(new NavigationTabStrip.OnTabStripSelectedIndexListener() {

            @Override
            public void onStartTabSelected(String title, int index) {

                mViewPager.setCurrentItem(index);

            }

            @Override
            public void onEndTabSelected(String title, int index) {
                mViewPager.setCurrentItem(index);
            }
        });
    }
*/

}


