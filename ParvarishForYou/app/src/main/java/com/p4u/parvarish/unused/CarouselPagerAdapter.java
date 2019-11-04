package com.p4u.parvarish.unused;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.p4u.parvarish.R;

import java.util.Objects;

public class CarouselPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {
    private static final String TAG = "CarouselPageAdapter";
    final static float BIG_SCALE = 1.0f;
    private final static float SMALL_SCALE = 0.7f;
    private final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;
    private ImageGalleryFragment context;
    private FragmentManager fragmentManager;
    private float scale;

    CarouselPagerAdapter(ImageGalleryFragment context, FragmentManager fm) {
        super(fm);
        this.fragmentManager = fm;
        this.context = context;
    }
@NonNull
    @Override
    public Fragment getItem(int position) {
        // make the first pager bigger than others
        try {
            if (position == ImageGalleryFragment.FIRST_PAGE)
                scale = BIG_SCALE;
            else
                scale = SMALL_SCALE;

            position = position % ImageGalleryFragment.count;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return GalleryItemFragment.newInstance(context, position, scale);
    }

    @Override
    public int getCount() {
        int count = 0;
        try {
            count = ImageGalleryFragment.count * ImageGalleryFragment.LOOPS;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        try {
            if (positionOffset >= 0f && positionOffset <= 1f) {
                CarouselLayout cur = getRootView(position);
                CarouselLayout next = getRootView(position + 1);

                cur.setScaleBoth(BIG_SCALE - DIFF_SCALE * positionOffset);
                next.setScaleBoth(SMALL_SCALE + DIFF_SCALE * positionOffset);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private CarouselLayout getRootView(int position) {
        return (CarouselLayout) Objects.requireNonNull(Objects.requireNonNull(fragmentManager.findFragmentByTag(this.getFragmentTag(position)))
                .getView()).findViewById(R.id.root_container);
    }

    private String getFragmentTag(int position) {
        return "android:switcher:" + context.pager.getId() + ":" + position;
    }
}