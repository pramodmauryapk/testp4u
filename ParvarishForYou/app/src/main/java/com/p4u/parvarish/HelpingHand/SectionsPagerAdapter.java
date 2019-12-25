package com.p4u.parvarish.HelpingHand;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.p4u.parvarish.R;


public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};//,R.string.tab_text_3
    private final Context mContext;

    SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0: //Fragment # 0 - This will show ThirdFragment
                return ShowHalpingHandFragment.newInstance(position);
            case 1:
                // Fragment # 0 - This will show ThirdFragment different title
                return UserwiseFragment.newInstance(position);
           // case 2: // Fragment # 1 - This will show SecondFragment
           //     return PlaceholderFragment.newInstance(position);


        }
        return ShowHalpingHandFragment.newInstance(position);


    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 2;
    }

}