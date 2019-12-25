package com.p4u.parvarish.HelpingHand;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.p4u.parvarish.R;

public class HelpingFragment extends Fragment {
    private ViewPager mViewPager;
    private FragmentPagerAdapter adapterViewPager;
    private View v;
    private Context context;
    private ViewPager viewPager;
    private TabLayout tabs;
    private SectionsPagerAdapter sectionsPagerAdapter;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.helping_main, container, false);
        context = container.getContext();
        init();
        sectionsPagerAdapter = new SectionsPagerAdapter(context, getChildFragmentManager());
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs.setupWithViewPager(viewPager);
        return v;
    }
    private void init() {
        viewPager = v. findViewById(R.id.view_pager);
        tabs = v.findViewById(R.id.tabs);
    }
    public void onStart() {
        super.onStart();
    }
}